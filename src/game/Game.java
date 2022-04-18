package game;

import game.mob.Player;
import game.mob.enemy.Enemy;
import game.mob.enemy.Zombie;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class Game extends Pane{
    Player player;
    ArrayList<Enemy> enemies;
    public ArrayList<String> currentlyActiveKeys = new ArrayList<>();
    public Game(){
        this.player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2));
        this.enemies = new ArrayList<Enemy>();
        this.enemies.add(new Zombie(new Point2D(0,0)));
    }

    public void processKeys(){
        player.direction.forward = currentlyActiveKeys.contains("W");
        player.direction.left = currentlyActiveKeys.contains("A");
        player.direction.backward = currentlyActiveKeys.contains("S");
        player.direction.right = currentlyActiveKeys.contains("D");
    }

    public void update(){
        processKeys();
        // player.facePoint(enemies.get(0).getPosition());
        player.update();
        enemies.forEach(enemy -> {
            enemy.direction.forward = true;
            enemy.update();
            enemy.facePoint(player.getPosition());
        });
        this.paint();
        // I'm looking for complication
    }
    public void paint(){
        this.getChildren().clear();
        this.getChildren().addAll(player.getNode());
        enemies.forEach(e -> this.getChildren().add(e.getNode()));
    }
}
