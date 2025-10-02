import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class HomeScreenGUI extends JPanel {
    private Image backgroundImage;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public HomeScreenGUI() {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/HomeScreen.png"))).getImage();
    }

}
