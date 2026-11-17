package ui;

import java.net.URI;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import util.ImageGrabber;

/**
 * a Disused, depracated, class used as cached code for my own reference and future uses.
 * CustomTaskBar
 */
public class CustomTaskBar {
    int animWidth = 10;
        boolean couldUpdateApp;
        public static int height = 25;
        private Button closeButton;
        private Button minimizeButton;
        // private Button forecastButton;
        /*** @param updateButton a button that allows the user to check for updates to the application. It is only visible if the application can be updated.*/
        private Button invertButton;
        private Button updateButton;
        Image taskbarImg;// do
        ImageView taskbarView;
        Group container;
        CustomTaskBar(Pane root, ArrayList<Runnable> shutdownL) {
            taskbarView = new ImageView(taskbarImg);

            taskbarView.setLayoutX(0);
            taskbarView.setLayoutY(0);
            taskbarView.setFitWidth(animWidth);
            taskbarView.setFitHeight(CustomTaskBar.height);

            container = new Group(taskbarView);   // field: Group container
            root.getChildren().add(container);
            initButtons(container, shutdownL);    // was: root
            
        }

        
        @SuppressWarnings("unused")
        public int getHeight() {
            return CustomTaskBar.height;
        }

        @SuppressWarnings("unused")
        public int getWidth() {
            return animWidth;
        }
        public double getLayoutX() {
            return container.getLayoutX();
        }
        public double getLayoutY() {
            return container.getLayoutY();
        }
        public void setLayoutX(double v) {
            container.setLayoutX(v);
        }
        public void setLayoutY(double v) {
            container.setLayoutY(v);
        }

        public void initButtons(Group container, ArrayList<Runnable> shutdownL) {
            this.minimizeButton = new Button("-");
            this.closeButton = new Button("x");
            this.updateButton = getUpdateButton();
            this.invertButton = new Button("⇅");

            
            
            double buttonSize = height - 6;

            for (Button button : new Button[] {
                    minimizeButton,
                    closeButton,
                    // forecastButton,
                    updateButton,
                    invertButton
            }) {
                button.setMinSize(buttonSize, buttonSize);
                button.setPrefSize(buttonSize, buttonSize);
                button.setMaxSize(buttonSize, buttonSize);
            }

            // ..________________ positioning of elements _______________..//
            closeButton.setLayoutX(animWidth - 22);
            minimizeButton.setLayoutX(animWidth - 42);
            invertButton.setLayoutX(3);
            // forecastButton.setLayoutX(3);
            updateButton.setLayoutX(23);

            updateButton.setLayoutY(3);
            invertButton.setLayoutY(3);
            closeButton.setLayoutY(3);
            // forecastButton.setLayoutY(3);
            minimizeButton.setLayoutY(3);

            // ..________________________ Tool tip  ________________________..//

            updateButton.setTooltip(new Tooltip("New Update Available"));
            invertButton.setTooltip(new Tooltip("Flip forecast view"));
            // ..________________ styling and styling events _______________..//

            installButtonStyling(closeButton);
            installButtonStyling(updateButton);
            installButtonStyling(invertButton);
            // installButtonStyling(forecastButton);
            installButtonStyling(minimizeButton);

            // ..________________ click action events _______________..//
            closeButton.setOnMouseClicked(event -> {
                Platform.exit();// Temp (later i may add system tray functionality)
                for (Runnable r : shutdownL) {
                    r.run();
                }
            });
            invertButton.setOnMouseClicked(event->{
                // invertLayout();
            });
            minimizeButton.setOnMouseClicked(event -> {
                // weatherDashboardController.onMinimizePressed();

            });

            // updateButton.setOnMouseClicked(e->{
            //     try {
            //         if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            //             Desktop.getDesktop().browse(URI.create("https://8-bit-weather.com/update.html"));
            //         }
            //     } catch (Exception ex) {
            //         ex.printStackTrace();
            //     }
            // });
            // stage.iconifiedProperty().addListener((obs, oldValue, newValue) -> {
            //     if (newValue) {
            //         stage.setIconified(false); // block minimize
            //     }
            // });
            
            // ..________________ adding to containers _______________..//
            container.getChildren().add(closeButton);
            container.getChildren().add(minimizeButton);
            // container.getChildren().add(forecastButton);
            container.getChildren().add(invertButton);
            if(couldUpdateApp)
                container.getChildren().add(updateButton);

        }
    
    private void installButtonStyling(Button button) {
        String baseCol = "#098bd151;";
        String hovrCol = "#7b070776;";
        String clikCol = "#05c33b76;";
        installButtonStyling(button, baseCol, hovrCol, clikCol);
    }

    private void installButtonStyling(
            Button button,
            String baseCol,
            String hovrCol,
            String clikCol) {

        String baseStyle = "-fx-background-color: " + baseCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        String hoverStyle = "-fx-background-color: " + hovrCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        String clickStyle = "-fx-background-color: " + clikCol +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 4;" +
                "-fx-padding: 0;" +
                "-fx-font-size: 12px;" +
                "-fx-alignment: center;";

        button.setStyle(baseStyle);

        button.setOnMousePressed(e -> button.setStyle(clickStyle));
        button.setOnMouseReleased(e -> button.setStyle(baseStyle));
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    private void installDragBehavior(Pane root) {
        // Runnable dragHandler = () -> {
            // stage.setX(stageStartX + (stage.getX() - stageStartX));
            // stage.setY(stageStartY + (stage.getY() - stageStartY));


            // if (stage.getY() + stage.getHeight() > screenHeight) stage.setY(screenHeight - stage.getHeight());{
            //     // root.setLayoutY(screenHeight-root.getBoundsInParent().getHeight());
            //     // verticallyFlipElements();
            // }
        // }
    }
    private Button getUpdateButton(){

        Image image = ImageGrabber.getImage("/images/menu", "updatebutton", ".png");
        ImageView imageView = new ImageView(image);

        // Optionally resize the image
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        imageView.setPreserveRatio(true);

        // Create the button and set the graphic
        Button button = new Button();
        button.setGraphic(imageView);

        return button;

    }
}