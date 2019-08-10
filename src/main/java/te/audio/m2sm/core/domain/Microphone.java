package te.audio.m2sm.core.domain;

import javax.sound.sampled.*;

public class Microphone implements AudioDevice, AutoCloseable {
    private TargetDataLine connection = null;
    private Mixer mixer;

    public Microphone(Mixer mixer) {
        this.mixer = mixer;
    }

    public int read(byte[] bytes) {
        if(connection == null) {
            throw new IllegalStateException("Cannot read bytes from a null connection.");
        }

        return connection.read(bytes, 0, bytes.length);
    }

    @Override
    public String getName() {
        return mixer.getMixerInfo().getName();
    }

    public byte[] getByteBuffer() {
        return new byte[connection.getBufferSize() / 5];
    }

    @Override
    public void connect() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getFormat());
        connection = (TargetDataLine) mixer.getLine(dataLineInfo);
        connection.open(getFormat());
        connection.start();
    }

    @Override
    public void close() {
        connection.close();
    }
}
