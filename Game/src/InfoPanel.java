import javax.swing.*;
import java.awt.*;

/**
 * Class describing panel showing info about game stats, current level and score.
 */
public class InfoPanel extends JPanel {
    JLabel levelLabel;
    JLabel timeLabel;
    JLabel staminaLabel;
    JLabel xVelocityLabel;
    JLabel yVelocityLabel;
    JLabel maxVelocityLabel;
    JLabel gravityLabel;
    JLabel scoreLabel;

    /**
     * Default constructor.
     */
    public InfoPanel(){
        levelLabel = new JLabel("Aktualny poziom: 0");
        timeLabel = new JLabel("Pozostały czas: 0:00");
        staminaLabel = new JLabel("Pozostała wytrzymałość: 0");
        xVelocityLabel = new JLabel("Aktualna prędkość X: 0");
        yVelocityLabel = new JLabel("Aktualna prędkość Y: 0");
        maxVelocityLabel = new JLabel("Maksymalna prędkość Y: 0");
        gravityLabel = new JLabel("Stała grawitacyjna: 0.0");
        scoreLabel = new JLabel("Aktualny wynik: 0");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setDoubleBuffered(true);
        this.add(levelLabel);
        this.add(timeLabel);
        this.add(staminaLabel);
        this.add(xVelocityLabel);
        this.add(yVelocityLabel);
        this.add(maxVelocityLabel);
        this.add(gravityLabel);
        this.add(scoreLabel);
        this.setPreferredSize(new Dimension(180, 800));
    }
}
