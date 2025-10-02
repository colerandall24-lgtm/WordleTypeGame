import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Wordle {
    private static final int GREEN = 2;
    private static final int YELLOW = 1;
    private static final int GRAY = 0;
    private int size;
    private String word;
    private boolean gameOver;
    private List<String> dictionary;
    private int[][] board;
    private List<String> wordsToPickFrom;

    public Wordle() throws FileNotFoundException {
        this.size = 0;
        board = new int[6][5];
        gameOver = false;
        dictionary = loadFile("dic.txt");
        wordsToPickFrom = loadFile("dic1.txt");
        this.word = wordsToPickFrom.get((int)(Math.random() * wordsToPickFrom.size()));
    }

    public Wordle(int numGuesses) throws FileNotFoundException {
        this.size = 0;
        board = new int[numGuesses][5];
        gameOver = false;
        dictionary = loadFile("dic.txt");
        wordsToPickFrom = loadFile("dic1.txt");
        this.word = wordsToPickFrom.get((int)(Math.random() * wordsToPickFrom.size()));
    }

    public Wordle(String word) throws FileNotFoundException {
        this();
        this.word = word;
    }

    public int getNumGuesses(){
        return board.length;
    }

    public List<String> getDictionary(){
        return dictionary;
    }



    public boolean won(int row){
        int num = 0;
        for(int i : board[row]){
            num += i;
        }
        return num == word.length() * 2;
    }

    public int[] recordGuess(String guess){
        if(!checkGuess(guess)){
            throw new IllegalArgumentException("Word not in dictionary");
        }
        int[] pattern = patternFor(guess);
        if (size < board.length) {
            board[size] = pattern;
            size++;
            if (won(size-1)) {
                gameOver = true;
            }
            if (size == board.length) gameOver = true;
            return pattern;
        }
        return pattern;
    }

    public boolean checkGuess(String guess){
        return dictionary.contains(guess);
    }

    public boolean gameOver(){
        return gameOver;
    }

    public List<String> getWordsToPickFrom(){
        return wordsToPickFrom;
    }

    public int[] patternFor(String guess) {
        if (guess.length() != word.length()) {
            throw new IllegalArgumentException("Word must be 5 letters");
        }
        Map<Integer, String> guessMap = new HashMap<>();
        List<Integer> colors = new ArrayList<>();
        String correct = this.word;
        for (int i = 0; i < this.word.length(); i++) {
            guessMap.put(i, guess.substring(i, i + 1));
            colors.add(GRAY);
        }

        for (int i = 0; i < this.word.length(); i++) {
            if (guessMap.get(i).equalsIgnoreCase(this.word.substring(i, i + 1))) {
                colors.set(i, GREEN);
                guessMap.put(i, "USED");
                correct = correct.substring(0, i) + " " + correct.substring(i + 1);
            }
        }


        for (int i = 0; i < word.length(); i++) {
            String targetLetter = correct.substring(i, i + 1);
            for (int j = 0; j < this.word.length(); j++) {
                if (guessMap.get(j).equalsIgnoreCase(targetLetter) && colors.get(j) != GREEN) {
                    colors.set(j, YELLOW);
                    guessMap.put(j, "USED");
                    correct = correct.substring(0, i) + " " + correct.substring(i + 1);
                    break;
                }
            }
        }

        int[] pattern = new int[colors.size()];
        for(int i = 0; i < pattern.length; i++){
            pattern[i] = colors.get(i);
        }
        return pattern;
    }



    public void reset() {
        this.size = 0;
        this.gameOver = false;
        this.word = wordsToPickFrom.get((int)(Math.random() * wordsToPickFrom.size()));
        this.board = new int[6][5];
    }



    public int getSize() {
        return size;
    }


    public String getWord() {
        return word;
    }




    public List<String> loadFile(String fileName) throws FileNotFoundException {
        //Scanner dictScan = new Scanner(new File("dic.txt"));
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("dic.txt not found in resources");
        }
        Scanner dictScan = new Scanner(inputStream);
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    //TODO DELETE THIS LATER
    public int[] patternFor(String guess, String target) {
        Map<Integer, String> guessMap = new HashMap<>();
        List<Integer> colors = new ArrayList<>();
        String correct = target;
        for (int i = 0; i < target.length(); i++) {
            guessMap.put(i, guess.substring(i, i + 1));
            colors.add(0);
        }

        for (int i = 0; i < target.length(); i++) {
            if (guessMap.get(i).equalsIgnoreCase(target.substring(i, i + 1))) {
                colors.set(i, 2);
                guessMap.put(i, "USED");
                correct = correct.substring(0, i) + " " + correct.substring(i + 1);
            }
        }

        for (int i = 0; i < target.length(); i++) {
            String targetLetter = correct.substring(i, i + 1);
            for (int j = 0; j < target.length(); j++) {
                if (guessMap.get(j).equalsIgnoreCase(targetLetter) && colors.get(j) != 2) {
                    colors.set(j, 1);
                    guessMap.put(j, "USED");
                    correct = correct.substring(0, i) + " " + correct.substring(i + 1);
                    break;
                }
            }
        }

        int[] pattern = new int[colors.size()];
        for (int i = 0; i < pattern.length; i++) {
            pattern[i] = colors.get(i);
        }
        return pattern;
    }


}
