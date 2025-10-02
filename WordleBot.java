import java.io.FileNotFoundException;
import java.util.*;

public class WordleBot {
    private List<String> possibleWords;
    private Wordle wordle;
    public static int[] lastPattern;
    public static String lastGuess;
    private int counter;



    public WordleBot(Wordle wordle, String fileName) throws FileNotFoundException {
        this.wordle = wordle;
        possibleWords = wordle.loadFile(fileName);
        counter = 0;
    }


    public void reset() throws FileNotFoundException {
        counter = 0;
        possibleWords.clear();
        possibleWords = wordle.loadFile("dic.txt");
    }




    /**
     * pick the best word
     */
    public String generateWord(int[] pattern, String guess) {
        if(counter == 0){
            counter++;
            return pickBestWord();
        }
        Set<Character> forbiddenSet = new HashSet<>();
        List<String> yellowList = new ArrayList<>(Collections.nCopies(guess.length(), " "));
        List<String> greenList = new ArrayList<>(Collections.nCopies(guess.length(), " "));// creates [" ", " ", " ", " ", " "]
        for (int i = 0; i < pattern.length; i++) {
            char c = guess.charAt(i);
            if (pattern[i] == 1) {
                yellowList.set(i, c + "");
            } else if (pattern[i] == 2) {
                greenList.set(i, c + "");
            }
        }

        //create forbidden set
        for (int i = 0; i < pattern.length; i++) {
            char c = guess.charAt(i);
            if (pattern[i] == 0 && !yellowList.contains(c + "") && !greenList.contains(c + "")) {
                forbiddenSet.add(c);
            }
        }
        //remove all words with forbidden letters
        Iterator<String> iterator = possibleWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            for(Character c : forbiddenSet){
                if(word.contains(c + "")){
                    iterator.remove();
                    break;
                }
            }
        }
        //remove words without green letters in the correct spot
        iterator = possibleWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            for(int i = 0; i < greenList.size(); i++){
                if(!greenList.get(i).equals(" ") && !word.substring(i, i + 1).equals(greenList.get(i))){
                    iterator.remove();
                    break;
                }
            }
        }
        iterator = possibleWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            boolean valid = true;

            for (int i = 0; i < yellowList.size(); i++) {
                String yellow = yellowList.get(i);
                if (!yellow.equals(" ")) {
                    if (word.substring(i, i + 1).equals(yellow)) {
                        valid = false;
                        break;
                    }

                    if (!word.contains(yellow)) {
                        valid = false;
                        break;
                    }
                }
            }
            if (!valid) {
                iterator.remove();
            }
        }

        return pickBestWord();
        //return pickRandomWord();
    }

    public String pickRandomWord() {
        return possibleWords.get(new Random().nextInt(possibleWords.size()));
    }


    public String pickBestWord() {
        Map<Character, Integer> freq = new HashMap<>();

        // frequency of each letter
        for (String word : possibleWords) {
            Set<Character> seen = new HashSet<>();
            for (char c : word.toCharArray()) {
                if (seen.add(c)) {
                    freq.put(c, freq.getOrDefault(c, 0) + 1);
                }
            }
        }

        // Score each word
        String best = null;
        int bestScore = -1;
        for (String word : possibleWords) {
            Set<Character> seen = new HashSet<>();
            int score = 0;
            for (char c : word.toCharArray()) {
                if (seen.add(c)) {
                    score += freq.getOrDefault(c, 0);
                }
            }
            if (score > bestScore) {
                bestScore = score;
                best = word;
            }
        }

        return best;
    }

    public int getDicSize() {
        return possibleWords.size();
    }

}
