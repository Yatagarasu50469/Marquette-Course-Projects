/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package galaga;

import java.io.FileInputStream;
import java.io.IOException;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Computer1
 */
class Sounds {

    public static void gameOpeningMenuMusic() throws IOException, InterruptedException {

        AudioStream openingMenuMusic = new AudioStream(new FileInputStream("sounds/GalagaQuestMainMenu.wav")); //Creates an input stream and constructs an audio stream rom it     
        if (!Galaga.gameOpeningMenu) {
            AudioPlayer.player.start(openingMenuMusic);    //Audio player begins to play openingMenuMusic audio file
            Galaga.gameOpeningMenuMusicCheck = true;
            new Galaga();
            while (!Galaga.gameOpeningMenu) {
                Thread.sleep(1);
            }
            AudioPlayer.player.stop(openingMenuMusic);

        }
    }
}
