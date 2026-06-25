package pl.miodun.server;

import org.jetbrains.annotations.NotNull;
import pl.miodun.game.Duel;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"CallToPrintStackTrace", "UseOfSystemOutOrSystemErr"})
public final class Server implements AutoCloseable {

	private final @NotNull ServerSocket serverSocket;
	private final @NotNull Database database;

	private final @NotNull Collection<ClientHandler> clients = new ArrayList<>();

	private Server(final int port) throws IOException, SQLException {
		this.serverSocket = new ServerSocket(port);
		this.database = new Database();
	}

	static void main() throws Exception {
		try (final var server = new Server(5000)) {
			server.listen();
		}
	}

	public @NotNull Database getDatabase() {
		return this.database;
	}

	public void listen() {
		System.out.printf("Listening on %s:%d%n", this.serverSocket.getInetAddress(), this.serverSocket.getLocalPort());
		while (true) {
			try {
				if (this.serverSocket.isClosed()) break;
				final var clientSocket = this.serverSocket.accept();
				final ClientHandler client;
				try {
					client = new ClientHandler(this, clientSocket);
				} catch (final Exception e) {
					System.err.println("Failed to connect client");
					e.printStackTrace();

					clientSocket.close();
					continue;
				}

				client.getThread().start();
				this.clients.add(client);

				client.send(Protocol.S_REQUEST_LOGIN);

			} catch (final Exception e) {
				System.err.println("Server socket threw an exception");
				e.printStackTrace();
			}
		}
	}

	public void disconnect(@NotNull final ClientHandler client) {
		try {
			client.close();
		} catch (final Exception e) {
			System.err.println("Failed to disconnect client");
			e.printStackTrace();
		}

		this.clients.remove(client);
	}

	public void challengeToDuel(@NotNull final ClientHandler challenger, @NotNull final String challengeeLogin) throws IOException {
		final var challengee = this.clients.stream()
			.filter(ClientHandler::isAuthenticated)
			.filter(c -> challengeeLogin.equalsIgnoreCase(c.getLogin()))
			.findFirst().orElse(null);

		if (challengee == null) {
			challenger.send(Protocol.S_OPPONENT_NOT_FOUND);
			return;
		}

		if (challenger == challengee) {
			challenger.send(Protocol.S_OPPONENT_SELF);
			return;
		}

		if (challengee.isDuelling()) {
			challenger.send(Protocol.S_OPPONENT_BUSY);
			return;
		}

		this.startDuel(challenger, challengee);
	}

	private void startDuel(@NotNull final ClientHandler challenger, @NotNull final ClientHandler challengee) throws IOException {
		final var duel = new Duel(challenger, challengee);
		challenger.send(Protocol.S_DUEL_STARTED, Protocol.createDuelStartedData(challengee));
		challengee.send(Protocol.S_DUEL_STARTED, Protocol.createDuelStartedData(challenger));

		duel.onEnd(() -> this.finishDuel(duel, challenger, challengee));
	}

	private void finishDuel(@NotNull final Duel duel, @NotNull final ClientHandler challenger, @NotNull final ClientHandler challengee) {
		final var result = duel.evaluate();
		try {
			challenger.send(Protocol.S_DUEL_RESULT, Protocol.createDuelResultData(challenger, result));
		} catch (final Exception e) {
			System.err.println("Failed to summarize duel for client");
			e.printStackTrace();
		}
		try {
			challengee.send(Protocol.S_DUEL_RESULT, Protocol.createDuelResultData(challengee, result));
		} catch (final Exception e) {
			System.err.println("Failed to summarize duel for client");
			e.printStackTrace();
		}

		if (result != null) {
			this.database.updateLeaderboard(
				((ClientHandler) result.winner()).getLogin(),
				((ClientHandler) result.loser()).getLogin()
			);
		}

		System.out.println("Tablica wyników:");
		try {
			for (final var entry : this.database.getLeaderboard().entrySet()) {
				System.out.printf("%s\t%d%n", entry.getKey(), entry.getValue());
			}
		} catch (final SQLException e) {
			System.err.println("Błąd");
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {
		this.serverSocket.close();
	}

}
