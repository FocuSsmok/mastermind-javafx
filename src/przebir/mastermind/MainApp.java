package przebir.mastermind;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import przebir.mastermind.view.GameBoardController;
import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane gameBoard;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Mastermind");
//        System.out.println("Working Directory = " +
//                System.getProperty("user.dir"));
        showGameBoard();
    }

    public void showGameBoard() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GameBoard.fxml"));
            System.out.println(loader.getLocation());
            gameBoard = (AnchorPane) loader.load();

            GameBoardController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(gameBoard);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
