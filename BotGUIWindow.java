import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BotGUIWindow extends JFrame {
    private JPanel frame;
    private ColorPanel[][] colorPanel;
    public Wordle wordle;
    private String guess;
    private Set<String> greenChars;
    private Set<String> yellowChars;
    private int guesses;


    public BotGUIWindow(Wordle wordle) {
        //initialize the game
        //this.wordle = new Wordle("bozos");//set the word to guess as hello
        this.wordle = wordle;
        yellowChars = new HashSet<>();
        greenChars = new HashSet<>();
        guesses = 0;
        guess = "";




        //initialize the graphics
        this.setTitle("Wordle Bot");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);

        frame = new JPanel(new GridLayout(wordle.getNumGuesses(), wordle.getWord().length()));


        colorPanel = new ColorPanel[wordle.getNumGuesses()][wordle.getWord().length()];

        //fill the board with new light gray color panels
        for(int i = 0; i < wordle.getNumGuesses(); i++){
            for(int j = 0; j < wordle.getWord().length(); j++){
                colorPanel[i][j] = new ColorPanel(Color.LIGHT_GRAY, this);
            }
        }
        //add the board to the frame
        for(int i = 0; i < colorPanel.length; i++){
            for(int j = 0; j < colorPanel[0].length; j++){
                frame.add(colorPanel[i][j]);
            }
        }


        //this.addKeyListener(new KeyBoardListener());
        this.setFocusable(false);
        this.requestFocusInWindow();

        //add the frame to the content pane and create the window
        frame.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 30));
        Container pane = getContentPane();
        pane.add(frame, BorderLayout.CENTER);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        this.setSize((int)(screenWidth * 0.2), (int)(screenHeight * 0.2));


        resetAllPanels();
    }

    public void setVisible(boolean visible){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        this.setLocation((int)(screenWidth * 0.7), (int)(screenHeight * 0.2));


        this.setFocusable(false);
        this.pack();
        super.setVisible(visible);
    }



    /**
     * reset the game pick a new word and reset the number of guesses
     */
    protected void resetAllPanels() {
        for(int i = 0; i < colorPanel.length; i++){
            for(int j = 0; j < colorPanel[0].length; j++){
                colorPanel[i][j].setColor(Color.LIGHT_GRAY);
                colorPanel[i][j].setLabel("");
            }
        }
        greenChars.clear();
        yellowChars.clear();
        guesses = 0;
        guess = "";
        repaint();
    }


    /**
     * transfer the board pattern from the wordle game into the color panels
     * @param pattern the pattern given by the wordle game
     */
    private void setColorPanel(int[] pattern){
        for (int i = 0; i < colorPanel[0].length; i++) {
            if (pattern[i] == 2) {
                colorPanel[guesses][i].setColor(Color.GREEN);
                colorPanel[guesses][i].letter.setVisible(false);
                colorPanel[guesses][i].setLabel(guess.substring(i, i + 1));
            } else if (pattern[i] == 1) {
                colorPanel[guesses][i].letter.setVisible(false);
                colorPanel[guesses][i].setColor(Color.YELLOW);
                colorPanel[guesses][i].setLabel(guess.substring(i, i + 1));
            } else if (pattern[i] == 0) {
                colorPanel[guesses][i].letter.setVisible(false);
                colorPanel[guesses][i].setColor(Color.DARK_GRAY);
                colorPanel[guesses][i].setLabel(guess.substring(i, i + 1));
            }
        }
    }

    private void setColorPanel(String guess){
        if (guesses >= colorPanel.length) {
            return;
        }
        while (guess.length() < colorPanel[0].length) {
            guess = guess + " ";
        }
        for (int i = 0; i < colorPanel[0].length; i++) {
            colorPanel[guesses][i].setLabel(guess.substring(i, i + 1));
        }
        repaint();
    }

    /**
     * Records a guess using the wordle game and updates the color panels
     * @param word word that is being guessed
     */
    public void guess(String word){
        this.guess = word;
        int[] pattern = wordle.recordGuess(word);
        setColorPanel(pattern);
        guesses++;
        repaint();
        if (wordle.gameOver()) {
            showLabels();
            // Let GUIWindow handle the result message
        }
    }

    protected void showLabels() {
        for(int i = 0; i < colorPanel.length; i++){
            for(int j = 0; j < colorPanel[0].length; j++){
                colorPanel[i][j].letter.setVisible(true);
            }
        }
    }

   /* public void addBot(WordleBot bot) {
        this.bot = bot;
    }*/

    public Wordle getWordle() {
        return wordle;
    }
}
