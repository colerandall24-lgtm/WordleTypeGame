import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class GUIWindow extends JFrame {
    private JPanel frame;
    private ColorPanel[] alphabetPanels;
    private JPanel alphabetPanel;
    private int size;
    private ColorPanel[][] colorPanel;
    private JPanel buttonPanel;
    private Wordle wordle;
    private JLabel messageLabel;
    private JButton startButton;
    private String guess;
    private Set<String> greenChars;
    private Set<String> yellowChars;
    private int guesses;
    private int wins;
    private WordleBot bot;
    private KeyBoardListener keyBoardListener;
    private JFrame helperBotFrame;
    private HelperBotPanel helperBotPanel;
    private JLabel helperBotLabel;
    private BotGUIWindow botFrame;
    private WordleBot helperBot;
    private JButton backButton;
    private boolean hasBot;


    public GUIWindow(Wordle wordle, boolean hasBot) throws FileNotFoundException {
        //initialize the game
        //this.wordle = new Wordle("crack");//set the word to guess as hello
        this.wordle = wordle;
        this.hasBot = hasBot;
        size = wordle.getSize();
        yellowChars = new HashSet<>();
        greenChars = new HashSet<>();
        alphabetPanel = new JPanel(new GridLayout());
        guesses = 0;
        wins = 0;
        this.keyBoardListener = new KeyBoardListener();
        guess = "";
        this.setLocationRelativeTo(null);

        //bot stuff
        if (hasBot) {
            botFrame = new BotGUIWindow(new Wordle(wordle.getWord()));//toDo why does this have a parameter
            bot = new WordleBot(new Wordle(wordle.getWord()), "dic.txt");//TODO this makes the bot better or worse
            //botFrame.addBot(bot);
        }
        //initialize the graphics
        this.setTitle("Wordle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startButton = new JButton("Restart");
        startButton.addActionListener(new startListener());
        startButton.setVisible(false);

        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonListener());

        alphabetPanels = new ColorPanel[26];
        String alphabetSTR = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < alphabetSTR.length(); i++) {
            alphabetPanels[i] = new ColorPanel(Color.lightGray, alphabetSTR.substring(i, i + 1));
        }

        //the commented out code below puts the alphabet at the top of the game in the order
        //shown on a keyboard

        /*String alphabetSTR = "qwertyuiopasdfghjklzxcvbnm";
        for(int i = 0; i < alphabetSTR.length(); i++){
            alphabetPanels[i] = new ColorPanel(Color.lightGray, this, alphabetSTR.substring(i, i+1));
        }*/

        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Lobster", Font.PLAIN, 20));//TODO
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame = new JPanel(new GridLayout(wordle.getNumGuesses(), wordle.getWord().length()));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(150, 250, 150));


        colorPanel = new ColorPanel[wordle.getNumGuesses()][wordle.getWord().length()];

        //fill the board with new light gray color panels
        for(int i = 0; i < wordle.getNumGuesses(); i++){
            for(int j = 0; j < wordle.getWord().length(); j++){
                colorPanel[i][j] = new ColorPanel(Color.LIGHT_GRAY);
            }
        }
        //add the board to the frame
        for(int i = 0; i < colorPanel.length; i++){
            for(int j = 0; j < colorPanel[0].length; j++){
                frame.add(colorPanel[i][j]);
            }
        }
        //add the available letters to the bar of letters at the top
        for(int i = 0; i < alphabetPanels.length; i++){
            alphabetPanel.add(alphabetPanels[i]);
        }
        //add buttons for guessing to the button panel
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(messageLabel);
        buttonPanel.add(backButton, BorderLayout.EAST);
        buttonPanel.add(startButton, BorderLayout.SOUTH);


        //this.addKeyListener(keyBoardListener);
        this.setFocusable(true);
        this.requestFocusInWindow();

        //add the frame to the content pane and create the window
        frame.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 30));
        Container pane = getContentPane();
        pane.add(frame, BorderLayout.CENTER);
        pane.add(buttonPanel, BorderLayout.SOUTH);
        pane.add(alphabetPanel, BorderLayout.NORTH);
        this.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        this.setSize((int)(screenWidth * 0.4), (int)(screenHeight * 0.8));

        resetAllPanels();
    }


    /**
     * Records a guess using the wordle game and updates the color panels
     * @param word word that is being guessed
     */
    private void guess(String word){
        if (word.length() != wordle.getWord().length()) {
            messageLabel.setText("Guess must be 5 letters!");
        } else {
            this.guess = word;
            boolean validGuess = true;
            try {
                int[] pattern = wordle.recordGuess(word);
                size = wordle.getSize();
                setColorPanel(pattern);
                messageLabel.setText("");
                setAlphabet();
                guesses++;
                repaint();
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
                validGuess = false;
            }

            // Let the bot guess, even if the game is over, as long as the player's guess was valid
            if (hasBot && validGuess) {
                String botGuess = bot.generateWord(bot.lastPattern, bot.lastGuess);
                botFrame.guess(botGuess);
                bot.lastGuess = botGuess;
                bot.lastPattern = wordle.patternFor(botGuess);
            }

            // Now determine outcome after both have had their turn
            //if (wordle.gameOver()) {
            if (wordle.gameOver() || (hasBot && botFrame.getWordle().gameOver())) {
                this.removeKeyListener(keyBoardListener);
                if (hasBot) {
                    botFrame.showLabels();
                    boolean playerWon = wordle.won(size - 1);
                    boolean botWon = botFrame.getWordle().won(botFrame.getWordle().getSize() - 1);

                    if (playerWon && !botWon) {
                        wins++;
                        JOptionPane.showMessageDialog(this, "You Beat The Bot!");
                    } else if (!playerWon && botWon) {
                        JOptionPane.showMessageDialog(this, "The Bot Beat You!");
                        wins = 0;
                    } else if (playerWon && botWon) {
                        JOptionPane.showMessageDialog(this, "It's a tie!");
                    } else {
                        JOptionPane.showMessageDialog(this, "You both lost.");
                        wins = 0;
                    }
                } else {
                    if (wordle.won(size - 1)) {
                        wins++;
                        String message = "You Won in " + guesses + " guesses!\nYou have won " + wins + " times in a row";
                        JOptionPane.showMessageDialog(this, message);
                    } else {
                        JOptionPane.showMessageDialog(this, "You lost!\nThe correct word was " + wordle.getWord());
                        wins = 0;
                    }
                }

                showNewGameButton(true);
            }
        }
        /*boolean flag = true;
        if (word.length() != wordle.getWord().length()) {
            messageLabel.setText("Guess must be 5 letters!");
        }
        else {
            this.guess = word;
            try {
                int[] pattern = wordle.recordGuess(word);
                size = wordle.getSize();
                setColorPanel(pattern);
                messageLabel.setText("");
                setAlphabet();
                guesses++;
                repaint();
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
                flag = false;
            }

            if(hasBot && flag) {
                String botGuess = bot.generateWord(bot.lastPattern, bot.lastGuess);
                botFrame.guess(botGuess);
                bot.lastGuess = botGuess;
                bot.lastPattern = wordle.patternFor(botGuess);
            }

            if (wordle.gameOver()) {
                if (hasBot) {
                    botFrame.showLabels();
                    boolean playerWon = wordle.won(size - 1);
                    boolean botWon = botFrame.getWordle().won(botFrame.getWordle().getSize() - 1);

                    if (playerWon && !botWon) {
                        wins++;
                        JOptionPane.showMessageDialog(this, "You Beat The Bot!");
                    } else if (!playerWon && botWon) {
                        JOptionPane.showMessageDialog(this, "The Bot Beat You!");
                        wins = 0;
                    } else if (playerWon && botWon) {
                        JOptionPane.showMessageDialog(this, "It's a tie!");
                    } else {
                        JOptionPane.showMessageDialog(this, "You both lost.");
                        wins = 0;
                    }
                } else {
                    if (wordle.won(size - 1)) {
                        wins++;
                        String message = "You Won in " + guesses + " guesses!\nYou have won " + wins + " times in a row";
                        JOptionPane.showMessageDialog(this, message);
                    } else {
                        JOptionPane.showMessageDialog(this, "You lost!\nThe correct word was " + wordle.getWord());
                        wins = 0;
                    }
                }

                showNewGameButton(true);
            }
        }*/
    }

    public void addHelperBot() throws FileNotFoundException {
        helperBotFrame = new JFrame();
        helperBotPanel = new HelperBotPanel();
        helperBot = new WordleBot(this.wordle, "dic.txt");
        helperBotLabel = new JLabel("Suggested Guess " + helperBot.pickBestWord());
        helperBotFrame.setLayout(new BorderLayout());


        helperBotFrame.add(helperBotLabel, BorderLayout.SOUTH);

        helperBotPanel.setPreferredSize(new Dimension(360, 450));
        helperBotFrame.add(helperBotPanel, BorderLayout.NORTH);





        helperBotFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        helperBotFrame.setSize(450, 550);
        helperBotFrame.setResizable(false);
        helperBotFrame.setLocation(1030, 80);
        helperBotFrame.setUndecorated(true);
        helperBotFrame.setVisible(true);
    }

    public void showNewGameButton(boolean show) {
        startButton.setVisible(show);
        repaint();
    }

    /**
     * reset the game pick a new word and reset the number of guesses
     */
    private void resetAllPanels() throws FileNotFoundException {
        for(int i = 0; i < colorPanel.length; i++){
            for(int j = 0; j < colorPanel[0].length; j++){
                colorPanel[i][j].setColor(Color.LIGHT_GRAY);
                colorPanel[i][j].setLabel("");
            }
        }
        for (ColorPanel panel : alphabetPanels) {
            panel.setColor(Color.LIGHT_GRAY);
        }
        greenChars.clear();
        if(hasBot){
            botFrame.resetAllPanels();
        }
        yellowChars.clear();
        guesses = 0;
        this.addKeyListener(keyBoardListener);
        guess = "";
        this.setFocusable(true);
        repaint();
    }

    //TODO make sure game is no longer playable when this is called
    public void gameOver(){
        JOptionPane.showMessageDialog(this, "You lost!");
        showNewGameButton(true);
        this.setFocusable(false);
        wins = 0;
    }

    /**
     * transfer the board pattern from the wordle game into the color panels
     * @param pattern the pattern given by the wordle game
     */
    private void setColorPanel(int[] pattern){
        for(int i = 0; i < colorPanel[0].length; i++){
            if(pattern[i] == 2) {
                colorPanel[size-1][i].setColor(Color.GREEN);
                colorPanel[size-1][i].setLabel(guess.substring(i, i+1));
            }
            else if(pattern[i] == 1) {
                colorPanel[size-1][i].setColor(Color.YELLOW);
                colorPanel[size-1][i].setLabel(guess.substring(i, i+1));
            }
            else if(pattern[i] == 0) {
                colorPanel[size-1][i].setColor(Color.DARK_GRAY);
                colorPanel[size-1][i].setLabel(guess.substring(i, i+1));
            }
        }
    }

    public void setVisible(boolean visible){
        this.setLocationRelativeTo(null);
        super.setVisible(visible);
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
     * updates the letters on the top after a guess
     */
    public void setAlphabet(){
        for(int i = 0; i < colorPanel[0].length; i++){
            int col = colorPanel[size - 1][i].getColor();
            if(col == 2) {
                greenChars.add(colorPanel[size-1][i].getTitle());
            }
            else if(col == 1) {
                yellowChars.add(colorPanel[size-1][i].getTitle());
            }
        }
        for(int i = 0; i < alphabetPanels.length; i++){
            if(greenChars.contains(alphabetPanels[i].getTitle())){
                alphabetPanels[i].setColor(Color.GREEN);
            }
            else if(yellowChars.contains(alphabetPanels[i].getTitle())){
                alphabetPanels[i].setColor(Color.YELLOW);
            }
        }
        for(int i = 0; i < alphabetPanels.length; i++){
            if(guess.contains(alphabetPanels[i].getTitle()) && !yellowChars.contains(alphabetPanels[i].getTitle()) &&
                    !greenChars.contains(alphabetPanels[i].getTitle())){
                alphabetPanels[i].setColor(Color.DARK_GRAY);
            }
        }

    }









    public Wordle getWordle() {
        return wordle;
    }






    public JFrame getBotFrame(){
        return botFrame;
   }



   public void openBotWindow() {
       botFrame.setVisible(true);
       this.toFront();
   }

    public WordleBot getBot() {
        return bot;
    }



    private class KeyBoardListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            char ch = e.getKeyChar();
            if (Character.isLetter(ch) && guess.length() < wordle.getWord().length()) {
                guess += Character.toLowerCase(ch);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                guess(guess);
                if(guess.length() == wordle.getWord().length() && wordle.checkGuess(guess)) {
                    guess = "";
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !guess.isEmpty()) {
                guess = guess.substring(0, guess.length() - 1);
            }
            setColorPanel(guess);
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class startListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Reset game logic
                wordle.reset();

                // Update bot and botFrame if bot exists
                if (hasBot) {
                    // Update BotGUIWindow's wordle instance
                    botFrame.wordle = new Wordle(wordle.getWord());
                    botFrame.resetAllPanels();

                    // Recreate bot with new Wordle
                    bot = new WordleBot(new Wordle(wordle.getWord()), "dic.txt");
                    //botFrame.addBot(bot);
                }

                resetAllPanels();
                startButton.setVisible(false);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class BackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose(); // Properly closes this window
            if(hasBot){
                botFrame.dispose();
            }
            WordleApp.runHomeScreen();
        }
    }


}
