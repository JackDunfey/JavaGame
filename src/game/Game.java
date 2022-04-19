package game;

import game.mob.Player;
import game.mob.enemy.Enemy;
import game.mob.enemy.Zombie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Game extends BorderPane{
    Player player;
    ArrayList<Enemy> enemies;
    Timer mob_spawnTimer;
    public boolean ongoing = true;
    public HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    public Game(){
        this.player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2));
    }

    public void init(){
        this.enemies = new ArrayList<Enemy>();
        this.mob_spawnTimer = new Timer("Mob Spawner");
        mob_spawnTimer.schedule(new TimerTask() {
            Point2D getSpawnCoords(){
                return new Point2D(Math.random() * App.WIDTH, Math.random()*App.HEIGHT);
            }
            public void run(){
                Point2D coords;
                do{
                    coords = getSpawnCoords();
                } while (coords.getX() > App.WIDTH/3 && coords.getY() > App.HEIGHT/3 && coords.getX() < App.WIDTH*2/3 && coords.getY() < App.HEIGHT*2/3);
                enemies.add(new Zombie(coords));
            }
        }, 500L, 1000L);
    }

    public void processKeys(){
        player.processKeys(currentlyActiveKeys);
    }

    public void update(){
        if (player.isDead())
            end(false);
        else if (player.getKillCount() >= 10)
            end(true);
        processKeys();
        player.update();
        player.updateBullets();
        for(int i = enemies.size() - 1; i >= 0; i--){
            var enemy = enemies.get(i);
            if(enemy.isDead()){
                enemies.remove(i);
                continue;
            }
            enemy.direction.forward = true;
            enemy.update();
            enemy.facePoint(player.getPosition());
            player.getBullets().forEach(bullet -> {
                if(((Path) Rectangle.intersect(((Rectangle) bullet.getNode()), enemy.getBodyRectangle())).getElements().size() > 0){
                    bullet.kill();
                    enemy.damage(bullet.getDamage(), player);
                }
            });
            if(enemy.isIntersecting(player))
                enemy.attackPlayer(player);
        }
        this.paint();
    }

    public void end(boolean won){
        ongoing = false;
        System.out.println(won ? "You Won! :)" : "You Lost :(");
    }
    
    public void paint(){
        var pane = new Pane(player.getNode());
        enemies.forEach(e -> pane.getChildren().add(e.getNode()));
        this.setCenter(pane);
        var headerPane = new HBox();
        headerPane.getChildren().add(new Text("Kill Count " + player.getKillCount()));
        this.setTop(headerPane);
    }
}
