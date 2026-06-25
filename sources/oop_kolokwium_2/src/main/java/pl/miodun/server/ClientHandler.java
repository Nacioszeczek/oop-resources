package pl.miodun.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pl.miodun.game.Gesture;
import pl.miodun.game.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class ClientHandler extends Player implements AutoCloseable {

	private final @NotNull Server server;
	private final @NotNull Socket socket;
	private final @NotNull Thread thread;

	private final @NotNull InputStream in;
	private final @NotNull OutputStream out;

	private @UnknownNullability String login;
	private @NotNull State state = State.INDETERMINATE;

	public ClientHandler(@NotNull final Server server, @NotNull final Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;
		this.thread = new Thread(this::listen);

		this.in = this.socket.getInputStream();
		this.out = this.socket.getOutputStream();
	}

	public @NotNull Thread getThread() {
		return this.thread;
	}

	public @UnknownNullability String getLogin() {
		return this.login;
	}

	public boolean isAuthenticated() {
		return this.state == State.CONNECTED;
	}

	public void send(final byte opcode) throws IOException {
		this.send(opcode, null);
	}

	public void send(final byte opcode, final byte @Nullable [] data) throws IOException {
		if (opcode < 0)
			throw new IllegalArgumentException("Client-sent opcode");

		switch (opcode) {
			case Protocol.S_REQUEST_LOGIN -> this.state = State.AWAITING_LOGIN;
			case Protocol.S_REQUEST_PASSWORD -> this.state = State.AWAITING_PASSWORD;
			default -> {
				if (!this.isAuthenticated())
					throw new IllegalStateException("Trying to send data to an unauthenticated client");
			}
		}

		final var buf = ByteBuffer.allocate(Byte.BYTES + Integer.BYTES + (data != null ? data.length : 0));
		buf.put(opcode);
		buf.putInt(data != null ? data.length : 0);
		if (data != null) buf.put(data);

		this.out.write(Arrays.copyOf(buf.array(), buf.capacity()));
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

	private void listen() {
		try {
			while (this.state == State.INDETERMINATE) {
				if (this.socket.isClosed()) break;
				//noinspection BusyWait
				Thread.sleep(100);
			}
			while (true) {
				if (this.socket.isClosed()) break;
				final var opcode = this.in.read();
				if (opcode == -1) break;

				final var length = ByteBuffer.wrap(this.in.readNBytes(Integer.BYTES)).getInt();
				final var data = this.in.readNBytes(length);
				this.handle((byte) opcode, data);
			}
		} catch (final Exception e) {
			//noinspection UseOfSystemOutOrSystemErr
			System.err.println("Client handler failed");
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
		} finally {
			this.disconnect();
		}
	}

	private void handle(final byte opcode, final byte @NotNull [] data) throws IOException {
		final var buf = ByteBuffer.wrap(data);

		switch (opcode) {
			case Protocol.C_PROVIDE_LOGIN -> {
				if (this.state != State.AWAITING_LOGIN)
					throw new IllegalStateException("Client provides login without reason");

				this.login = ByteBuffers.readString(buf);
				this.send(Protocol.S_REQUEST_PASSWORD);
			}
			case Protocol.C_PROVIDE_PASSWORD -> {
				if (this.state != State.AWAITING_PASSWORD)
					throw new IllegalStateException("Client provides password without reason");
				if (this.login == null)
					throw new IllegalStateException("Client login is null");

				final var password = ByteBuffers.readString(buf);
				if (!this.server.getDatabase().authenticate(this.login, password)) {
					System.out.printf("Client failed authentication: %s%n", this.login);
					this.disconnect();
					break;
				}

				this.state = State.CONNECTED;
				this.send(Protocol.S_AUTHENTICATED);
			}
			case Protocol.C_REQUEST_DUEL -> {
				if(this.state != State.CONNECTED || this.isDuelling())
					throw new IllegalStateException("Client requests duel without reason");

				final var challengee = ByteBuffers.readString(buf);
				this.server.challengeToDuel(this, challengee);
			}
			case Protocol.C_MAKE_GESTURE -> {
				if (this.state != State.CONNECTED || !this.isDuelling())
					throw new IllegalStateException("Client provides gesture without reason");

				final var gesture = Gesture.fromString(ByteBuffers.readString(buf));
				if (gesture == null) break;

				this.makeGesture(gesture);
			}
			default -> throw new IllegalStateException("Client sends an unknown opcode");
		}
	}

	private void disconnect() {
		this.server.disconnect(this);
	}

	public enum State {
		INDETERMINATE,
		AWAITING_LOGIN,
		AWAITING_PASSWORD,
		CONNECTED
	}

}
