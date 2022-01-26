package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> sortedSet = new TreeSet<>();
    private Set<String> wordSet = new HashSet<>();
    private int wordLength;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Set<String> words = new HashSet<>();
        this.wordLength = wordLength;

        try (var scanner = new Scanner(dictionary)) {
            while (scanner.hasNext()) {
                String str = scanner.next();
                if (str.length() == wordLength) {
                    words.add(str);
                }
            }
            wordSet = words;
            if (wordSet.size() == 0) {
                throw new EmptyDictionaryException();
            }
        }

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Map<String, Set<String>> setMap = new HashMap<>();
        String newKey = null;
        guess = Character.toLowerCase(guess);
        if (sortedSet.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        sortedSet.add(guess);

        /* Set Making */
        for (String s : wordSet) {
            Set<String> subset = new HashSet<>();
            String key = getSubsetKey(s, guess);
            if (setMap.get(key) == null) {
                subset.add(s);
                setMap.put(key, subset);
            } else {
                subset = setMap.get(key);
                subset.add(s);
            }
        }

        /* Finding Set With Most Values */
        int count = 0;
        for (Set<String> entry : setMap.values()) {
            if (entry.size() > count) {
                count = entry.size();
            }
        }
        Map<String, Integer> potentialKeys = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : setMap.entrySet()) {
            if (entry.getValue().size() == count) {
                newKey = entry.getKey();
                potentialKeys.put(newKey, 0);
            }
        }

        /* Tie Breakers */
        for (String s : potentialKeys.keySet()) {
            if (s.indexOf(guess) == -1) { // If the character is not found in the key
                newKey = s;
                wordSet = setMap.get(newKey);
                return setMap.get(newKey);
            }

            else { // Attach letter amount to each String s
                int letterCount = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == guess) {
                        letterCount++;
                    }
                }
                potentialKeys.put(s, letterCount);
            }
        }

        int minLetterCount = wordLength;
        for (String s : potentialKeys.keySet()) {
            if (potentialKeys.get(s) < minLetterCount) {
                minLetterCount = potentialKeys.get(s);
            }
        }

        int potentialKeyCount = 0;
        for (String s : potentialKeys.keySet()) {
            if (potentialKeys.get(s) == minLetterCount) {
                potentialKeyCount++;
            }
        }

        if (potentialKeyCount == 1) { // This key has the least amount of letters
            for (String s : potentialKeys.keySet()) {
                if (potentialKeys.get(s) == minLetterCount) {
                    newKey = s;
                    wordSet = setMap.get(newKey);
                    return setMap.get(newKey);
                }
            }
        }

        else {
            for (String s : potentialKeys.keySet()) {
                int rightCounter = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == guess) {
                        rightCounter += i;
                    }
                }
                potentialKeys.put(s, rightCounter);
            }

        }
        int maxWeight = 0;
        for (String s : potentialKeys.keySet()) {
            if (potentialKeys.get(s) > maxWeight) {
                maxWeight = potentialKeys.get(s);
            }
        }

        for (String s : potentialKeys.keySet()) {
            if (potentialKeys.get(s) == maxWeight) {
                newKey = s;
            }
        }
        wordSet = setMap.get(newKey);
        return setMap.get(newKey);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return sortedSet;
    }

    public String getSubsetKey(String s, char guess) {
        var sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == guess) {
                sb.append(s.charAt(i));
            }
            else {
                sb.append("-");
            }
        }
        return sb.toString();
    }




}
