package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Character.isLetter;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        var game = new EvilHangmanGame();
        File dictionary = new File(args[0]);
        int guesses = Integer.parseInt(args[2]);
        int wordCount = Integer.parseInt(args[1]);
        boolean won = false;

        game.startGame(dictionary, wordCount);

        for (int i = 0; i < guesses; i++) {
            if (won) {
               break;
            }
            System.out.println("You have " + (guesses - i) + " guesses left");
            System.out.print("Used letters: ");
            for (char c : game.getGuessedLetters()) {
                System.out.print(c + " ");
            }

            System.out.println("");
            System.out.print("Word: ");
            if(game.getCurrKey() == null) {
                for (int k = 0; k < wordCount; k++) {
                    if (k == wordCount - 1) {
                        System.out.println("-");
                    } else {
                        System.out.print("-");
                    }
                }
            }
            else {
                System.out.println(game.getCurrKey());
            }



            boolean valid = false;
            while (!valid) {
                System.out.println("Enter guess: ");
                Scanner sc = new Scanner(System.in);
                String input = sc.nextLine();
                input = input.toLowerCase(Locale.ROOT);
                if (input.length() != 1 || !isLetter(input.charAt(0))) {
                    System.out.println("Invalid Input");
                }
                else if (game.getGuessedLetters().contains(input.charAt(0))) {
                    System.out.println("You already guessed that!");
                }
                else {
                    game.makeGuess(input.charAt(0));
                    if(game.isCorrectGuess()) {
                        guesses++;
                        int correctLetter = 0;
                        for (int j = 0; j < game.getCurrKey().length(); j++) {
                            if (game.getCurrKey().charAt(j) != '-') {
                                correctLetter++;
                            }
                        }
                        if (correctLetter == game.getCurrKey().length()) {
                            System.out.println("Congrats! You correctly guessed the word: " + game.getCurrKey());
                            won = true;
                        }
                    }
                    valid = true;
                }
            }
        }
        if(!won) {
            System.out.println("Sorry, you lost! The correct word was: " + game.getCorrectWord());
        }



    }


}


