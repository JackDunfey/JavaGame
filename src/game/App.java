package game;

import java.text.Normalizer.Form;

import game.dependencies.FormattedText;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
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

    private Stage stage;
    private Scene scene;
    private Scene titleScene;

    private Game game;
    private Tutorial tutorial;

    public static boolean isValidPosition(Point2D position){
        return position.getX() >= 0 && position.getX() <= WIDTH &&
            position.getY() >= 0 && position.getY() <= HEIGHT;
    }

    private Point2D mousePosition;
    public Point2D getMousePosition(Scene scene){
        try{
            return new Robot().getMousePosition().subtract(scene.getWindow().getX(), scene.getWindow().getY()).subtract(scene.getX(), scene.getY());
        } catch (Exception e){
            if(mousePosition == null)
                throw e;
            return mousePosition;
        }
    }

    public App(){
        this.game = new Game(this);
        this.tutorial = new Tutorial(this);
    }

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        char buttonWidth = 200;
        stage.setTitle("2D Minceraft");
        var play_btn = new Button("Play Game");
            play_btn.setMinWidth(buttonWidth);
        var tutorial_btn = new Button("Play Tutorial");
            tutorial_btn.setMinWidth(buttonWidth);
            tutorial_btn.setOnAction(__ -> {
                this.tutorial = new Tutorial(this);
                var gameScene = new Scene(tutorial, WIDTH, HEIGHT);
                gameScene.setFill(Color.BEIGE);
                setScene(gameScene);
                gameScene.setOnKeyPressed(event -> {
                    String s = event.getCode().toString();
                    if(!tutorial.currentlyActiveKeys.containsKey(s))
                        tutorial.currentlyActiveKeys.put(s, true);
                });
                gameScene.setOnKeyReleased(event -> {
                    String s = event.getCode().toString();
                    if(tutorial.currentlyActiveKeys.containsKey(s))
                    tutorial.currentlyActiveKeys.remove(s);
                });
                gameScene.setOnMouseClicked(event -> {
                    tutorial.player.shoot();
                });
                this.animationTimer = new AnimationTimer(){
                    public void handle(long currentNanoTime){
                        tutorial.processKeys();
                        tutorial.update();
                        tutorial.player.facePoint(getMousePosition(gameScene));
                    }
                };
                animationTimer.start();
            });
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
        this.titleScene = new Scene(title_root, WIDTH, HEIGHT);
        play_btn.setOnAction(__ -> {
            this.game = new Game(this);
            var gameScene = new Scene(game, WIDTH, HEIGHT);
            gameScene.setFill(Color.BEIGE);
            setScene(gameScene);
            gameScene.setOnKeyPressed(event -> {
                String s = event.getCode().toString();
                if(s == "C"){
                    this.rollCredits();
                    return;
                }
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
            this.animationTimer = new AnimationTimer(){
                public void handle(long currentNanoTime){
                    game.processKeys();
                    game.update();
                }
            };
            animationTimer.start();
        });
        setScene(titleScene);
        stage.show();
    }

    public Scene getScene(){
        return scene;
    }

    // Switch scenes

    public void setScene(Scene scene){
        this.scene = scene;
        this.stage.setScene(scene);
    }

    public void showTitleScene(){
        setScene(titleScene);
    }

    public void rollCredits(){
        if(this.animationTimer != null)
            animationTimer.stop();

        var credits_root = new VBox(15);
            credits_root.setAlignment(Pos.TOP_CENTER);

        // Add credits
        var title = new Text("Credits");
            title.setFont(new Font("Arial", 36));
            credits_root.getChildren().add(title);
        FormattedText heading = new FormattedText(new Font("Arial", 20));
        FormattedText name = new FormattedText(Font.getDefault());
        String[] headings = {
            "Lead Developer",
            "Graphic Designer",
        };
        String[][] input = {
            {"pyjshtml"},
            {"pyjshtml"},
        };
        for(int i = 0; i < input.length; i++){
            credits_root.getChildren().add(heading.getTextFromString(headings[i]));
            for(int j = 0; j < input[i].length; j++){
                credits_root.getChildren().add(name.getTextFromString(input[i][j]));
            }
        }

        // setScene
        var container = new StackPane(credits_root);
            StackPane.setAlignment(container, Pos.TOP_CENTER);
        var creditsScreen = new Scene(container, App.WIDTH, App.HEIGHT);
        creditsScreen.setOnContextMenuRequested(__ -> {
            showTitleScene();
        });
        setScene(creditsScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
