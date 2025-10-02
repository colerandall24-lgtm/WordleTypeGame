import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class HelperBotPanel extends JPanel {
    private Image backgroundImage;

    public HelperBotPanel() {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/BotDog.png"))).getImage();
        this.setSize(backgroundImage.getWidth(null), backgroundImage.getHeight(null));//TODO
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}
