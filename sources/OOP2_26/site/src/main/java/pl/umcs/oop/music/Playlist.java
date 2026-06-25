package pl.umcs.oop.music;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {

    public Song atSecond(int second){
        if (second < 0) {
            throw new IndexOutOfBoundsException("Sekunda poniżej 0");
        }
        int secondsOfSongs = 0;
        for(int i=0;i<this.size();i++){
            secondsOfSongs += this.get(i).duration();
            if(secondsOfSongs > second){
                return this.get(i);
            }
        }

        throw new IndexOutOfBoundsException("Sekunda wykracza poza czas trwania");

    }
}
