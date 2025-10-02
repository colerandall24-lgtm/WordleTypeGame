import java.io.FileNotFoundException;
import java.util.List;

public class BotBatchTester {
    public static void main(String[] args) throws FileNotFoundException {
        Wordle baseGame = new Wordle(); // Used only to load the word list
        List<String> wordsToTest = baseGame.getWordsToPickFrom(); // Or use dic1.txt if that’s what you want
        int maxGuesses = 6;

        int totalGames = 0;
        int totalGuesses = 0;
        int failedGames = 0;

        for (String targetWord : wordsToTest) {
            Wordle game = new Wordle(targetWord);
            WordleBot bot = new WordleBot(game, "dic.txt");

            boolean won = false;
            int guesses = 0;
            String guess = "";

            while (!game.gameOver()) {
                if (guesses == 0) {
                    guess = bot.generateWord(null, null);
                } else {
                    int[] pattern = game.patternFor(guess);
                    guess = bot.generateWord(pattern, guess);
                }
                game.recordGuess(guess);
                guesses++;

                if (guess.equalsIgnoreCase(targetWord)) {
                    won = true;
                    break;
                }
            }

            totalGames++;
            if (won) {
                totalGuesses += guesses;
                //System.out.println("✓ Word: " + targetWord + " - Guesses: " + guesses);
            } else {
                failedGames++;
                System.out.println("✗ Word: " + targetWord + " - FAILED" + " Guesses: " + guesses);
            }
        }

        System.out.println("\n===== SUMMARY =====");
        System.out.println("Total words tested: " + totalGames);
        System.out.println("Games won: " + (totalGames - failedGames));
        System.out.println("Games failed: " + failedGames);
        if (totalGames - failedGames > 0) {
            System.out.printf("Average guesses per win: %.2f%n", (double) totalGuesses / (totalGames - failedGames));
        }
    }
}
