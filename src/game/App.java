package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

public class App extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static boolean isValidPosition(Point2D position){
        return position.getX() >= 0 && position.getX() <= WIDTH &&
            position.getY() >= 0 && position.getY() <= HEIGHT;
    }
    public Point2D getMousePosition(Scene scene){
        return new Robot().getMousePosition().subtract(scene.getWindow().getX(), scene.getWindow().getY()).subtract(scene.getX(), scene.getY());
    }
    private Game game;
    public App(){
        this.game = new Game();
    }
    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("2D Minceraft");
        var title_root = new BorderPane();
        var play_btn = new Button("Play");
        var title_buttons = new VBox(15, play_btn);
        title_root.setCenter(new StackPane(title_buttons));
        var titleScene = new Scene(title_root, WIDTH, HEIGHT);
        play_btn.setOnAction(__ -> {
            var gameScene = new Scene(game, WIDTH, HEIGHT);
            gameScene.setFill(Color.BEIGE);
            stage.setScene(gameScene);
            gameScene.setOnKeyPressed(event -> {
                String s = event.getCode().toString();
                if(!game.currentlyActiveKeys.containsKey(s))
                    game.currentlyActiveKeys.put(s, true);
            });
            gameScene.setOnKeyReleased(event -> {
                String s = event.getCode().toString();
                if(game.currentlyActiveKeys.containsKey(s))
                    game.currentlyActiveKeys.remove(s);
            });
            gameScene.setOnMouseClicked(event -> {
                game.player.shoot();
            });
            game.init();
            new AnimationTimer(){
                public void handle(long currentNanoTime){
                    if(game.ongoing){
                        game.update();
                        game.player.facePoint(getMousePosition(gameScene));
                    } else {
                        this.stop();
                    }
                }
            }.start();
        });
        stage.setScene(titleScene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
