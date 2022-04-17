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
    public Game(){
        this.player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2));
        this.enemies = new ArrayList<Enemy>();
        this.enemies.add(new Zombie(new Point2D(0,0)));
    }
    public void update(){
        player.facePoint(enemies.get(0).getPosition());
        player.forward(1);
        enemies.forEach(enemy -> enemy.facePoint(player.getPosition()));
        this.paint();
        // I'm looking for complication
    }
    public void paint(){
        this.getChildren().clear();
        this.getChildren().addAll(player.getNode());
        enemies.forEach(e -> this.getChildren().add(e.getNode()));
    }
}
