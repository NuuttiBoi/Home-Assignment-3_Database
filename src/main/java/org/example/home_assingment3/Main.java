package org.example.home_assingment3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private Label distanceLabel;
    private Label fuelLabel;
    private Button saveButton;
    private Label resultLabel;
    private Label errorLabel;
    private ComboBox<String> languageSelector;

    private ResourceBundle bundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setLanguage("en");

        distanceLabel = new Label();
        fuelLabel = new Label();
        saveButton = new Button();
        resultLabel = new Label();
        errorLabel = new Label();
        languageSelector = new ComboBox<>();

        languageSelector.getItems().addAll("English", "Farsi", "Japanese", "French");
        languageSelector.setValue("English");

        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();
            switch (selectedLanguage) {
                case "Farsi":
                    setLanguage("fa");
                    break;
                case "Japanese":
                    setLanguage("ja");
                    break;
                case "French":
                    setLanguage("fr");
                    break;
                default:
                    setLanguage("en");
                    break;
            }
            updateLabels();
        });



        TextField distanceField = new TextField();
        TextField fuelField = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(languageSelector, 0, 0, 2, 1);
        gridPane.add(distanceLabel, 0, 1);
        gridPane.add(distanceField, 1, 1);
        gridPane.add(fuelLabel, 0, 2);
        gridPane.add(fuelField, 1, 2);
        gridPane.add(saveButton, 0, 3, 2, 1);
        gridPane.add(resultLabel, 0,4,2,1);
        gridPane.add(errorLabel, 0,5,2,1);

        updateLabels();

        Scene scene = new Scene(gridPane, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nuutti Turunen");
        primaryStage.show();

        saveButton.setOnAction(event -> {
            try {
                double firstName = Double.parseDouble(distanceField.getText());
                double lastName = Double.parseDouble(fuelField.getText());

                ConsumptionRecord consumptionRecord = new ConsumptionRecord(firstName, lastName);

                System.out.println(consumptionRecord.calculateConsumption());
                resultLabel.setText(String.valueOf(consumptionRecord.calculateConsumption()));
            } catch (Exception e){
                e.printStackTrace();
                errorLabel.setText(bundle.getString("errorLabel"));
            }

            String selectedLanguage = languageSelector.getValue();

            String languageCode;
            switch (selectedLanguage) {
                case "Farsi":
                    languageCode = "fa";
                    break;
                case "Japanese":
                    languageCode = "ja";
                    break;
                case "French":
                    languageCode= "fr";
                    break;
                default:
                    languageCode = "en";
                    break;
            }

            //saveUser(firstName, lastName, email, languageCode);
        });
    }

    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("message", locale);
    }

    private void updateLabels() {
        distanceLabel.setText(bundle.getString("label.firstName"));
        fuelLabel.setText(bundle.getString("label.lastName"));
        saveButton.setText(bundle.getString("button.save"));
        resultLabel.setText(bundle.getString("resultLabel"));
    }
    private void saveUser(String firstName, String lastName, String email, String languageCode) {
        String insertEmployeeSQL = "INSERT INTO employee (email) VALUES (?)";
        String insertTranslationSQL = "INSERT INTO employee_translations (employee_id, language_code, first_name, last_name) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement employeeStmt = connection.prepareStatement(insertEmployeeSQL, Statement.RETURN_GENERATED_KEYS);
            employeeStmt.setString(1, email);
            employeeStmt.executeUpdate();

            ResultSet generatedKeys = employeeStmt.getGeneratedKeys();
            int employeeId = 0;
            if (generatedKeys.next()) {
                employeeId = generatedKeys.getInt(1);
            }

            PreparedStatement translationStmt = connection.prepareStatement(insertTranslationSQL);
            translationStmt.setInt(1, employeeId);
            translationStmt.setString(2, languageCode);
            translationStmt.setString(3, firstName);
            translationStmt.setString(4, lastName);
            translationStmt.executeUpdate();

            System.out.println("User saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
