package te.audio.m2sm.domain;

import javax.sound.sampled.*;

public class Speaker implements AudioDevice {
    private SourceDataLine speakerConnection = null;
    private Mixer mixer;

    public Speaker(Mixer mixer) {
        this.mixer = mixer;
    }

    public int write(byte[] b) {
        if(speakerConnection == null) {
            throw new IllegalStateException("Cannot write to a null speaker connection");
        }

        return speakerConnection.write(b, 0, b.length);
    }

    @Override
    public Mixer getMixer() {
        return mixer;
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
