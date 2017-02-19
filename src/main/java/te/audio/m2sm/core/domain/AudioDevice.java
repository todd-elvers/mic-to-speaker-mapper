package te.audio.m2sm.core.domain;

import javax.sound.sampled.*;

public interface AudioDevice extends AutoCloseable {

    AudioFormat FORMAT = new AudioFormat(8000.0f, 16, 1, true, true);

    void connect() throws LineUnavailableException;

    String getName();
}
