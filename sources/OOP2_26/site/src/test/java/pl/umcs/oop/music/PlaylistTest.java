package pl.umcs.oop.music;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaylistTest {
    @Test
    void testNewPlaylistsIsEmpty() {
        //given
        //when
        Playlist pl1 = new Playlist();
        //then
        Assertions.assertTrue(pl1.isEmpty());
    }
    @Test
    void testAddSongToEmptyMakesSize1() {
        // given
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        // when
        pl1.add(s1);
        // then
        Assertions.assertEquals(1, pl1.size());
    }
    @Test
    void testAddSongContainsThisSong() {
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        pl1.add(s1);
        Assertions.assertTrue(pl1.contains(s1));
    }

    @Test
    void testAddSongContainsSameSong() {
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        Song s2 = new Song("aaa", "ttt", 100);

        pl1.add(s1);
        Assertions.assertTrue(pl1.contains(s2));
    }

    @Test
    void testAtSecond(){
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        Song s2 = new Song("aab", "ttb", 90);
        Song s3 = new Song("aac", "ttc", 80);
        pl1.add(s1);
        pl1.add(s2);
        pl1.add(s3);

        Song result = pl1.atSecond(99);

        Assertions.assertEquals(s1, result);
    }

    @Test
    void testAtSecondAfterTimeThrowsException(){
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        Song s2 = new Song("aab", "ttb", 90);
        Song s3 = new Song("aac", "ttc", 80);
        pl1.add(s1);
        pl1.add(s2);
        pl1.add(s3);

        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () ->  pl1.atSecond(270)
        );
    }

    @Test
    void testAtSecondBelow0ThrowsException(){
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        Song s2 = new Song("aab", "ttb", 90);
        Song s3 = new Song("aac", "ttc", 80);
        pl1.add(s1);
        pl1.add(s2);
        pl1.add(s3);

        IndexOutOfBoundsException e = Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () ->  pl1.atSecond(-1)
        );

        Assertions.assertEquals("Sekunda poniżej 0", e.getMessage());
    }

    @Test
    void testAtSecondTooBigThrowsExceptionWithMessage(){
        Playlist pl1 = new Playlist();
        Song s1 = new Song("aaa", "ttt", 100);
        Song s2 = new Song("aab", "ttb", 90);
        Song s3 = new Song("aac", "ttc", 80);
        pl1.add(s1);
        pl1.add(s2);
        pl1.add(s3);

        IndexOutOfBoundsException e = Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () ->  pl1.atSecond(270)
        );

        Assertions.assertEquals("Sekunda wykracza poza czas trwania", e.getMessage());
    }


}
