package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;



public class EvilHangmanGame implements IEvilHangmanGame {
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Set<String> stringSet = null;

        var scanner = new Scanner(dictionary);

        while (scanner.hasNext()) {
            String str = scanner.next();
            stringSet.add(str);
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
