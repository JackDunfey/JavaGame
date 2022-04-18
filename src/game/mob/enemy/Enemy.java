package game.mob.enemy;

import game.mob.Mob;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Enemy extends Mob {
    public Enemy(Point2D position, Color color){
        super(position, color);
        this.speed = 1;
    }
    abstract public void attackPlayer();
    @Override
    public String toString(){
        return "Enemy @ (" + this.getPosition().toString() + ")";
    }
}
