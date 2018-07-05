import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bonus{
    BufferedImage bonusImage;
    private Point2D.Double position;
    private int type;


    public static final int BONUS_SIZE = 3;

    Bonus(int type, Point2D.Double pos){
        this.type = type;
        bonusImage = null;
        try {
            bonusImage = ImageIO.read(new File("bonus"+type+".png"));
        }catch (IOException e){
            System.out.println("Błąd odczytu obrazu");
            System.exit(4);
        }
        position = pos;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
