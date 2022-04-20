package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public AnimationTimer animationTimer;
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
        char buttonWidth = 200;
        stage.setTitle("2D Minceraft");
        var play_btn = new Button("Play Game");
            play_btn.setMinWidth(buttonWidth);
        var tutorial_btn = new Button("Play Tutorial");
            tutorial_btn.setMinWidth(buttonWidth);
        var exit_btn = new Button("Exit");
            exit_btn.setMinWidth(buttonWidth);
            exit_btn.setOnAction(__ -> {
                Platform.exit();
            });
        var title_text = new Text("World's Coolest Game");
            title_text.setFont(new Font("Arial", 48));
        var title_buttons = new VBox(18, title_text, play_btn, tutorial_btn, exit_btn);
            title_buttons.setAlignment(Pos.CENTER);
        var title_root = new StackPane(title_buttons);
            StackPane.setAlignment(title_root, Pos.CENTER);
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
                    game.processKeys();
                    game.update();
                    game.player.facePoint(getMousePosition(gameScene));
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
