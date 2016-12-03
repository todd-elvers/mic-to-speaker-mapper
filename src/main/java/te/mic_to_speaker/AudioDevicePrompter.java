package te.mic_to_speaker;

import javax.sound.sampled.Mixer;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AudioDevicePrompter {

    private static BiConsumer<Integer, Mixer> printIntegerAndMixerName = (integer, mixer) -> {
        System.out.println("\t" + integer + ") " + mixer.getMixerInfo().getName());
    };

    static Mixer promptUserForMicrophone(BufferedReader console) {
        System.out.println("Microphones found on your system:");

        // Build map of option-number to microphone (for user input)
        List<Mixer> allMicrophones = AudioDeviceFinder.findMicrophones();
        Map<Integer, Mixer> integerToMicrophoneMap = IntStream
                .range(0, allMicrophones.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, allMicrophones::get));

        integerToMicrophoneMap.forEach(printIntegerAndMixerName);

        return promptUserForSelection(console, integerToMicrophoneMap);
    }

    static Mixer promptUserForSpeakers(BufferedReader console) {
        System.out.println("Speakers found on your system:");

        // Build map of option-number to speaker (for user input)
        List<Mixer> allSpeakers = AudioDeviceFinder.findSpeakers();
        Map<Integer, Mixer> intToSpeakerMap = IntStream
                .range(0, allSpeakers.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, allSpeakers::get));

        intToSpeakerMap.forEach(printIntegerAndMixerName);

        return promptUserForSelection(console, intToSpeakerMap);
    }

    private static Mixer promptUserForSelection(BufferedReader console, Map<Integer, Mixer> options) {
        Mixer userSelectedOption = null;

        System.out.print("Please select one from the list above.\n> ");
        while (userSelectedOption == null) {
            try {
                userSelectedOption = options.get(Integer.parseInt(console.readLine()));
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

            if (userSelectedOption == null) {
                System.out.print("Invalid selection, please try again.\n> ");
            }
        }

        System.out.println("\nYou selected: " + userSelectedOption.getMixerInfo().getName() + "\n");

        return userSelectedOption;
    }

}
