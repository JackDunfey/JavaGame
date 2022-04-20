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
    private final Level[] levels;
    private char level_index;
    private Level level;
    public Game(){
        this.player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2));
        levels = new Level[]{new Level("Level 1", 15, 1000L, 100D), new Level("Level 2", 32, 750L, 100D)};
        level = levels[level_index];
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
                } while (Math.sqrt((coords.getX()-player.getPosition().getX())*(coords.getX()-player.getPosition().getX())+(coords.getY()-player.getPosition().getY())*(coords.getY()-player.getPosition().getY())) <= level.min_safe_distance());
                enemies.add(new Zombie(coords));
            }
        }, 500L, level.spawning_rate());
    }

    public void processKeys(){
        if(currentlyActiveKeys.containsKey("ESCAPE") && currentlyActiveKeys.get("ESCAPE")){
            currentlyActiveKeys.put("ESCAPE", false);
            this.ongoing = !this.ongoing;
        }
        player.processKeys(currentlyActiveKeys);
    }

    public void update(){
        if(!ongoing)
            return;
        if (player.isDead())
            end(false);
        else if (player.getKillCount() >= level.needed_kills())
            end(true);
        player.update();
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
        player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2)); // Probably a bad idea
        if(won){
            System.out.println("WON");
            if(++level_index >= levels.length)
                System.exit(69420);
            new Timer().schedule(new TimerTask() {
                public void run(){
                    // Show between levels scene
                    init();
                    ongoing = true;
                }
            }, 600L);
        } else {

        }
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
