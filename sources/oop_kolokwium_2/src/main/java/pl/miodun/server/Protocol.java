package pl.miodun.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.miodun.game.Duel;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class Protocol {

	// Server-sent opcodes
	public static final byte S_REQUEST_LOGIN = 1;
	public static final byte S_REQUEST_PASSWORD = 2;
	public static final byte S_AUTHENTICATED = 3;
	public static final byte S_DUEL_STARTED = 4;
	public static final byte S_DUEL_RESULT = 5;

	// Server-sent errors
	public static final byte S_OPPONENT_NOT_FOUND = 127;
	public static final byte S_OPPONENT_SELF = 126;
	public static final byte S_OPPONENT_BUSY = 125;

	// Server-sent data constants
	public static final byte SD_DUEL_RESULT_WON = 1;
	public static final byte SD_DUEL_RESULT_TIE = 0;
	public static final byte SD_DUEL_RESULT_LOST = -1;

	// Client-sent opcodes
	public static final byte C_PROVIDE_LOGIN = -1;
	public static final byte C_PROVIDE_PASSWORD = -2;
	public static final byte C_REQUEST_DUEL = -3;
	public static final byte C_MAKE_GESTURE = -4;

	private Protocol() {

	}

	public static byte[] createDuelStartedData(@NotNull final ClientHandler opponent) {
		final var login = opponent.getLogin();
		if (login == null)
			throw new IllegalStateException("Client has no login");

		return createStringData(login);
	}

	public static byte[] createDuelResultData(@NotNull final ClientHandler receiver, @Nullable final Duel.Result result) {
		return new byte[] {result == null ? SD_DUEL_RESULT_TIE : (receiver == result.winner() ? SD_DUEL_RESULT_WON : SD_DUEL_RESULT_LOST)};
	}

	public static byte[] createStringData(@NotNull final String str) {
		final var encoded = StandardCharsets.UTF_8.encode(str);
		final var result = ByteBuffer.allocate(Integer.BYTES + encoded.capacity());
		result.putInt(encoded.capacity());
		result.put(encoded);
		return result.array();
	}

}
