package przebir.mastermind.view;

import przebir.mastermind.MainApp;
import przebir.mastermind.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogWindowStartController {

    @FXML
    private Button setName;
    @FXML
    private TextField inputName;
    @FXML
    private Label mandatoryLabel;
    private MainApp mainApp;
    private GameBoardController gameBoardController;
    private boolean isClosed;


    public DialogWindowStartController() {
//        constructor
    }

    @FXML
    public void initialize() {
//        initialize controller
        Player.setName("");
        String textTmp = setName.getText();
        setName.setText(textTmp.toUpperCase());
    }

    @FXML
    public void closeDialogWindow() {
        if (!inputName.getText().equals("")) {
            GameBoardController.isDialogWindowClosed = true;
            gameBoardController.initBalls();
            mandatoryLabel.setText("");
            Player.setName(inputName.getText());
            Stage stage = (Stage) setName.getScene().getWindow();
            stage.close();
        } else {
            mandatoryLabel.setText("Wpisz imie!");
        }
    }

    public void setGameBoardController(GameBoardController controller) {
        this.gameBoardController = controller;
    }
}
