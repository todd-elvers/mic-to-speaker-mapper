package te.audio.m2sm.core;

import te.audio.m2sm.core.domain.Microphone;
import te.audio.m2sm.core.domain.Speaker;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AudioDeviceFinder {

    public static List<Microphone> findUsableMicrophones() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(AudioDeviceFinder::isMicrophone)
                .map(Microphone::new)
                .collect(Collectors.toList());
    }

    private static boolean isMicrophone(Mixer mixer) {
        return mixer.getTargetLineInfo().length != 0        //  Can record
                && mixer.getSourceLineInfo().length == 0    //  Cannot playback
                && supportsTargetDataLine.test(mixer);
    }

    private static final Predicate<Mixer> supportsTargetDataLine = mixer -> Arrays
            .stream(mixer.getTargetLineInfo())
            .anyMatch(lineInfo -> {
                try {
                    return mixer.getLine(lineInfo) instanceof TargetDataLine && mixer.isLineSupported(lineInfo);
                } catch (LineUnavailableException e) {
                    return false;
                }
            });

    public static List<Speaker> findUsableSpeakers() {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .map(AudioSystem::getMixer)
                .filter(AudioDeviceFinder::isSpeaker)
                .map(Speaker::new)
                .collect(Collectors.toList());
    }

    private static boolean isSpeaker(Mixer mixer) {
        return mixer.getTargetLineInfo().length == 0        //  Cannot record
                && mixer.getSourceLineInfo().length != 0;   //  Can playback
    }
}
