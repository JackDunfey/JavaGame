package game.mob.enemy;

import java.util.Timer;
import java.util.TimerTask;

import game.mob.Mob;
import game.mob.Player;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Enemy extends Mob {
    private double strength;
    private long attack_reset_duration;
    private boolean canAttack = true;

    public Enemy(Point2D position, Color color, double strength, long attack_reset_duration){
        super(position, color, 1);
        this.speed = 1;
        this.strength = strength;
        this.attack_reset_duration = attack_reset_duration;
    }

    public double getStrength(){
        return this.strength;
    }

    public void attackPlayer(Player player){
        if(!this.canAttack)
            return;
        player.damage(this.strength, this);
        this.canAttack = false;
        new Timer().schedule(new TimerTask() {
            public void run(){
                canAttack = true;
            }
        }, this.attack_reset_duration);
    }
}
