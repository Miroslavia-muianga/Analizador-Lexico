package org.analizador;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/analizador/main.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 750);

        // aplica o tema escuro por defeito
        scene.getStylesheets().add(
                getClass().getResource("/org/analizador/dark-theme.css").toExternalForm()
        );

        stage.setTitle("Mini-Pascal Lexer");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
