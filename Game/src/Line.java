/**
 * Class describing line between two points
 */
public class Line {
    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    public final int points;

    /**
     * Constructor of line from coordinates
     * @param x1 Coordinate x of beginning point
     * @param y1 Coordinate y of beginning point
     * @param x2 Coordinate x of ending point
     * @param y2 Coordinate y of ending point
     */
    public Line(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.points = 3;
    }

    /**
     * Constructor of line from coordinates with point value
     * @param x1 Coordinate x of beginning point
     * @param y1 Coordinate y of beginning point
     * @param x2 Coordinate x of ending point
     * @param y2 Coordinate y of ending point
     * @param points Point value of line
     */
    public Line(int x1, int y1, int x2, int y2, int points){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.points = points;
    }
}
