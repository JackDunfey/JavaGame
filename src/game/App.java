package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private Game game;
    public App(){
        this.game = new Game();
    }
    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("2D Minceraft");
        game.update();
        var gameScene = new Scene(game, WIDTH, HEIGHT);
        stage.setScene(gameScene);
        gameScene.setOnMouseMoved(event -> {
            var x = event.getSceneX();
            var y = event.getSceneY();
            game.enemies.get(0).setPosition(new Point2D(x, y));
        });
        new AnimationTimer(){
            public void handle(long currentNanoTime){
                game.update();
            }
        }.start();
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
