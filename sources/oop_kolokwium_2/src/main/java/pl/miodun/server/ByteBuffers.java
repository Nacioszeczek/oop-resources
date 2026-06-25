package pl.miodun.server;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class ByteBuffers {

	private ByteBuffers() {
	}

	public static @NotNull String readString(@NotNull final ByteBuffer buf) {
		final var length = buf.getInt();
		final var data = new byte[length];
		buf.get(data, 0, length);
		return new String(data, StandardCharsets.UTF_8);
	}

}
