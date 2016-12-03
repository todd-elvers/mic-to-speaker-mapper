package te.mic_to_speaker;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Notes:
 * - Mixer capable of recording audio if target LineWavelet length != 0
 * - Mixer capable of audio playback if source LineWavelet length != 0
 */
public class AudioDeviceFinder {

    private static final Predicate<Mixer> onlyMixersThatSupportTargetDataLine = mixer -> {
        return Arrays.stream(mixer.getTargetLineInfo()).anyMatch(lineInfo -> {
            try {
                return mixer.getLine(lineInfo) instanceof TargetDataLine
                        && mixer.isLineSupported(lineInfo);
            } catch (LineUnavailableException e) {
                return false;
            }
        });
    };

    public List<Mixer> findMicrophones() {
        return Arrays
                .stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> mixer.getTargetLineInfo().length != 0)     // Can record
                .filter(mixer -> mixer.getSourceLineInfo().length == 0)     // Can not playback
                .filter(onlyMixersThatSupportTargetDataLine)
                .collect(Collectors.toList());

    }

    public List<Mixer> findSpeakers() {
        return Arrays
                .stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> mixer.getTargetLineInfo().length == 0)
                .filter(mixer -> mixer.getSourceLineInfo().length != 0)
                .collect(Collectors.toList());

    }

}
