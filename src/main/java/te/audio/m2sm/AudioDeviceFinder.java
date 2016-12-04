package te.audio.m2sm;

import te.audio.m2sm.domain.Microphone;
import te.audio.m2sm.domain.Speaker;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class AudioDeviceFinder {

    public static List<Microphone> findUsableMicrophones() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> mixer.getTargetLineInfo().length != 0)     // Can record
                .filter(mixer -> mixer.getSourceLineInfo().length == 0)     // Can not playback
                .filter(onlyMixersThatSupportTargetDataLine)
                .map(Microphone::new)
                .collect(Collectors.toList());
    }

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

    public static List<Speaker> findUsableSpeakers() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(mixer -> mixer.getTargetLineInfo().length == 0)    // Cannot record
                .filter(mixer -> mixer.getSourceLineInfo().length != 0)    // Can playback
                .map(Speaker::new)
                .collect(Collectors.toList());
    }

}
