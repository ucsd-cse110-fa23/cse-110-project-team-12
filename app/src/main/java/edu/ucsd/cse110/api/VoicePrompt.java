package edu.ucsd.cse110.api;

import java.io.File;
import javax.sound.sampled.*;

public class VoicePrompt implements VoicePromptInterface {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Thread t;
    private String saveFile;

    public VoicePrompt(String sf) {
        // the number of samples of audio per second.
        float sampleRate = 44100;
        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;
        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;
        // whether the data is signed or unsigned.
        boolean signed = true;
        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        audioFormat = new AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian);
        
        saveFile = sf;
    }

    public void startRecording() {
        try {
            // the format of the TargetDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(
                    TargetDataLine.class,
                    audioFormat);
            // the TargetDataLine used to capture audio data from the microphone

            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

            targetDataLine.open(audioFormat);
            targetDataLine.start();
            
            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // the AudioInputStream that will be used to write the audio data to a file
                        AudioInputStream audioInputStream = new AudioInputStream(
                                targetDataLine);

                        // the file that will contain the audio data
                        File audioFile = new File(saveFile);
                        AudioSystem.write(
                                    audioInputStream,
                                    AudioFileFormat.Type.WAVE,
                                    audioFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            t.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	public File stopRecording() {
        targetDataLine.stop();
        targetDataLine.flush();
        targetDataLine.close();
		return new File(saveFile);
	}
}