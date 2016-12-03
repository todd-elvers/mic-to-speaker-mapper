package te.audio.m2sm;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class ConsolePrompter {

    /**
     * Prompts a user with a numbered list of options, returning the first valid selection they make.
     * <p>This blocks & retries indefinitely until the user either provides valid input or terminates the program.
     *
     * @param console a connection to the console
     * @param options the options to present to the user
     * @param optionToString the function that converts an option of type T to a string for the user to read
     * @param <T> the class of the option the user is deciding between
     * @return the selection the user made from the list of options provided
     */
    static <T> T promptUser(BufferedReader console, List<T> options, Function<T, String> optionToString) {
        Map<Integer, T> intToOptionMap = toMapForUserSelection(options);

        intToOptionMap.forEach((integer, option) ->
                System.out.println("\t" + integer + ") " + optionToString.apply(option))
        );

        return promptUserForSelection(console, intToOptionMap, optionToString);
    }

    /**
     * Given some list of options to present to the user on the command line, this
     * generates a map of integer->option so they can be displayed to the user and
     * the user can use the integer to indicate their selection.
     *
     * @param options the options the user will need to select from
     * @param <T>     whatever type the option is
     * @return a map of integer->T (integer starts at 1 and increments)
     */
    protected static <T> Map<Integer, T> toMapForUserSelection(List<T> options) {
        return IntStream.range(0, options.size())
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, options::get));
    }

    /**
     * Prompts a user indefinitely until they input an integer that maps to a non-null
     * value in the provided map.
     *
     * @param console a connection to the console
     * @param options the map of integer->option we want the user to make a selection from
     * @param optionToString the function that, when called on our option, returns a String for the user to read
     * @param <T> the class of the option the user is deciding between
     * @return the selection the user made from the map of options
     */
    protected static <T> T promptUserForSelection(BufferedReader console, Map<Integer, T> options, Function<T, String> optionToString) {
        T userSelectedOption = null;

        System.out.print("Please select one from the list above.\n> ");
        while (userSelectedOption == null) {
            try {
                userSelectedOption = options.get(Integer.parseInt(console.readLine().trim()));
            } catch (Exception ignored) {
            }

            if (userSelectedOption == null) {
                System.out.print("Invalid selection, please try again.\n> ");
            }
        }
        System.out.println("You selected: " + optionToString.apply(userSelectedOption) + "\n");

        return userSelectedOption;
    }

}
