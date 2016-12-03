package te.mic_to_speaker;

import javax.sound.sampled.*;

class MicrophoneToSpeakerMapper {

    private final AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
    private TargetDataLine microphoneConnection = null;
    private SourceDataLine speakerConnection = null;
    private Mixer microphone = null;
    private Mixer speaker = null;

    MicrophoneToSpeakerMapper(Mixer microphone, Mixer speaker) {
        this.microphone = microphone;
        this.speaker = speaker;
    }

    void pipe() {
        try {
            openMicrophoneConnection();
            openSpeakerConnection();
            readFromMicrophoneAndWriteToSpeaker();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            closeSpeakerConnection();
            closeMicrophoneConnection();
        }
    }

    private void openMicrophoneConnection() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphoneConnection = (TargetDataLine) microphone.getLine(info);
        microphoneConnection.open(format);
        microphoneConnection.start();
    }

    private void openSpeakerConnection() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        speakerConnection = (SourceDataLine) speaker.getLine(dataLineInfo);
        speakerConnection.open(format);
        speakerConnection.start();
    }

    private void readFromMicrophoneAndWriteToSpeaker() {
        int numBytesRead;
        byte[] buffer = new byte[microphoneConnection.getBufferSize() / 5];

        System.out.println("SUCCESS: Microphone is now outputting to speakers.");
        while (true) {
            numBytesRead = microphoneConnection.read(buffer, 0, buffer.length);
            if (numBytesRead == -1) break;
            speakerConnection.write(buffer, 0, numBytesRead);
        }
    }

    private void closeSpeakerConnection() {
        if (speakerConnection != null) {
            speakerConnection.drain();
            speakerConnection.close();
        }
    }

    private void closeMicrophoneConnection() {
        if (microphoneConnection != null) {
            microphoneConnection.close();
        }
    }
}
