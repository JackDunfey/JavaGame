package game.mob;

import java.util.HashMap;

import game.Sound;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

// Make ShootingMob an abstract class

public class Player extends ShooterMob{
    public static final Color PLAYER_COLOR = Color.RED;
    public static final String SHOOT_SOUND = "resources/sounds/shoot.wav";

    public Player(Point2D position){
        super(position, PLAYER_COLOR, 10);
        this.speed = 2.5D;
    }

    public void processKeys(HashMap<String, Boolean> keys){
        direction.forward = keys.containsKey("W");
        direction.left = keys.containsKey("A");
        direction.backward = keys.containsKey("S");
        direction.right = keys.containsKey("D");
        direction.ccw = keys.containsKey("LEFT");
        direction.cw = keys.containsKey("RIGHT");
    }

    public void playShootSound() {
        try{
            var sound = new Sound(SHOOT_SOUND);
            sound.play();
            // TODO: store sounds so they can be stopped
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }

    @Override
    public Node getNode(){
        Group group = new Group(super.getNode());
        getBullets().forEach(bullet -> group.getChildren().add(bullet.getNode()));
        return group;
    }

    @Override
    public void shoot(){
        super.shoot();
        playShootSound();
    }
}
