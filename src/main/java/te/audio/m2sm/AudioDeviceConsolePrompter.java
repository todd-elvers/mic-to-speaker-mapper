package te.audio.m2sm;

import te.audio.m2sm.domain.AudioDevice;
import te.audio.m2sm.domain.Microphone;
import te.audio.m2sm.domain.Speaker;

import java.io.BufferedReader;
import java.util.List;

class AudioDeviceConsolePrompter extends ConsolePrompter {

    static Microphone promptForMicrophoneSelection(BufferedReader console) {
        List<Microphone> microphonesToChooseFrom = AudioDeviceFinder.findUsableMicrophones();

        System.out.println("Microphones found on your system:");
        return promptUser(
                console,
                microphonesToChooseFrom,
                AudioDevice::getName
        );
    }

    static Speaker promptForSpeakerSelection(BufferedReader console) {
        List<Speaker> speakersToChooseFrom = AudioDeviceFinder.findUsableSpeakers();

        System.out.println("Speakers found on your system:");
        return promptUser(
                console,
                speakersToChooseFrom,
                AudioDevice::getName
        );
    }

}