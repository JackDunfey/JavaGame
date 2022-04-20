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

public class Tutorial extends BorderPane{
    Player player;
    ArrayList<Enemy> enemies = new ArrayList<>();
    Timer mob_spawnTimer;
    public boolean ongoing = true;
    public HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();
    private int counter;
    private String helpText = "Zombies appear as green rectangles use your mouse to aim and click to shoot one.";

    private static int[] needed_kills = {1, 1, 5};

    private App app;

    public Tutorial(App app){
        this.app = app;
        this.player = new Player(new Point2D(App.WIDTH/2, App.HEIGHT/2));
        new Timer().schedule(new TimerTask() {
            public void run(){
                spawnZombie();
            }
        }, 2000L);
    }

    private Point2D generateSpawnCoords(){
        return new Point2D(Math.random() * App.WIDTH, Math.random()*App.HEIGHT);
    }

    private void spawnZombie(){
        Point2D coords;
        do{
            coords = generateSpawnCoords();
        } while (Math.sqrt(Math.pow(coords.getX()-player.getPosition().getX(),2)*+Math.pow(coords.getY()-player.getPosition().getY(),2)) <= 250 && coords.getY() < 175);
        enemies.add(new Zombie(coords));
    }

    public void init(){
        this.enemies = new ArrayList<Enemy>();
        if(counter == 1){
            spawnZombie();
            this.helpText += " Zombies will attempt to chase you around. If they touch you, you will lose health. Shoot them as quickly as you can!";
        } else if(counter == 2){
            helpText = "Quick kill five zombies";
            new Timer("Level 2").schedule(new TimerTask() {
                public void run(){
                    spawnZombie();
                }
            }, 1000L, 1250L);
        }
    }

    public void processKeys(){
        if(currentlyActiveKeys.containsKey("ESCAPE") && currentlyActiveKeys.get("ESCAPE")){
            currentlyActiveKeys.put("ESCAPE", false);
            this.ongoing = !this.ongoing;
        }
        // player.processKeys(currentlyActiveKeys);
    }

    public void update(){
        if(!ongoing)
            return;
        if (player.isDead())
            end(false);
        else if (player.getKillCount() >= needed_kills[counter])
            end(true);
        player.update();
        for(int i = enemies.size() - 1; i >= 0; i--){
            var enemy = enemies.get(i);
            if(enemy.isDead()){
                enemies.remove(i);
                continue;
            }
            if(counter > 0){
                enemy.direction.forward = true;
            }
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
            if(++counter >= needed_kills.length){
                // TODO: Tutorial finished logic here
                helpText = "You finished the tutorial! Congratulations!";
                this.setOnMouseClicked(__ -> {
                    app.showTitleScene();
                });
                return;
            }
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
        var text = new Text(this.helpText);
        text.setWrappingWidth(App.WIDTH);
        var helpPane = new Pane(text);
            helpPane.setMinHeight(100);
        this.setTop(headerPane);
        this.setBottom(helpPane);
    }
}
