import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class WordleApp {
    private static JPanel hs;
    private static JFrame hsFrame;

    public static void main(String[] args) throws FileNotFoundException {
        runHomeScreen();
    }

    public static void runHomeScreen(){
        hs = new HomeScreenGUI();
        hsFrame = new JFrame();
        JPanel panel = new JPanel();

        //Buttons
        JButton startButton = new JButton("Wordle");
        JButton botButton = new JButton("Wordle Against Bot");
        JButton helperBotButton = new JButton("Wordle with Helper");
        helperBotButton.addActionListener(new HelperButtonListener());
        startButton.addActionListener(new WordleButtonListener());
        botButton.addActionListener(new WordleBotButtonListener());


        panel.add(startButton);
        panel.add(botButton);
        //panel.add(helperBotButton);

        panel.setBackground(new Color(30, 100, 50).darker());
        hsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hsFrame.add(hs, BorderLayout.CENTER);
        hsFrame.add(panel, BorderLayout.SOUTH);
        hsFrame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        hsFrame.setSize((int)(screenWidth * 0.4), (int)(screenHeight * 0.8));
        hsFrame.setResizable(false);
        hsFrame.setLocationRelativeTo(null);
        hsFrame.setVisible(true);
    }


    private static class WordleButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                startWordleGame();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static class WordleBotButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                startBotWordle();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void startWordleGame() throws FileNotFoundException {
        Wordle wordle = new Wordle();
        GUIWindow window = new GUIWindow(wordle, false);
        window.setVisible(true);
        hsFrame.dispose();
    }

    private static void startBotWordle() throws FileNotFoundException {
        Wordle wordle = new Wordle();
        //Wordle wordle = new Wordle("water");
        GUIWindow window = new GUIWindow(wordle, true);
        window.setVisible(true);
        window.openBotWindow();
        hsFrame.dispose();
    }

    private static class HelperButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                startHelperWordle();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void startHelperWordle() throws FileNotFoundException {
        Wordle wordle = new Wordle();
        GUIWindow window = new GUIWindow(wordle, false);
        window.addHelperBot();
        window.setVisible(true);
        hsFrame.dispose();
    }

}
