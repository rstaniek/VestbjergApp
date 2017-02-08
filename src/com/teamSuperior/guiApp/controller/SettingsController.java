package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import com.teamSuperior.guiApp.GUI.Error;
import com.teamSuperior.guiApp.enums.Drawables;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.teamSuperior.guiApp.GUI.Error.displayError;
import static com.teamSuperior.guiApp.GUI.Error.displayMessage;
import static com.teamSuperior.guiApp.enums.ErrorCode.*;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * Created by Domestos on 16.11.30.
 */
public class SettingsController implements Initializable {
    @FXML
    public TextField text_settings_connection_hostname;
    @FXML
    public TextField text_settings_connection_username;
    @FXML
    public PasswordField text_settings_connection_password;
    @FXML
    public Button btn_settings_connection_testConn;
    @FXML
    public Button btn_save;
    @FXML
    public Button btn_saveQuit;
    @FXML
    public Button btn_quit;
    @FXML
    public TextField text_settings_discounts_registered;
    @FXML
    public TextField text_settings_discounts_craftsman;
    @FXML
    public TextField text_settings_discounts_quantity;
    @FXML
    public TextField text_settings_discounts_selfPickUp;
    @FXML
    public TextField text_settings_discounts_maxTreshold;
    @FXML
    public ImageView img_testMeme;
    @FXML
    public Tab tab_discounts;
    @FXML
    public CheckBox checkBox_showNotifications_lowAmountOfProducts;
    @FXML
    public Label label_timeline;
    @FXML
    public Button btn_play;
    @FXML
    public Button btn_stop;
    @FXML
    public Slider slider_timeline;
    private MediaPlayer mediaPlayer;
    private Media media;
    private boolean isPlaying;


    private Preferences registry; //application settings
    private DBConnect conn;
    private Employee loggedUser;

    @FXML
    public void btn_settings_connection_testConn_clicked(ActionEvent actionEvent) {
        if (validateDatabaseCredentials()) {
            if (DBConnect.testConnection(text_settings_connection_hostname.getText(), text_settings_connection_username.getText(), text_settings_connection_password.getText())) {
                Error.displayMessage(INFORMATION, "Connection test finished successfully", String.format("Connection with the server %1$s is established. Now you can log in", text_settings_connection_hostname.getText()));
            } else displayError(CONNECTION_TEST_FAILED);
        }
    }

    private boolean validateDatabaseCredentials() {
        if (text_settings_connection_hostname.getText().isEmpty()) {
            displayError(CONNECTION_HOSTNAME_EMPTY);
            return false;
        } else if (text_settings_connection_username.getText().isEmpty()) {
            displayError(CONNECTION_USERNAME_EMPTY);
            return false;
        } else if (text_settings_connection_password.getText().isEmpty()) {
            displayError(CONNECTION_PASSWORD_EMPTY);
            return false;
        } else return true;
    }

    private boolean validateDiscount(TextField tf) {
        if (tf.getText().isEmpty()) {
            displayError(VALIDATION_FIELD_EMPTY);
            return false;
        } else if (tf.getText().contains("[a-zA-Z]+")) {
            displayError(VALIDATION_ILLEGAL_CHARS);
            return false;
        } else return true;
    }

    private void save() {
        //general
        registry.putBoolean("SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS", checkBox_showNotifications_lowAmountOfProducts.isSelected());

        //connection
        registry.put("DATABASE_HOSTNAME", text_settings_connection_hostname.getText());
        registry.put("DATABASE_USER", text_settings_connection_username.getText());
        registry.put("DATABASE_PASS", text_settings_connection_password.getText());

        //discounts
        registry.putDouble("DISCOUNT_REGISTERED", Double.parseDouble(text_settings_discounts_registered.getText()));
        registry.putDouble("DISCOUNT_CRAFTSMAN", Double.parseDouble(text_settings_discounts_craftsman.getText()));
        registry.putDouble("DISCOUNT_SELF_PICKUP", Double.parseDouble(text_settings_discounts_selfPickUp.getText()));
        registry.putDouble("DISCOUNT_QUANTITY", Double.parseDouble(text_settings_discounts_quantity.getText()));
        registry.putDouble("DISCOUNT_MAX", Double.parseDouble(text_settings_discounts_maxTreshold.getText()));

        conn = new DBConnect();
        try{
            conn.upload(String.format("UPDATE discounts SET value='%1$s' WHERE id=1;", text_settings_discounts_registered.getText()));
            conn.upload(String.format("UPDATE discounts SET value='%1$s' WHERE id=2;", text_settings_discounts_craftsman.getText()));
            conn.upload(String.format("UPDATE discounts SET value='%1$s' WHERE id=3;", text_settings_discounts_selfPickUp.getText()));
            conn.upload(String.format("UPDATE discounts SET value='%1$s' WHERE id=4;", text_settings_discounts_quantity.getText()));
            conn.upload(String.format("UPDATE discounts SET value='%1$s' WHERE id=5;", text_settings_discounts_maxTreshold.getText()));
        } catch (SQLException sqlEx){
            displayMessage(ERROR, "SQL connection error.", sqlEx.getMessage());
        }
    }

    @FXML
    public void btn_save_click(ActionEvent actionEvent) {
        if (validateDiscount(text_settings_discounts_registered) &&
                validateDiscount(text_settings_discounts_craftsman) &&
                validateDiscount(text_settings_discounts_quantity) &&
                validateDiscount(text_settings_discounts_selfPickUp) &&
                validateDiscount(text_settings_discounts_maxTreshold)) {
            System.out.println(Float.parseFloat(text_settings_discounts_registered.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_craftsman.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_selfPickUp.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_quantity.getText()));
            System.out.println(Float.parseFloat(text_settings_discounts_maxTreshold.getText()));
            save();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        img_testMeme.setImage(Drawable.getImage(this.getClass(), Drawables.TEST_MEME));

        registry = Preferences.userRoot();
        loggedUser = UserController.getUser();
        if (!UserController.isLoggedIn()) {
            tab_discounts.setDisable(true);
        } else if (loggedUser.getAccessLevel() < 2) {
            tab_discounts.setDisable(true);
        }

        //general
        checkBox_showNotifications_lowAmountOfProducts.setSelected(registry.getBoolean("SETTINGS_NOTIFICATIONS_SHOW_ON_LOW_PRODUCTS", false));

        //connection
        text_settings_connection_hostname.setText(registry.get("DATABASE_HOSTNAME", ""));
        text_settings_connection_username.setText(registry.get("DATABASE_USER", ""));
        text_settings_connection_password.setText(registry.get("DATABASE_PASS", ""));

        if (UserController.isLoggedIn() && !registry.get("DATABASE_HOSTNAME", "").isEmpty()) {
            conn = new DBConnect();
            ResultSet rs;
            try {
                rs = conn.getFromDataBase("SELECT * FROM discounts");
                rs.next();
                registry.putDouble("DISCOUNT_REGISTERED", rs.getDouble("value"));
                rs.next();
                registry.putDouble("DISCOUNT_CRAFTSMAN", rs.getDouble("value"));
                rs.next();
                registry.putDouble("DISCOUNT_QUANTITY", rs.getDouble("value"));
                rs.next();
                registry.putDouble("DISCOUNT_SELF_PICKUP", rs.getDouble("value"));
                rs.next();
                registry.putDouble("DISCOUNT_MAX", rs.getDouble("value"));

            } catch (Exception ex) {
                displayMessage(ERROR, ex.getMessage());
            }

            //CEO only features
            if (loggedUser.getAccessLevel() < 3) {
                text_settings_discounts_maxTreshold.setDisable(true);
            }
        }


        //discounts
        text_settings_discounts_registered.setText(String.valueOf(registry.getDouble("DISCOUNT_REGISTERED", 0)));
        text_settings_discounts_craftsman.setText(String.valueOf(registry.getDouble("DISCOUNT_CRAFTSMAN", 0)));
        text_settings_discounts_quantity.setText(String.valueOf(registry.getDouble("DISCOUNT_QUANTITY", 0)));
        text_settings_discounts_selfPickUp.setText(String.valueOf(registry.getDouble("DISCOUNT_SELF_PICKUP", 0)));
        text_settings_discounts_maxTreshold.setText(String.valueOf(registry.getDouble("DISCOUNT_MAX", 0)));

        URL url = null;
        try {
            url = new URL("http://remix1436.ct8.pl/resources/Media/Thomas_Jack-Booka_Shake.mp4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String s = "";
        try {
            s = url.toURI().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        media = new Media(s);
        System.out.println(s);
        mediaPlayer = new MediaPlayer(media);
        label_timeline.setText("0:00");
        btn_play.setDisable(true);
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                slider_timeline.setMin(0);
                slider_timeline.setValue(0.0);
                System.err.println("INFO: Media duration init: " + media.getDuration().toSeconds());
                slider_timeline.setMax(media.getDuration().toSeconds());
                btn_play.setDisable(false);
            }
        });
        isPlaying = false;
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                updateValues();
            }
        });
        /*slider_timeline.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (slider_timeline.isValueChanging()) {
                    mediaPlayer.seek(new Duration(slider_timeline.getValue()));
                }
            }
        });*/
        slider_timeline.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(slider_timeline.getValue()));
            }
        });

        mediaPlayer.setOnError(new Runnable() {
            @Override
            public void run() {
                isPlaying = false;
                mediaPlayer.stop();
                slider_timeline.setValue(0.0);
                btn_play.setText("Play");
            }
        });

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                slider_timeline.setValue(0.0);
                isPlaying = false;
                btn_play.setText("Play");
            }
        });
    }

    private void updateValues() {
        if (slider_timeline != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    slider_timeline.setDisable(media.getDuration().isUnknown());
                    Duration current = mediaPlayer.getCurrentTime();
                    if (!slider_timeline.isDisabled() && !slider_timeline.isValueChanging() && media.getDuration().greaterThan(Duration.ZERO)) {
                        slider_timeline.setValue(current.toSeconds());
                    }
                    int mins = (int) current.toMinutes();
                    String secs = String.valueOf((int) current.toSeconds() % 60);
                    if (((int) current.toSeconds() % 60) < 10) {
                        secs = "0" + secs;
                    }
                    label_timeline.setText(String.format("%d:%s", mins, secs));
                }
            });
        }
    }

    @FXML
    public void btn_saveQuit_clicked(ActionEvent actionEvent) {
        save();
        Stage stage = (Stage) btn_saveQuit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_quit_clicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btn_quit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_play_onClick(ActionEvent actionEvent) {
        if (isPlaying) {
            btn_play.setText("Play");
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            btn_play.setText("Pause");
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    @FXML
    public void btn_stop_onClick(ActionEvent actionEvent) {
        mediaPlayer.stop();
        btn_play.setText("Play");
        isPlaying = false;
        slider_timeline.setValue(0.0);
    }
}
