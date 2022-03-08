import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class WordleLogic {

  // Toggle DEBUG MODE On/Off
  public static final boolean DEBUG_MODE = true;
  // Toggle WARM_UP On/Off
  public static final boolean WARM_UP = false;

  private static final String FILENAME = "englishWords5.txt";
  // Number of words in the words.txt file
  private static final int WORDS_IN_FILE = 5758; // Review BJP 6.1 for

  // Use for generating random numbers!
  private static final Random rand = new Random();

  public static final int MAX_ATTEMPTS = 6; // max number of attempts
  public static final int WORD_LENGTH = 5; // WORD_LENTGH-letter word
  // as is 5 like wordle, could be changed

  private static final char EMPTY_CHAR = WordleView.EMPTY_CHAR; // use to delete char

  // ************ Color Values ************

  // Green (right letter in the right place)
  private static final Color CORRECT_COLOR = new Color(53, 209, 42);
  // Yellow (right letter in the wrong place)
  private static final Color WRONG_PLACE_COLOR = new Color(235, 216, 52);
  // Dark Gray (letter doesn't exist in the word)
  private static final Color WRONG_COLOR = Color.DARK_GRAY;
  // Light Gray (default keyboard key color, letter hasn't been checked yet)
  private static final Color NOT_CHECKED_COLOR = new Color(160, 163, 168);

  private static final Color DEFAULT_BGCOLOR = Color.BLACK;

  // ***************************************************

  // ************ Class variables ************

  // Add them as necessary (I have some but less than 5)
  private static int row = 0;
  private static int col = 0;
  private static String secret;
  private static char[] input = new char[WORD_LENGTH];
  private static String[] wordList = new String[WORDS_IN_FILE - 1];
  // ***************************************************

  // ************ Class methods ************

  // There are 6 already defined below, with 5 of them to be completed.
  // Add class helper methods as necessary. Our solution has 12 of them total.

  // Complete for 3.1.1
  public static void warmUp() {
    System.out.println("test");
    WordleView.setCellLetter(0, 0, 'C');
    WordleView.setCellLetter(1, 2, 'O');
    WordleView.setCellLetter(5, 4, 'C');
    WordleView.setCellLetter(3, 3, 'S');

    WordleView.setCellColor(0, 0, CORRECT_COLOR);
    WordleView.setCellColor(1, 2, WRONG_COLOR);
    WordleView.setCellColor(5, 4, WRONG_PLACE_COLOR);

  }

  // This function gets called ONCE when the game is very first launched
  // before the player has the opportunity to do anything.
  //
  // Returns the chosen mystery word the user needs to guess
  public static String init() throws FileNotFoundException {
    File file = new File(FILENAME);
    Scanner scan = new Scanner(file);
    int randomnum = rand.nextInt(WORDS_IN_FILE);
    for (int k = 0; k < WORDS_IN_FILE - 1; k++) {
      wordList[k] = scan.next().toUpperCase();
    }
    secret = wordList[randomnum];
    // secret = "DELAY";
    return secret;
  }

  // This function gets called everytime the user inputs 'Backspace'
  // pressing the physical or virtual keyboard.
  // call on Backspace input
  public static void deleteLetter() {

    if (DEBUG_MODE) {
      System.out.println("in deleteLetter()");

    }
    if (col > 0) {
      WordleView.setCellLetter(row, col - 1, EMPTY_CHAR);
      System.out.println(col);
      input[col - 1] = EMPTY_CHAR;
      col--;
    }
    // WordleView.setCellLetter(row,col,'D');

  }

  public static boolean isWord(String in) {
    for (int i = 0; i < wordList.length; i++) {
      if (in.equals(wordList[i].toUpperCase())) {
        return true;
      }
    }
    return false;
  }

  // This function gets called everytime the player inputs 'Enter'
  // pressing the physical or virtual keyboard.
  public static void checkLetters() {
    if (DEBUG_MODE) {
      System.out.println("in checkLetters()");
    }
    // Variables
    String scbase = secret;
    char[] tempinpt = input.clone();
    String inptStr = new String(input);
    char[] answer = secret.toCharArray();
    int counter = 0;

    if (col < 5 || isWord(inptStr) == false)
      WordleView.wiggleRow(row);
    if (isWord(inptStr)) {
      for (int k = 0; k < 5; k++) { // Sets everything to default wrong
        WordleView.setCellColor(row, k, WRONG_COLOR);
        updateKb(input[k], 2);
      }
      for (int i = 0; i < 5; i++) { // checks for correct word
        if (input[i] == answer[i]) {
          WordleView.setCellColor(row, i, CORRECT_COLOR);
          updateKb(input[i], 0);
          tempinpt[i] = '\0';
          counter++;
          System.out.println(counter);
          if (counter == 5) {
            WordleView.gameOver(true);
          }
          dupeCheck(input[i], i); // deletes characters out of secret to fix duplicates
          System.out.println(secret);
        }
      }
      for (int j = 0; j < tempinpt.length; j++) {
        // converting to string to use indexOf()
        if (secret.indexOf(Character.toString(tempinpt[j])) != -1) {
          WordleView.setCellColor(row, j, WRONG_PLACE_COLOR);
          updateKb(tempinpt[j], 1);
          dupeCheck(tempinpt[j], j);
        }
      }
      row += 1;
      col = 0;
    }
    if (row == MAX_ATTEMPTS && counter != 5) {
      WordleView.gameOver(false);
    }
    secret = scbase;
    input = new char[WORD_LENGTH];
  }

  public static void updateKb(char key, int color) {
    if (color == 0) { // in the case that letter is correct/green
      WordleView.setKeyboardColor(key, CORRECT_COLOR);
    }
    if (color == 1 && WordleView.getKeyboardColor(key) != CORRECT_COLOR) { // If letter is yellow
      WordleView.setKeyboardColor(key, WRONG_PLACE_COLOR);
    }
    if (color == 2) {
      WordleView.setKeyboardColor(key, WRONG_COLOR);
    }
  }

  public static void dupeCheck(char key, int i) {
    if (key == secret.charAt(i)) {
      char[] temp = secret.toCharArray();
      temp[i] = ' ';
      secret = String.valueOf(temp);
    } else {
      char[] temp = secret.toCharArray();
      temp[secret.indexOf(String.valueOf(key))] = ' ';
      secret = String.valueOf(temp);
    }

  }

  // This function gets called everytime the player types a valid letter
  // on the keyboard or clicks one of the letter keys on the
  // graphical keyboard interface.
  // The key pressed is passed in as a char uppercase for letter
  public static void inputLetter(char key) {

    // Some placeholder debugging code...

    System.out.println("Letter pressed!: " + key);
    if (col < 5) {
      WordleView.setCellLetter(row, col, key);
      input[col] = key;
      col += 1;
    }

    if (WARM_UP) {

      System.out.println("A row should wiggle");

      // if (key == 'W')
      // WordleView.wiggleRow(3);

    }

  }

  // Initializes and launches the game logic and its GUI window
  public static void main(String[] args) throws FileNotFoundException {
    // String secret = null;

    if (!WARM_UP) {
      // Calls to intialize the game logic and pick the secret word
      init();
    }

    // Creates the game window
    WordleView.create(secret);

    if (WARM_UP) {
      warmUp();
    }
  }
}