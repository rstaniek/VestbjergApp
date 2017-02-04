package com.teamSuperior.guiApp.GUI;

import com.teamSuperior.guiApp.enums.Drawables;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Created by rajmu on 17.02.03.
 */
public class SplashScreen {

    private static Style style;
    private static ImageView imgView;

    public static void viewSplashScreen(int durationMillis, Style styleOpen, Style styleClose) {
        Stage window = new Stage();

        style = styleClose;

        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.TRANSPARENT);
        window.setMinWidth(600);
        window.setMinHeight(400);

        ImageView img = new ImageView();
        img.setImage(new Image(Drawables.SPLASH.getPath()));
        imgView = img;

        VBox layout = new VBox();
        layout.getChildren().addAll(img);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        scene.setFill(null);
        window.setScene(scene);
        window.setResizable(false);
        window.setAlwaysOnTop(true);

        Task waitForTime = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(durationMillis);

                Task fadeOut = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        fadeOut();
                        return null;
                    }
                };

                Thread th = new Thread(fadeOut);
                th.setDaemon(true);
                th.start();

                if (styleClose == Style.FADE) Thread.sleep(1000);

                Platform.runLater(window::close);
                return null;
            }
        };

        Thread wait = new Thread(waitForTime);
        wait.setDaemon(true);
        wait.start();
        if (styleOpen == Style.FADE) {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), img);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        window.showAndWait();
    }

    private static void fadeOut() {
        if (style == Style.FADE) {
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), imgView);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.play();
        }
    }

    public enum Style {
        FADE,
        SOLID
    }
}
