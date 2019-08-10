package te.audio.m2sm.core.domain;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

public interface AudioDevice extends AutoCloseable {

    void connect() throws LineUnavailableException;

    String getName();

    default AudioFormat getFormat() {
        return new AudioFormat(
                8000.0f,
                16,
                1,
                true,
                true
        );
    }

}
