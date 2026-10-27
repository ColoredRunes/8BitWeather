package animator.interfacewindow;

import javafx.scene.control.Button;


/**
 * A factory who diseminates a button for the scene to consume which is formatted properly.
 * ClosableFactory
 */
public class ClosableFactory {

    // private Stage stage;

    public static Button getFormattedCloseButton(Runnable[] rExits,int[] pos) {
        Button closeButton;

        closeButton = new Button("x");

        // Positioning
        closeButton.setLayoutX(pos[0]);
        closeButton.setLayoutY(pos[1]);

        // Styling
        String baseCol = "#08537654;";
        String hovrCol = "#7b070776;";
        String clikCol = "#05c33b76;";

        closeButton.setOnMousePressed(e ->
            closeButton.setStyle("-fx-background-color: " + clikCol +
                                 "-fx-text-fill: white;" +
                                 "-fx-background-radius: 4;" +
                                 "-fx-padding: 4 8;"));

        closeButton.setOnMouseReleased(e ->
            closeButton.setStyle("-fx-background-color: " + baseCol +
                                 "-fx-text-fill: white;" +
                                 "-fx-background-radius: 4;" +
                                 "-fx-padding: 4 8;"));

        closeButton.setOnMouseEntered(e ->
            closeButton.setStyle("-fx-background-color: " + hovrCol +
                                 "-fx-text-fill: white;" +
                                 "-fx-background-radius: 4;" +
                                 "-fx-padding: 4 8;"));

        closeButton.setOnMouseExited(e ->
            closeButton.setStyle("-fx-background-color: " + baseCol +
                                 "-fx-text-fill: white;" +
                                 "-fx-background-radius: 4;" +
                                 "-fx-padding: 4 8;"));

        closeButton.setStyle("-fx-background-color: " + baseCol +
                             "-fx-text-fill: white;" +
                             "-fx-background-radius: 4;" +
                             "-fx-padding: 4 8;");

        // Close action
        closeButton.setOnMouseClicked(event ->{
            for (Runnable runnable : rExits) {
                runnable.run();
            }
        });
        return closeButton;
    }
}
