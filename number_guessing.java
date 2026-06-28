
import java.util.Scanner;
import java.util.Random;

public class number_guessing  {

    private int playerScore;
    private int levelNumber;
    private int roundNumber;
    private int totalLevelsInRound;
    private boolean isGameRunning;
    private Random numberGenerator;
    private Scanner userInput;

    public number_guessing() {
        this.playerScore = 0;
        this.levelNumber = 1;
        this.roundNumber = 1;
        this.totalLevelsInRound = 10;
        this.isGameRunning = true;
        this.numberGenerator = new Random();
        this.userInput = new Scanner(System.in);
    }

    public void startGameSession() {
        displayWelcomeMessage();
        while (isGameRunning) {
            playCurrentLevel();
        }
        displayFinalScore();
    }

    private void displayWelcomeMessage() {
        System.out.println("============================================");
        System.out.println("    MAZE NUMBER GUESSING GAME");
        System.out.println("============================================");
        System.out.println("Welcome! Guess the number to move the 'P'");
        System.out.println("Reach the end 'E' to finish the round!");
        System.out.println("============================================\n");
    }

    private void playCurrentLevel() {
        System.out.println("\n----------------------------------------");
        System.out.println("Round " + roundNumber + " | Level " + levelNumber +
                " of " + totalLevelsInRound);
        System.out.println("----------------------------------------");

        LevelDetails levelInfo = getLevelDetails();

        System.out.println("Range: 1 to " + levelInfo.maxRange);
        System.out.println("Chances: " + levelInfo.chancesAvailable);
        System.out.println("Current Score: " + playerScore + "\n");

        int chancesLeft = levelInfo.chancesAvailable;
        int secretNumber = numberGenerator.nextInt(levelInfo.maxRange) + 1;


        long levelStartTime = System.currentTimeMillis();
        int timeLimitSeconds = 30;

        boolean hasWon = false;

        while (chancesLeft > 0 && isGameRunning) {

            long secondsElapsed = (System.currentTimeMillis() - levelStartTime) / 1000;
            long secondsRemaining = timeLimitSeconds - secondsElapsed;


            if (levelInfo.hasTimeLimit && secondsRemaining <= 0) {
                System.out.println("\n[!] TIME EXPIRED! (30s limit reached)");
                penaltyAndRetry();
                return;
            }

            displayMazeProgress();

            System.out.print("Guess (1-" + levelInfo.maxRange + ")");
            if (levelInfo.hasTimeLimit) {
                System.out.print(" [TIME LEFT: " + secondsRemaining + "s]");
            }
            System.out.print(" | Chances: " + chancesLeft + " | Enter number (q to quit): ");

            String rawInput = userInput.nextLine().trim();


            long timeAfterInput = (System.currentTimeMillis() - levelStartTime) / 1000;
            if (levelInfo.hasTimeLimit && timeAfterInput >= timeLimitSeconds) {
                System.out.println("\n[!] TOO SLOW! You took " + timeAfterInput + " seconds.");
                penaltyAndRetry();
                return;
            }

            if (rawInput.equalsIgnoreCase("q")) {
                isGameRunning = false;
                return;
            }

            try {
                int guess = Integer.parseInt(rawInput);
                if (guess < 1 || guess > levelInfo.maxRange) {
                    System.out.println("Invalid! Stay within range.");
                    continue;
                }

                if (guess == secretNumber) {
                    hasWon = true;
                    break;
                } else {
                    chancesLeft--;
                    int gap = Math.abs(guess - secretNumber);
                    System.out.println(getHint(guess, secretNumber, gap));
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        if (hasWon) {
            playerScore += 10;
            System.out.println("\nCORRECT! +10 points.");
            levelNumber++;
            if (levelNumber > totalLevelsInRound) {
                moveToNextRound();
            }
        } else {
            System.out.println("\nOut of chances! The number was: " + secretNumber);
            penaltyAndRetry();
        }
    }

    private void displayMazeProgress() {
        int mazeLength = 15;
        double progress = (double)(levelNumber - 1) / totalLevelsInRound;
        int pIndex = (int)(progress * mazeLength);

        System.out.println("\n+-------------------+");
        System.out.print("| S ");
        for (int i = 0; i < mazeLength; i++) {
            if (i == pIndex) System.out.print("P");
            else System.out.print(".");
        }
        System.out.println(" E |");
        System.out.println("+-------------------+");
    }

    private void penaltyAndRetry() {
        playerScore = Math.max(0, playerScore - 5);
        System.out.println("PENALTY: -5 | Score: " + playerScore);
        System.out.println("Retrying Level " + levelNumber + "...");
        try { Thread.sleep(2000); } catch (Exception e) {}
        playCurrentLevel();
    }

    private void moveToNextRound() {
        System.out.println("\nROUND " + roundNumber + " COMPLETE!");
        roundNumber++;
        levelNumber = 1;
        totalLevelsInRound += 5;
        playerScore = 0;
        System.out.println("Moving to Round " + roundNumber + "...");
        try { Thread.sleep(3000); } catch (Exception e) {}
    }

    private String getHint(int guess, int secret, int gap) {
        if (guess < secret) return (gap <= 2) ? "Wrong! low" : "Wrong! too low";
        else return (gap <= 2) ? "Wrong! high" : "Wrong! too high";
    }

    private LevelDetails getLevelDetails() {
        int maxRange;
        int chances;
        boolean hasTimer = false;


        if (levelNumber == 1) chances = 3;
        else if (levelNumber == 2) chances = 5;
        else if (levelNumber == 3) chances = 7;
        else if (levelNumber == 4) chances = 9;
        else if (levelNumber == 5) chances = 12;
        else chances = 15;


        if (levelNumber <= 10) {

            maxRange = (levelNumber <= 5) ? levelNumber * 10 : 50 + (levelNumber - 5) * 10;
            hasTimer = false;
        } else {

            int extra = levelNumber - 10;
            maxRange = 100 + (extra * 10);
            hasTimer = true;
            chances = 15;
        }

        return new LevelDetails(maxRange, chances, hasTimer);
    }

    private void displayFinalScore() {
        System.out.println("\nGame Closed. Final Score: " + playerScore);
    }

    public static void main(String[] args) {
        new number_guessing().startGameSession();
    }
}

class LevelDetails {
    int maxRange, chancesAvailable;
    boolean hasTimeLimit;
    public LevelDetails(int r, int c, boolean t) {
        this.maxRange = r; this.chancesAvailable = c; this.hasTimeLimit = t;
    }
}
