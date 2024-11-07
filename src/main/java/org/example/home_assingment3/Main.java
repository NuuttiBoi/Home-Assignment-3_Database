package org.example.home_assingment3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private Label firstNameLabel;
    private Label lastNameLabel;
    private Button saveButton;
    private ComboBox<String> languageSelector;

    private ResourceBundle bundle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setLanguage("en");

        firstNameLabel = new Label();
        lastNameLabel = new Label();
        saveButton = new Button();
        languageSelector = new ComboBox<>();

        languageSelector.getItems().addAll("English", "Farsi", "Japanese");
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
                default:
                    setLanguage("en");
                    break;
            }
            updateLabels();
        });



        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(languageSelector, 0, 0, 2, 1);
        gridPane.add(firstNameLabel, 0, 1);
        gridPane.add(firstNameField, 1, 1);
        gridPane.add(lastNameLabel, 0, 2);
        gridPane.add(lastNameField, 1, 2);
        gridPane.add(saveButton, 0, 3, 2, 1);

        updateLabels();

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multilingual UI Example");
        primaryStage.show();

        saveButton.setOnAction(event -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = "example@example.com"; // Replace with email input field if added
            String selectedLanguage = languageSelector.getValue();

            String languageCode;
            switch (selectedLanguage) {
                case "Farsi":
                    languageCode = "fa";
                    break;
                case "Japanese":
                    languageCode = "ja";
                    break;
                default:
                    languageCode = "en";
                    break;
            }

            saveUser(firstName, lastName, email, languageCode);
        });
    }

    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("message", locale);
    }

    private void updateLabels() {
        firstNameLabel.setText(bundle.getString("label.firstName"));
        lastNameLabel.setText(bundle.getString("label.lastName"));
        saveButton.setText(bundle.getString("button.save"));
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
