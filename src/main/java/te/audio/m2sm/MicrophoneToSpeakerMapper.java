package te.audio.m2sm;

import te.audio.m2sm.domain.Microphone;
import te.audio.m2sm.domain.Speaker;

import javax.sound.sampled.*;

class MicrophoneToSpeakerMapper {

    void map(Microphone microphone, Speaker speaker) {
        try {
            speaker.connect();
            microphone.connect();

            readFromMicrophoneAndWriteToSpeaker(microphone, speaker);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            speaker.close();
            microphone.close();
        }
    }

    private void readFromMicrophoneAndWriteToSpeaker(Microphone microphone, Speaker speaker) {
        System.out.println("SUCCESS: Microphone output now mapped to speakers.");
        System.out.println("Terminate the program to end this mapping.");

        int numBytesRead;
        byte[] buffer = microphone.getByteBuffer();

        while (true) {
            numBytesRead = microphone.read(buffer);
            if (numBytesRead == -1) break;
            speaker.write(buffer);
        }
    }

}
