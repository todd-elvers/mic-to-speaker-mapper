package te.audio.m2sm.domain;

import javax.sound.sampled.*;

public class Speaker implements AudioDevice, AutoCloseable {
    private SourceDataLine speakerConnection = null;
    private Mixer mixer;

    public Speaker(Mixer mixer) {
        this.mixer = mixer;
    }

    public int write(byte[] b) {
        if(speakerConnection == null) {
            throw new IllegalStateException("Write failed: connection is null.");
        }

        return speakerConnection.write(b, 0, b.length);
    }

    public String getName() {
        return mixer.getMixerInfo().getName();
    }

    public void connect() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, FORMAT);
        speakerConnection = (SourceDataLine) mixer.getLine(dataLineInfo);
        speakerConnection.open(FORMAT);
        speakerConnection.start();
    }

    @Override
    public void close() {
        speakerConnection.drain();
        speakerConnection.close();
    }
}
