package engine;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ImageGrabber;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class SystemFxTray extends Application {
    MenuItem exitItem;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Keep JavaFX running even when the window is hidden
        Platform.setImplicitExit(false);

        setupSystemTray(primaryStage);
    }

    private void setupSystemTray(Stage stage) throws AWTException {
        // Hide the window automatically when the user clicks away
        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                stage.hide();
            }
        });
        if (!SystemTray.isSupported())
            return;

        SystemTray tray = SystemTray.getSystemTray();
        Image image = ImageGrabber.getImageAwt("/images", "icon", ".png");

        // 1. Create the AWT PopupMenu
        PopupMenu popup = new PopupMenu();

        // 2. Create MenuItems
        MenuItem openItem = new MenuItem("Show Window");
        // MenuItem settingsItem = new MenuItem("Settings");
        exitItem = new MenuItem("Exit");

        // 3. Add click actions to each item
        openItem.addActionListener(e -> Platform.runLater(() -> Position.setPos(stage)));

        // settingsItem.addActionListener(e -> Platform.runLater(() -> {
        // // Trigger your settings view or window here
        // showBottomRight(stage);
        // }));

        exitItem.addActionListener(e -> {
            tray.remove(tray.getTrayIcons()[0]); // Remove icon cleanly
            Platform.exit(); // Shut down JavaFX
            System.exit(0); // Terminate JVM
        });

        // 4. Assemble the menu (with a separator line before Exit)
        popup.add(openItem);
        // popup.add(settingsItem);
        popup.addSeparator();
        popup.add(exitItem);

        // 5. Attach the PopupMenu to the TrayIcon
        TrayIcon trayIcon = new TrayIcon(image, "My JavaFX App", popup);
        trayIcon.setImageAutoSize(true);

        // Keep left-click toggle behavior
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Platform.runLater(() -> {
                        if (stage.isShowing()) {
                            stage.hide();
                        } else {
                            Position.setPos(stage);
                        }
                    });
                }
            }
        });

        tray.add(trayIcon);
        
    }

    public void addShutDownListener(Runnable r) {
        exitItem.addActionListener(e -> {
            r.run();
        });

    }

}
