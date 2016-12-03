package te.mic_to_speaker;


import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Features:
 * - (v1) Command Line Interface
 * - (v2) Optional GUI
 */
public class Startup {

    private static AudioDeviceFinder audioDeviceFinder = new AudioDeviceFinder();

    public static void main(String... args) throws IOException {
        System.out.println("Welcome to MicToSpeaker!  Let's configure your microphone & speaker.\n");

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            pipeMicrophoneToSpeaker(
                    promptUserForMicrophone(console),
                    promptUserForSpeakers(console)
            );
        }
    }

    private static Mixer promptUserForMicrophone(BufferedReader console) {
        // Ask the user what microphone they want to use
        System.out.println("Microphones found on your system:");
        List<Mixer> allMicrophones = audioDeviceFinder.findMicrophones();

        // Print out all available microphones
        Map<Integer, Mixer> integerToMicrophoneMap = IntStream
                .range(0, allMicrophones.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, allMicrophones::get));

        integerToMicrophoneMap.forEach(printIntegerAndMixerName);

        Mixer microphone = promptUserForSelection(console, integerToMicrophoneMap);
        System.out.println("You selected: " + microphone.getMixerInfo().getName() + "\n");
        return microphone;
    }

    private static Mixer promptUserForSpeakers(BufferedReader console) {
        // Ask the user what speakers they want to use
        System.out.println("\nSpeakers found on your system:");
        List<Mixer> allSpeakers = audioDeviceFinder.findSpeakers();

        // Print out all available speakers
        Map<Integer, Mixer> intToSpeakerMap = IntStream
                .range(0, allSpeakers.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, allSpeakers::get));

        intToSpeakerMap.forEach(printIntegerAndMixerName);

        Mixer speakers = promptUserForSelection(console, intToSpeakerMap);
        System.out.println("You selected: " + speakers.getMixerInfo().getName() + "\n");
        return speakers;
    }

    private static BiConsumer<Integer, Mixer> printIntegerAndMixerName = (integer, mixer) -> {
        System.out.println("\t" + integer + ") " + mixer.getMixerInfo().getName());
    };

    private static <T> T promptUserForSelection(BufferedReader console, Map<Integer, T> options) {
        T userSelectedOption = null;

        System.out.print("Please select one from the list above.\n> ");
        while (userSelectedOption == null) {
            try {
                userSelectedOption = options.get(Integer.parseInt(console.readLine()));
            } catch (Exception ignored) {
            }

            if (userSelectedOption == null) {
                System.out.print("Invalid selection, please try again.\n> ");
            }
        }

        return userSelectedOption;
    }

    private static void pipeMicrophoneToSpeaker(Mixer microphone, Mixer speaker) {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        TargetDataLine microphoneConnection = null;
        SourceDataLine speakerConnection = null;

        try {
            // Connect to the microphone
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphoneConnection = (TargetDataLine) microphone.getLine(info);
            microphoneConnection.open(format);
            microphoneConnection.start();

            // Connect to the speaker
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakerConnection = (SourceDataLine) speaker.getLine(dataLineInfo);
            speakerConnection.open(format);
            speakerConnection.start();


            System.out.println("SUCCESS: Microphone is now outputting to speakers.");


            // Read from microphone connection and write to speaker connection indefinitely
            int numBytesRead;
            byte[] buffer = new byte[microphoneConnection.getBufferSize() / 5];
            while (true) {
                numBytesRead = microphoneConnection.read(buffer, 0, buffer.length);
                if (numBytesRead == -1) break;
                speakerConnection.write(buffer, 0, numBytesRead);
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (speakerConnection != null) {
                speakerConnection.drain();
                speakerConnection.close();
            }

            if (microphoneConnection != null) {
                microphoneConnection.close();
            }
        }
    }

}
