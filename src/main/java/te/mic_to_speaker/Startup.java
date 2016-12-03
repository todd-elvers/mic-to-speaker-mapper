package te.mic_to_speaker;


import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Features:
 * - (v1) Command Line Interface
 * - (v2) Optional GUI
 */
public class Startup {

    public static void main(String... args) throws IOException {
        System.out.println("Welcome to MicToSpeaker!  Let's configure your microphone & speaker.\n");

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            Mixer microphone = AudioDevicePrompter.promptUserForMicrophone(console);
            Mixer speaker = AudioDevicePrompter.promptUserForSpeakers(console);

            new MicrophoneToSpeakerMapper(microphone, speaker).pipe();
        }
    }

}
