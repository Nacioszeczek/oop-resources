import java.util.Map;

public class Database {

    public Database() {
    }

    public boolean authenticate(String login, String password) {
        // "SELECT * FROM users WHERE login = ? AND password = ?";
        return true;
    }

    public void updateLeaderboard(String winner, String loser) {
        // "UPDATE users SET points = points + 1 WHERE login = ?";
        // "UPDATE users SET points = points - 1 WHERE login = ?";
    }

    public Map<String, Integer> getLeaderboard() {
        // "SELECT login, points FROM users ORDER BY points DESC";
        return Map.of("user", 0);
    }
}
