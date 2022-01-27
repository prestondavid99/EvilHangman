package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> sortedSet = new TreeSet<>();
    private Set<String> wordSet = new HashSet<>();
    private int wordLength;
    private StringBuilder currWord;




    private boolean correctGuess = false;


    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Set<String> words = new HashSet<>();
        this.wordLength = wordLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            sb.append('-');
        }
        currWord = sb;

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

    public boolean isCorrectGuess() {
        return correctGuess;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        correctGuess = false;
        Map<String, Set<String>> setMap = new HashMap<>();
        String newKey = null;
        guess = Character.toLowerCase(guess);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            sb.append('-');
        }


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
                if (newKey.equals(sb.toString())) {
                    System.out.println("Sorry! There are no " + guess + "'s");
                }
                for (int i = 0; i < newKey.length(); i++) {
                    if (newKey.charAt(i) != '-') {
                        currWord.setCharAt(i, newKey.charAt(i));
                    }
                }
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

        int potentialKeyCount = 0; // If there are letters at all
        for (String s : potentialKeys.keySet()) {
            if (potentialKeys.get(s) == minLetterCount) {
                potentialKeyCount++;
            }
        }

        if (potentialKeyCount == 1) { // This key has the least amount of letters
            for (String s : potentialKeys.keySet()) {
                if (potentialKeys.get(s) == minLetterCount) {
                    newKey = s;
                    if (newKey.equals(sb.toString())) {
                        System.out.println("Sorry! There are no " + guess + "'s");
                    }
                    else {
                        System.out.println("Nice! There is " + minLetterCount + " " + guess);
                        correctGuess = true;
                    }

                    for (int i = 0; i < newKey.length(); i++) {
                        if (newKey.charAt(i) != '-') {
                            currWord.setCharAt(i, newKey.charAt(i));
                        }
                    }

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

        if (newKey.equals(sb.toString())) {
            System.out.println("Sorry! There are no " + guess + "'s");
        }
        else {
            System.out.println("Nice! There is " + minLetterCount + " " + guess);
            correctGuess = true;
        }



        for (int i = 0; i < newKey.length(); i++) {
            if (newKey.charAt(i) != '-') {
                currWord.setCharAt(i, newKey.charAt(i));
            }
        }

        wordSet = setMap.get(newKey);
        return setMap.get(newKey);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return sortedSet;
    }

    public String getCorrectWord() {
        for (String s : wordSet) {
            return s;
        }
        return null;
    }

    public StringBuilder getCurrKey() {
        return currWord;
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
