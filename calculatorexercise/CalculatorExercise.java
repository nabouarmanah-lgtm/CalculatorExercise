package calculatorexercise;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;

public class CalculatorExercise extends Application {

    // UI Components
    TextField tf1;
    TextField tf2;
    Label resultLabel;
    Label historyLabel;

    // File for history
    File historyFile = new File("history.txt");

    @Override
    public void start(Stage stage) {

        // =========================
        // Number 1
        // =========================
        Label num1Label = new Label("Number 1:");
        num1Label.setFont(new Font("Arial", 14));

        tf1 = new TextField();
        tf1.setPromptText("Enter first number");
        tf1.setPrefWidth(180);

        HBox row1 = new HBox(10, num1Label, tf1);
        row1.setAlignment(Pos.CENTER);

        // =========================
        // Number 2
        // =========================
        Label num2Label = new Label("Number 2:");
        num2Label.setFont(new Font("Arial", 14));

        tf2 = new TextField();
        tf2.setPromptText("Enter second number");
        tf2.setPrefWidth(180);

        HBox row2 = new HBox(10, num2Label, tf2);
        row2.setAlignment(Pos.CENTER);

        // =========================
        // Operator Buttons
        // =========================
        Button addBtn = createButton("+");
        Button subBtn = createButton("-");
        Button mulBtn = createButton("*");
        Button divBtn = createButton("/");

        HBox operatorsRow = new HBox(10, addBtn, subBtn, mulBtn, divBtn);
        operatorsRow.setAlignment(Pos.CENTER);

        // =========================
        // Result Label
        // =========================
        resultLabel = new Label("Result:");
        resultLabel.setFont(new Font("Arial", 14));

        // =========================
        // Clear and History Buttons
        // =========================
        Button clearBtn = new Button("Clear");
        clearBtn.setPrefSize(80, 30);

        Button historyBtn = new Button("History");
        historyBtn.setPrefSize(80, 30);

        HBox actionRow = new HBox(15, clearBtn, historyBtn);
        actionRow.setAlignment(Pos.CENTER);

        // =========================
        // History Label
        // =========================
        historyLabel = new Label("History:");
        historyLabel.setFont(new Font("Arial", 14));
        historyLabel.setWrapText(true);

        // =========================
        // VBox Main Layout
        // =========================
        VBox vBox = new VBox(20, row1, row2, operatorsRow, resultLabel, actionRow, historyLabel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30));

        // =========================
        // BorderPane Root
        // =========================
        BorderPane root = new BorderPane();
        root.setCenter(vBox);

        // =========================
        // Scene + Stage
        // =========================
        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("JavaFX Calculator With History");
        stage.setScene(scene);
        stage.show();

        // =========================
        // Add Button
        // =========================
        addBtn.setOnAction(e -> {
            calculate("+");
        });

        // =========================
        // Subtract Button
        // =========================
        subBtn.setOnAction(e -> {
            calculate("-");
        });

        // =========================
        // Multiply Button
        // =========================
        mulBtn.setOnAction(e -> {
            calculate("*");
        });

        // =========================
        // Divide Button
        // =========================
        divBtn.setOnAction(e -> {
            calculate("/");
        });

        // =========================
        // Clear Button
        // =========================
        clearBtn.setOnAction(e -> {
            tf1.clear();
            tf2.clear();
            resultLabel.setText("Result:");
            historyLabel.setText("History:");
            clearHistoryFile();
        });

        // =========================
        // History Button
        // =========================
        historyBtn.setOnAction(e -> {
            String history = readHistory();

            if (history.isEmpty()) {
                historyLabel.setText("History: No operations yet");
            } else {
                historyLabel.setText("History:\n" + history);
            }
        });
    }

    // =========================
    // Create Operator Button
    // =========================
    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(50, 30);
        btn.setFont(new Font("Arial", 14));
        return btn;
    }

    // =========================
    // Calculate Method
    // =========================
    private void calculate(String operator) {

        String text1 = tf1.getText().trim();
        String text2 = tf2.getText().trim();

        // Check if fields are empty
        if (text1.isEmpty() || text2.isEmpty()) {
            resultLabel.setText("Result: Please enter both numbers");
            return;
        }

        float num1, num2;

        // Check valid input
        try {
            num1 = Float.parseFloat(text1);
            num2 = Float.parseFloat(text2);
        } catch (NumberFormatException e) {
            resultLabel.setText("Result: Invalid input");
            return;
        }

        // Division by zero
        if (operator.equals("/") && num2 == 0) {
            resultLabel.setText("Result: Error");
            return;
        }

        float result = 0;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
        }

        // Show result
        if (result == (int) result) {
            resultLabel.setText("Result: " + (int) result);
        } else {
            resultLabel.setText("Result: " + result);
        }

        // Save operation to history
        saveToHistory(num1, operator, num2, result);
    }

    // =========================
    // Save To History File
    // =========================
    private void saveToHistory(float num1, String op, float num2, float result) {
        try {
            FileWriter writer = new FileWriter(historyFile, true);

            String line;
            if (result == (int) result) {
                line = formatNumber(num1) + " " + op + " " + formatNumber(num2) + " = " + (int) result + "\n";
            } else {
                line = formatNumber(num1) + " " + op + " " + formatNumber(num2) + " = " + result + "\n";
            }

            writer.write(line);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Read History File
    // =========================
    private String readHistory() {
        StringBuilder history = new StringBuilder();

        try {
            if (!historyFile.exists()) {
                return "";
            }

            BufferedReader reader = new BufferedReader(new FileReader(historyFile));
            String line;

            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return history.toString();
    }

    // =========================
    // Clear History File
    // =========================
    private void clearHistoryFile() {
        try {
            FileWriter writer = new FileWriter(historyFile);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Format Numbers
    // =========================
    private String formatNumber(float number) {
        if (number == (int) number) {
            return String.valueOf((int) number);
        }
        return String.valueOf(number);
    }

    public static void main(String[] args) {
        launch();
    }
}