package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> sortedSet = new TreeSet<>();
    private Set<String> wordSet = new HashSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Set<String> words = new HashSet<>();

        var scanner = new Scanner(dictionary);

        while (scanner.hasNext()) {
            String str = scanner.next();
            if(str.length() == wordLength) {
                words.add(str);
            }
        }
        wordSet = words;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Map<String, Set<String>> setMap = new HashMap<>();
        String newKey = null;
        sortedSet.add(guess);

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
        int count = 0;
        for (Set<String> entry : setMap.values()) {
            if (entry.size() > count) {
                count = entry.size();
            }
        }
        Set<String> potentialKeys = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : setMap.entrySet()) {
            if (entry.getValue().size() == count) {
                newKey = entry.getKey();
                potentialKeys.add(newKey);
            }
        }

        for (String s : potentialKeys) {
            if (s.indexOf(guess) == -1) {
                newKey = s;
            }
            Map<String, Integer> countValues = new HashMap<>();
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
