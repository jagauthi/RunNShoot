package Controller;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioManager {

    long pauseTime;
    Clip clip;
    AudioInputStream ais;
    boolean playing;

    public AudioManager()
    {
        pauseTime = 0;
        playing = false;
    }

    public void playSong(String Filename)
    {
        try{
            File f = new File(Filename);
            clip = AudioSystem.getClip();
            ais = AudioSystem.getAudioInputStream(f);
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            playing = true;
            if(Filename.equals("resources/sounds/SnowZone.wav")) {
                setVolume(0.15f);
            }
            else {
                setVolume(0.25f);
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void pauseSong()
    {
        pauseTime = clip.getMicrosecondPosition();
        clip.stop();
        playing = false;
    }

    public void resumeSong()
    {
        clip.setMicrosecondPosition(pauseTime);
        clip.start();
        playing = true;
    }

    public void stopSong()
    {
        clip.stop();
        clip.close();
        playing = false;
    }

    public Boolean isPlaying()
    {
        return playing;
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume not valid: " + volume);
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}