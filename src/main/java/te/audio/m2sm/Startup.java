package te.audio.m2sm;


import te.audio.m2sm.core.console.AudioDeviceConsolePrompter;
import te.audio.m2sm.core.domain.Microphone;
import te.audio.m2sm.core.domain.Speaker;

import javax.sound.sampled.LineUnavailableException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Features:
 * - (v1) Command Line Interface
 * - (v2) Optional GUI
 */
public class Startup {

    public static void main(String... args) throws IOException, LineUnavailableException {
        System.out.println("Welcome to MicToSpeakerMapper!  Let's configure your microphone & speaker.\n");

        try (
                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                Microphone microphone = AudioDeviceConsolePrompter.promptForMicrophoneSelection(console);
                Speaker speaker = AudioDeviceConsolePrompter.promptForSpeakerSelection(console)
        ) {
            new MicrophoneToSpeakerMapper().map(microphone, speaker);
        }
    }

}
