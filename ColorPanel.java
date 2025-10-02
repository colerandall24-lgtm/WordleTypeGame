import javax.swing.*;
import java.awt.*;

public class ColorPanel extends JPanel {
    private Color color;

    protected JLabel letter;
    private String title;

    public ColorPanel(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(60, 60));
        setBackground(color);
        setLayout(new BorderLayout());


        letter = new JLabel("");
        letter.setHorizontalAlignment(JLabel.CENTER);
        letter.setVerticalAlignment(JLabel.CENTER);
        letter.setFont(new Font("Lobster", Font.BOLD, 50));
        this.add(letter, BorderLayout.CENTER);
        letter.setVisible(true);
    }
    public ColorPanel(Color color, String letterStr) {
        this.color = color;
        setPreferredSize(new Dimension(60, 60));
        setBackground(color);
        setLayout(new BorderLayout());
        this.title = letterStr;
        letter = new JLabel(letterStr);
        letter.setHorizontalAlignment(JLabel.CENTER);
        letter.setVerticalAlignment(JLabel.CENTER);
        letter.setFont(new Font("Lobster", Font.BOLD, 20));
        this.add(letter, BorderLayout.CENTER);
        letter.setVisible(true);
    }
    public ColorPanel(Color color, BotGUIWindow window) {
        this.color = color;
        setPreferredSize(new Dimension(60, 60));
        setBackground(color);
        setLayout(new BorderLayout());


        letter = new JLabel("");
        letter.setHorizontalAlignment(JLabel.CENTER);
        letter.setVerticalAlignment(JLabel.CENTER);
        letter.setFont(new Font("Lobster", Font.BOLD, 50));
        this.add(letter, BorderLayout.CENTER);
        letter.setVisible(true);
    }

    public String getLetter() {
        return letter.getText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void setLabel(String letter){
        this.letter.setText(letter);
        this.title = letter;
        this.letter.setBackground(color);
    }

    public String getTitle() {
        return title;
    }

    public int getColor(){
        if(color.equals(Color.GREEN)){
            return 2;
        }
        else if(color.equals(Color.YELLOW)){
            return 1;
        }
        else if(color.equals(Color.LIGHT_GRAY)){
            return 0;
        }
        return -1;
    }
}
