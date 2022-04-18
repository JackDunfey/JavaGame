package game.mob.ally;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Friend extends Ally{
    public static final Color COLOR = Color.BLUE;
    public Friend(Point2D position){
        super(position, COLOR);
    }
}
