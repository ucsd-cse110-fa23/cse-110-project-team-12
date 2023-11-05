package edu.ucsd.cse110.api;

import java.io.File;
import javax.sound.sampled.*;

public class VoicePrompt {
    private static AudioFormat audioFormat;
    private static TargetDataLine targetDataLine;
    private static Thread t;
    private static AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }
    
    public static void startRecording() {
        audioFormat = getAudioFormat();
        try {
            // the format of the TargetDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class,
                    audioFormat);
            // the TargetDataLine used to capture audio data from the microphone

            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

            targetDataLine.open(audioFormat);
            targetDataLine.start();
            
            t = new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File("local_storage/recording.wav");
                        AudioSystem.write(
                                    audioInputStream,
                                    AudioFileFormat.Type.WAVE,
                                    audioFile);
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	public static File stopRecording() {
        targetDataLine.stop();
        targetDataLine.flush();
        targetDataLine.close();
		return new File("local_storage/recording.wav");
	}
}