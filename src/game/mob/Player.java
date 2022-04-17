package game.mob;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Player extends Mob{
    public static final Color PLAYER_COLOR = Color.RED;
    public Player(Point2D position){
        super(position, PLAYER_COLOR);
    }
}
