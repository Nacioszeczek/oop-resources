package pl.miodun.extra;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.miodun.server.ByteBuffers;
import pl.miodun.server.Protocol;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
public final class Client implements AutoCloseable {

	private final @NotNull Socket socket;
	private final @NotNull InputStream in;
	private final @NotNull OutputStream out;

	private @NotNull State state = State.INDETERMINATE;
	private @Nullable String duelling;

	public Client() throws IOException {
		this.socket = new Socket("127.0.0.1", 5000);
		this.in = this.socket.getInputStream();
		this.out = this.socket.getOutputStream();
	}

	static void main() throws Exception {
		try (final var client = new Client()) {
			final var clientThread = new Thread(client::listen);
			clientThread.start();
			try (final var reader = new BufferedReader(new InputStreamReader(System.in))) {
				String line;
				while ((line = reader.readLine()) != null) {
					client.handleInput(line);
				}
			}
		}
	}

	public void send(final byte opcode, final byte @Nullable [] data) throws IOException {
		if (opcode > 0)
			throw new IllegalArgumentException("Server-sent opcode");

		final var buf = ByteBuffer.allocate(Byte.BYTES + Integer.BYTES + (data != null ? data.length : 0));
		buf.put(opcode);
		buf.putInt(data != null ? data.length : 0);
		if (data != null) buf.put(data);

		this.out.write(Arrays.copyOf(buf.array(), buf.capacity()));
	}

	public void listen() {
		try {
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
			System.err.println("Failed");
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
		}
	}

	public void handleInput(@NotNull final String input) throws IOException {
		switch (this.state) {
			case INDETERMINATE -> System.out.println("Czekaj");
			case REQUIRE_LOGIN -> this.send(Protocol.C_PROVIDE_LOGIN, Protocol.createStringData(input));
			case REQUIRE_PASSWORD -> this.send(Protocol.C_PROVIDE_PASSWORD, Protocol.createStringData(input));
			case WAITING -> this.send(Protocol.C_REQUEST_DUEL, Protocol.createStringData(input));
			case DUELLING -> this.send(Protocol.C_MAKE_GESTURE, Protocol.createStringData(input));
			default -> throw new IllegalStateException("Unknown state");
		}
	}

	private void handle(final byte opcode, final byte @NotNull [] data) throws IOException {
		final var buf = ByteBuffer.wrap(data);

		switch (opcode) {
			case Protocol.S_REQUEST_LOGIN -> {
				System.out.print("Wymagany login: ");
				this.state = State.REQUIRE_LOGIN;
			}
			case Protocol.S_REQUEST_PASSWORD -> {
				System.out.print("Wymagane hasło: ");
				this.state = State.REQUIRE_PASSWORD;
			}
			case Protocol.S_DUEL_STARTED -> {
				this.duelling = ByteBuffers.readString(buf);
				System.out.printf("Pojedynek rozpoczęty z %s, podaj gest: ", this.duelling);
				this.state = State.DUELLING;
			}
			case Protocol.S_DUEL_RESULT -> {
				final var result = buf.get();
				switch (result) {
					case Protocol.SD_DUEL_RESULT_WON -> System.out.printf("Wygrałeś z %s!%n", this.duelling);
					case Protocol.SD_DUEL_RESULT_TIE -> System.out.printf("Remis z %s!%n", this.duelling);
					case Protocol.SD_DUEL_RESULT_LOST -> System.out.printf("Przegrałeś z %s!%n", this.duelling);
				}
				this.duelling = null;
				this.state = State.WAITING;
			}
			case Protocol.S_AUTHENTICATED -> {
				System.out.println("Zalogowano prawidłowo");
				this.state = State.WAITING;
			}
			case Protocol.S_OPPONENT_NOT_FOUND -> System.out.println("Przeciwnik nie znaleziony");
			case Protocol.S_OPPONENT_SELF -> System.out.println("Nie możesz wyzwać samego siebie");
			case Protocol.S_OPPONENT_BUSY -> System.out.println("Przeciwnik jest zajęty");
			default -> throw new IllegalStateException("Server sends an unknown opcode");
		}
	}

	@Override
	public void close() throws Exception {
		this.socket.close();
	}

	private enum State {
		INDETERMINATE,
		REQUIRE_LOGIN,
		REQUIRE_PASSWORD,
		WAITING,
		DUELLING
	}

}
