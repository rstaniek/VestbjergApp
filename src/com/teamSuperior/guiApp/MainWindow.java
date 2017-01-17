package com.teamSuperior.guiApp;

import com.teamSuperior.core.connection.DBConnect;
import com.teamSuperior.core.model.entity.Employee;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static java.lang.Math.round;

/**
 * Vestbjerg App GUI Version
 *
 * @author Team Superiør
 */
public class MainWindow extends Application {
    private Stage window;
    @FXML
    public LineChart<Number, Number> efficiency;
    private DBConnect conn;
    private ArrayList<Double> empEfficiency;
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("layout/mainWindow.fxml"));
        primaryStage.setTitle("Team Superior - representing Silvan Inc.");
        primaryStage.setResizable(false);
        loadEfficiencyChart();
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void closeProgram() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Confirm Exit");
        a.setContentText("There might be unsaved changes. Are you sure you want to close the application?");
        Button exitButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        Optional<ButtonType> closeResponse = a.showAndWait();
        if (closeResponse.isPresent()) {
            if (ButtonType.OK.equals(closeResponse.get())) {
                window.close();
            }
        }
    }

    private void loadEfficiencyChart(){
        NumberAxis xAxis = new NumberAxis(0, 110, 10);
        NumberAxis yAxis = new NumberAxis(0, 10000, 500);
        efficiency = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        conn = new DBConnect();
        empEfficiency = new ArrayList();
        try
        {
            ResultSet rs = conn.getFromDataBase("SELECT * FROM employees");
            while(rs.next())
            {
                int sales = rs.getInt("numberOfSales");
                double revenue = rs.getDouble("totalRevenue");
                double eff = revenue/sales;
                empEfficiency.add(eff);
            }
            Collections.sort(empEfficiency);
            for(int i = 0; i < empEfficiency.size(); i++)
            {
                series.getData().add(new XYChart.Data(empEfficiency.get(i), i));
                System.out.println(empEfficiency.get(i));
            }
        }
        catch(SQLException ex)
        {
            System.out.println("err");
        }
        efficiency.getData().add(series);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
