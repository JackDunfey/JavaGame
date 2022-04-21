package game;

// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    public static enum SoundStatus{
        PLAYING,
        PAUSED,
        STOPPED
    }

    public static Clip getClip(String filename) throws Exception{
        File f = new File(filename);
        var clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(f.getAbsoluteFile()));
        return clip;
    }

	// to store current position
	Long currentFrame;
	Clip clip;
	// current status of clip
	SoundStatus status;
	AudioInputStream audioInputStream;
	static String filePath;

	public Sound(String filename) throws Exception {
        this.clip = getClip(filename);
	}

	public static void main(String[] args)
	{
		try
		{
			Sound audioPlayer = new Sound("resources/sounds/explode.wav");
            audioPlayer.play();
            while(true){
                Scanner s = new Scanner(System.in);
                s.next();
				s.close();
                break;
            }
		}
		
		catch (Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		
		}
	}
	// Method to play the audio
	public void play()
	{
		clip.start();
		status = SoundStatus.PLAYING;
	}
	
	// Method to pause the audio
	public void pause()
	{
		if (status == SoundStatus.PAUSED)
		{
			System.out.println("audio is already paused");
			return;
		}
		this.currentFrame =
		this.clip.getMicrosecondPosition();
		clip.stop();
		status = SoundStatus.PAUSED;
	}
	
	// Method to resume the audio
	public void resumeAudio() throws Exception {
		if (status == SoundStatus.PLAYING){
			System.out.println("Audio is already "+
			"being played");
			return;
		}
		clip.close();
		resetAudioStream();
		clip.setMicrosecondPosition(currentFrame);
		this.play();
	}
	
	// Method to restart the audio
	public void restart() throws Exception {
		clip.stop();
		clip.close();
		resetAudioStream();
		currentFrame = 0L;
		clip.setMicrosecondPosition(0);
		this.play();
	}
	
	// Method to stop the audio
	public void stop() throws Exception {
        status = SoundStatus.STOPPED;
		currentFrame = 0L;
		clip.stop();
		clip.close();
	}
	
	// Method to jump over a specific part
	public void jump(long c) throws Exception {
		if (c > 0 && c < clip.getMicrosecondLength())
		{
			clip.stop();
			clip.close();
			resetAudioStream();
			currentFrame = c;
			clip.setMicrosecondPosition(c);
			this.play();
		}
	}
	
	// Method to reset audio stream
	public void resetAudioStream() throws Exception {
		audioInputStream = AudioSystem.getAudioInputStream(
		new File(filePath).getAbsoluteFile());
		clip.open(audioInputStream);
	}
}
