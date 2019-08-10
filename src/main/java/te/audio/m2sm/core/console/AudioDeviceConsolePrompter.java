package te.audio.m2sm.core.console;

import te.audio.m2sm.core.AudioDeviceFinder;
import te.audio.m2sm.core.domain.AudioDevice;
import te.audio.m2sm.core.domain.Microphone;
import te.audio.m2sm.core.domain.Speaker;

import java.io.BufferedReader;
import java.util.List;

public class AudioDeviceConsolePrompter extends ConsolePrompter {

    public static Microphone promptForMicrophoneSelection(BufferedReader console) {
        List<Microphone> microphonesToChooseFrom = AudioDeviceFinder.findUsableMicrophones();

        System.out.println("Microphones found on your system:");
        return promptUser(
                console,
                microphonesToChooseFrom,
                AudioDevice::getName
        );
    }

    public static Speaker promptForSpeakerSelection(BufferedReader console) {
        List<Speaker> speakersToChooseFrom = AudioDeviceFinder.findUsableSpeakers();

        System.out.println("Speakers found on your system:");
        return promptUser(
                console,
                speakersToChooseFrom,
                AudioDevice::getName
        );
    }

}