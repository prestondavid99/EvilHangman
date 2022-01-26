package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Character.isLetter;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        var game = new EvilHangmanGame();
        Iterator<Character> itr = game.getGuessedLetters().iterator();
        File dictionary = new File(args[0]);
        int guesses = Integer.parseInt(args[2]);
        int wordCount = Integer.parseInt(args[1]);

        game.startGame(dictionary, wordCount);

        for (int i = 0; i < guesses; i++) {
            System.out.println("You have " + (guesses - i) + " guesses left");
            System.out.print("Used letters: ");
            for (char c : game.getGuessedLetters()) {
                System.out.print(c + " ");
            }

            System.out.println("");
            System.out.print("Word: ");
            for (int k = 0; k < wordCount; k++) {
                if (k == wordCount - 1) {
                    System.out.println("-");
                } else {
                    System.out.print("-");
                }
            }

            System.out.println("Enter guess: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (!isLetter(input.charAt(0)) || input.length() != 1) {
                System.out.println("Invalid Input");
            }
            else {
                game.makeGuess(input.charAt(0));
            }


        }


    }


}


