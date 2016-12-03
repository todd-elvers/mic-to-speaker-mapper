package te.audio.m2sm.domain;

import javax.sound.sampled.*;

public interface AudioDevice {

    AudioFormat FORMAT = new AudioFormat(8000.0f, 16, 1, true, true);

    Mixer getMixer();

    void connect() throws LineUnavailableException;

    void close();

    default String getName() {
        return getMixer().getMixerInfo().getName();
    }
}
