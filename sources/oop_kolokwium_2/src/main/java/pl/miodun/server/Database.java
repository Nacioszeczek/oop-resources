package pl.miodun.server;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"CallToPrintStackTrace", "UseOfSystemOutOrSystemErr"})
public class Database {

	private final @NotNull Connection connection;

	public Database() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:sqlite:users.db");
	}

	public boolean authenticate(@NotNull final String login, @NotNull final String password) {
		// "SELECT * FROM users WHERE login = ? AND password = ?";
		try (final var stmt = this.connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?")) {
			stmt.setString(1, login);
			stmt.setString(2, password);
			try (final var result = stmt.executeQuery()) {
				return result.next();
			}
		} catch (final SQLException e) {
			System.err.printf("Failed to authenticate: %s%n", login);
			e.printStackTrace();
			return false;
		}
	}

	public void updateLeaderboard(@NotNull final String winner, @NotNull final String loser) {
		// "UPDATE users SET points = points + 1 WHERE login = ?";
		// "UPDATE users SET points = points - 1 WHERE login = ?";
		try (final var stmt1 = this.connection.prepareStatement("UPDATE users SET points = points + 1 WHERE login = ?");
		     final var stmt2 = this.connection.prepareStatement("UPDATE users SET points = points - 1 WHERE login = ?")) {
			stmt1.setString(1, winner);
			stmt2.setString(1, loser);

			stmt1.executeUpdate();
			stmt2.executeUpdate();
		} catch (final SQLException e) {
			System.err.println("Failed to update leaderboard");
			e.printStackTrace();
		}
	}

	public @NotNull Map<String, Integer> getLeaderboard() throws SQLException {
		// "SELECT login, points FROM users ORDER BY points DESC";
		final var map = new HashMap<String, Integer>();
		try (final var stmt = this.connection.createStatement();
		     final var result = stmt.executeQuery("SELECT login, points FROM users ORDER BY points DESC")) {
			while (result.next()) {
				map.put(result.getString(1), result.getInt(2));
			}
		}
		return map;
	}

}
