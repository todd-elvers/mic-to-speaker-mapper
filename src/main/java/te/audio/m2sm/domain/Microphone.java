package te.audio.m2sm.domain;

import javax.sound.sampled.*;

public class Microphone implements AudioDevice {
    private TargetDataLine connection = null;
    private Mixer mixer;

    public Microphone(Mixer mixer) {
        this.mixer = mixer;
    }

    public int read(byte[] b) {
        if(connection == null) {
            throw new IllegalStateException("Cannot read to a null microphone connection");
        }

        return connection.read(b, 0, b.length);
    }

    @Override
    public Mixer getMixer() {
        return mixer;
    }

    public byte[] getByteBuffer() {
        return new byte[connection.getBufferSize() / 5];
    }

    public void connect() throws LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, FORMAT);
        connection = (TargetDataLine) mixer.getLine(dataLineInfo);
        connection.open(FORMAT);
        connection.start();
    }

    @Override
    public void close() {
        connection.close();
    }
}
