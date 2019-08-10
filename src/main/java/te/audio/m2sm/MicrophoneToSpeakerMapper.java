package te.audio.m2sm;


import te.audio.m2sm.core.domain.Microphone;
import te.audio.m2sm.core.domain.Speaker;

import javax.sound.sampled.*;

class MicrophoneToSpeakerMapper {

    public void map(Microphone microphone, Speaker speaker) throws LineUnavailableException {
        speaker.connect();
        microphone.connect();

        readFromMicrophoneAndWriteToSpeaker(microphone, speaker);
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
