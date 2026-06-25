package pl.umcs.oop.music;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.umcs.oop.database.DatabaseConnection;

import java.util.Optional;

public class SongTest {

    @BeforeAll
    static void connectToDatabase() {
        DatabaseConnection.connect("songs.db");
    }

    @Test
    void testGetSongFromDatabaseWithCorrectIndex() {
        Optional<Song> result = Song.Persistence.read(1);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(new Song("The Beatles", "Hey Jude", 431), result.get());
    }

    @AfterAll
    static void disconnectFromDatabase() {
        DatabaseConnection.disconnect();
    }
}
