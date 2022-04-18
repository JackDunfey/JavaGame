package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
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
        gameScene.setFill(Color.BEIGE);
        stage.setScene(gameScene);
        gameScene.setOnKeyPressed(event -> {
            String s = event.getCode().toString();
            if(!game.currentlyActiveKeys.contains(s))
                game.currentlyActiveKeys.add(s);
        });
        gameScene.setOnKeyReleased(event -> {
            String s = event.getCode().toString();
            if(game.currentlyActiveKeys.contains(s))
                game.currentlyActiveKeys.remove(s);
        });
        // gameScene.setOnMouseMoved(event -> {
        //     var x = event.getSceneX();
        //     var y = event.getSceneY();
        //     game.enemies.get(0).setPosition(new Point2D(x, y));
        // });
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
