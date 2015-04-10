package sound; 

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *This enum encapsulates all of the different wav files associated with the card matching game.
 * 
 * @author Brian Dennis
 * @author Brandon Soto
 * @version 8/12/2014
 */
public enum SoundEffect {
    
    THEME("tetris theme.wav", true);
    
    /** True if the clip should endlessly loop. Otherwise false. */
    private boolean myLoopStatus;
       
    /** Each sound effect has its own clip, loaded with its own sound file.*/
    private Clip myClip;
       
    /**
     * Constructs a SoundEffect with a specified file name and loop status. 
     * @param theSoundFileName the name of the audio file. 
     * @param loopStatus true if the sound should endlessly loop. Otherwise false. 
     */
    SoundEffect(final String theSoundFileName, final boolean loopStatus) {
        try {
            
            final AudioInputStream stream = AudioSystem.getAudioInputStream(
                                                    getClass().getResource(theSoundFileName));
            myClip = AudioSystem.getClip();
            myClip.open(stream);
            
            myLoopStatus = loopStatus;
        } catch (final UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final LineUnavailableException e) {
            e.printStackTrace();
        }
    }
       
   /** 
    * Play or Re-play the sound effect from the beginning, by rewinding.
    */
    public void play() {
        myClip.loop((myLoopStatus ? Clip.LOOP_CONTINUOUSLY : 1)); 
    }
    
    /** Restarts the sound effect from its first frame. */ 
    public void restart() {
        stop(); 
        myClip.setFramePosition(0);
        play(); 
    }
    
    /** Stops the clip from playing. */ 
    public void stop() {
        myClip.stop();
    }
       
   /** 
    * Optional static method to pre-load all the sound files. 
    */
    static void init() {
        values(); // calls the constructor for all the elements
    }
}