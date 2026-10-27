package mainmenu;

import java.util.ArrayList;

import animator.AnimationConveyor;
import animator.ContentsAnim;
import animator.interfacewindow.ClosableFactory;
import engine.App;
import engine.ApplicationContext;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenuController {
    Scene scene;
    @SuppressWarnings("unused")
    private ApplicationContext applicationContext;
    AnimationConveyor conveyor;

    public MainMenuController(App app, ApplicationContext applicationContext, Stage stage) {
        start(app, stage);
        this.applicationContext = applicationContext;

    }

    private AnimationConveyor initConveyor() {

        ArrayList<ContentsAnim> c = new ArrayList<>();
        ContentsAnim d = new ContentsAnim("launchimage", 2, 0, 240_000_000L);
        c.add(d);

        return new AnimationConveyor(c, 0, 0, 64, 64);
    }

    public void start(App app, Stage stage) {
        Button closeButton = ClosableFactory.getFormattedCloseButton(new Runnable[] {
                new Runnable() {
                    public void run() {
                        app.weatherPoller.stop();
                        Platform.exit();
                    };
                }
        }, new int[] { 20, 0 });
        Button btn1 = new Button("Start");

        btn1.setOpacity(.8);
        closeButton.setOpacity(.8);

        // Layout container
        VBox root = new VBox(0); // spacing = 10px
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(closeButton);

        root.setBackground(Background.EMPTY);
        this.conveyor = initConveyor();

        conveyor.addToRoot(root);

        TextField textBox = new TextField();
        textBox.setAlignment(Pos.CENTER);
        textBox.setMaxSize(200, 30);
        textBox.setPromptText("Enter a City");
        textBox.setStyle("-fx-control-inner-background: #00000054; -fx-text-fill: white; -fx-prompt-text-fill: white;");
        textBox.setOpacity(.8);
        textBox.selectNextWord();
        textBox.setFocusTraversable(true);
        root.getChildren().addAll(textBox);
        textBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitCity(app, stage, textBox);
            }
        });
        btn1.setOnAction(e -> submitCity(app, stage, textBox));
        root.getChildren().add(btn1);

        // Scene
        Scene scene = new Scene(root, 300, 200);
        scene.setFill(Color.TRANSPARENT);
        this.scene = scene;
        // Stage setup
        stage.setTitle("8 Bit Weather");
        stage.setScene(scene);
        stage.show();

    }

    private void submitCity(App app, Stage stage, TextField textBox) {
        String rawCity = textBox.getText();
        String city = rawCity == null ? "" : rawCity.trim();

        if (city.isEmpty()) {
            textBox.setStyle(
                    "-fx-control-inner-background: #5a0f0f; -fx-text-fill: white; -fx-prompt-text-fill: white;");
            textBox.requestFocus();
            return;
        }

        textBox.setStyle("-fx-control-inner-background: #00000054; -fx-text-fill: white; -fx-prompt-text-fill: white;");
        initAnimator(app, stage, city);
    }

    public void initAnimator(App app, Stage stage, String city) {
        String normalizedCity = city.replaceAll("\\s+", " ").replace(" ", "%20");
        String oldCity = app.applicationContext.city;
        app.weatherPoller.start(normalizedCity, ok -> Platform.runLater(() -> {
            if (!ok) {
                System.out.println("NONE FOUND");
                if (oldCity != null)
                    app.weatherPoller.start(oldCity, x -> {
                    });
                else
                    app.weatherPoller.cancel();
                toast(stage, "❌ Try format: Los Angeles,CA");
                return;
            }
            app.applicationContext.city = normalizedCity;
            app.animationController.start();
        }));
    }

    static void toast(Stage owner, String msg) {
        Label l = new Label(msg);
        l.setStyle(
                "-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 8 14; -fx-font-family: 'Courier New', monospace;");
        Popup p = new Popup();
        p.getContent().add(l);
        p.show(owner);
        PauseTransition t = new PauseTransition(Duration.seconds(5));
        t.setOnFinished(e -> p.hide());
        t.play();
    }
}
