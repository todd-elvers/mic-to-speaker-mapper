package te.audio.m2sm.core.domain;

import javax.sound.sampled.*;

public class Speaker implements AudioDevice, AutoCloseable {
    private SourceDataLine connection = null;
    private Mixer mixer;

    public Speaker(Mixer mixer) {
        this.mixer = mixer;
    }

    public int write(byte[] bytes) {
        if(connection == null) {
            throw new IllegalStateException("Cannot write bytes to a null connection.");
        }

        return connection.write(bytes, 0, bytes.length);
    }

    public String getName() {
        return mixer.getMixerInfo().getName();
    }

    public void connect() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getFormat());
        connection = (SourceDataLine) mixer.getLine(dataLineInfo);
        connection.open(getFormat());
        connection.start();
    }

    @Override
    public void close() {
        connection.drain();
        connection.close();
    }
}
