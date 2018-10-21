package przebir.mastermind.view;

import przebir.mastermind.MainApp;
import przebir.mastermind.model.Colors;
import przebir.mastermind.model.FileHandle;
import przebir.mastermind.model.Player;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBoardController {
    @FXML
    private GridPane gridChoice, winnersColors;
    @FXML
    private Button finishBtn, startBtn, checkBtn;
    @FXML
    private HBox guessRow1, guessRow2, guessRow3, guessRow4, guessRow5, guessRow6, guessRow7, guessRow8, guessRow9;
    @FXML
    private Text resultText;

    private GameBoardController that;
    private HBox[] guessRows;
    private ObservableList<Node> buttons;
    private String activeColor;
    private MainApp mainApp;
    private Colors modelColors;
    private HBox activeRow;
    private String winColors[], guessRowColors[];
    private int counterGuess, yourPoints;
    public static boolean isDialogWindowClosed;


    public GameBoardController() {
        //constructor
    }

    @FXML
    public void initialize() {
        //initialize controller
//        System.out.println(gridChoice.getChildren());
        that = this;
        resultText.setText("");
        GameBoardController.isDialogWindowClosed = false;
        counterGuess = 0;
        yourPoints = 0;
        Player.setPoints(0);
        guessRows = new HBox[]{
                guessRow1, guessRow2, guessRow3, guessRow4, guessRow5, guessRow6, guessRow7, guessRow8, guessRow9
        };
        guessRowColors = new String[4];
        modelColors = new Colors();
        winColors = modelColors.getWinColors();
        setDisable();
        finishBtn.setText("Zakończ");
    }

    @FXML
    public void startGame() {
        startBtn.setDisable(true);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(GameBoardController.class.getClassLoader().getResource("view/DialogWindowStart.fxml"));
//            fxmlLoader.setLocation(getClass().getResource("./DialogWindowStart.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Dialog Window");
            stage.setResizable(false);
            stage.setOnCloseRequest(e -> e.consume());
            stage.setScene(scene);
            stage.show();
            DialogWindowStartController dialogWindowStartController = fxmlLoader.getController();
            dialogWindowStartController.setGameBoardController(that);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new window", e);
        }
    }

    @FXML
    public void finishGame() {
        System.out.println("Finished game");
        startBtn.setDisable(false);
        checkBtn.setDisable(true);
        finishBtn.setDisable(true);
        activeColor = "";

        for (int i = 0; i < guessRows.length; i++) {
            GridPane guessRow = (GridPane) guessRows[i].getChildren().get(0);
            GridPane hintRow = (GridPane) guessRows[i].getChildren().get(1);
            clearColor(guessRow);
            clearColor(hintRow);
            if (i < 4) {
                winnersColors.getChildren().get(i).setStyle("-fx-background-color: lightgray");
            }
        }

        initialize();
    }

    @FXML
    public void choiceColor(Event e) {
        Button btn = (Button) e.getSource();
        Color c = (Color) btn.getBackground().getFills().get(0).getFill();
        activeColor = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
    }

    @FXML
    public void changeColor(Event e) {
        Button btn = (Button) e.getSource();
        if (activeColor != null) {
            btn.setStyle("-fx-background-color: " + activeColor);
        }
    }

    @FXML
    public void checkRow(Event e) {
        boolean finishedGuesses = false;
        if (counterGuess <= 8) {
            yourPoints++;
            GridPane guessRow = (GridPane) guessRows[counterGuess].getChildren().get(0);
            GridPane hintRow = (GridPane) guessRows[counterGuess].getChildren().get(1);
            ArrayList<String> guessColors = new ArrayList<String>();
            String[] winnersColors = new String[4];
            for (int i = 0; i < winnersColors.length; i++) {
                winnersColors[i] = winColors[i];
            }
            int[] result = new int[4];

            guessRow.getChildren().forEach((btn) -> {
                Button btnCopy = (Button) btn;
                Color c = (Color) btnCopy.getBackground().getFills().get(0).getFill();
                String hashColor = String.format("#%02X%02X%02X",
                        (int) (c.getRed() * 255),
                        (int) (c.getGreen() * 255),
                        (int) (c.getBlue() * 255));
                guessColors.add(hashColor);
            });

            for (int i = 0; i < guessColors.size(); i++) {
                if (winnersColors[i].equals(guessColors.get(i))) {
                    result[i] = 1;
                    winnersColors[i] = null;
                } else {
                    result[i] = 3;
                }
            }

            for (int i = 0; i < guessColors.size(); i++) {
                if (result[i] != 1 && result[i] != 2) {
                    for (int j = 0; j < guessColors.size(); j++) {
                        if (guessColors.get(i).equals(winnersColors[j])) {
                            result[i] = 2;
                            winnersColors[j] = null;
                            break;
                        }
                    }
                }
            }

            /*for (int i = 0; i < result.length; i++) {
                System.out.println(result[i]);
            }
            System.out.println("next:"); */
            Arrays.sort(result);
            configHintRow(counterGuess, hintRow, result);
            if (checkWin(result)) {
                viewWinColors();
                finishBtn.setText("Restart");
                checkBtn.setDisable(true);
                Player.setPoints(yourPoints);
                FileHandle fileHandle = new FileHandle(Player.getPoints(), Player.getName());
                fileHandle.save();
//                System.out.println("Your result: " + yourPoints);
                resultText.setText("Twoj Nick: " + Player.getName() + "\n" + "Twoj wynik: " + Player.getPoints());
                return;
            }
            if (yourPoints == 9) {
                finishedGuesses = true;
            }
            counterGuess += 1;
            if (counterGuess != 9) {
                changeDisable(counterGuess);
            }
        }
        if (yourPoints == 9 && finishedGuesses) {
            resultText.setText("Przegrales! Wcisnij RESTART i sprobuj ponownie!");
            finishBtn.setText("Restart");
            checkBtn.setDisable(true);
            viewWinColors();
        }
    }

//    @FXML
//    public void viewRanking(Event e) {
//        Ranking rank = new Ranking();
//        rank.viewRanking();
//    }

    public void clearColor(GridPane row) {
        row.getChildren().forEach(btn -> {
            btn.setStyle("-fx-background-color: lightgray");
        });
    }

    public boolean checkWin(int[] result) {
        int countWin = 0;
        for (int i : result) {
            if (i == 1) {
                countWin++;
            }
        }
        if (countWin == 4) {
            return true;
        } else {
            return false;
        }
    }

    public void configHintRow(int row, GridPane rowHint, int[] yourHitted) {
        ArrayList<Button> hitted = new ArrayList<>();

        rowHint.getChildren().forEach((hint) -> {
            hitted.add((Button) hint);
        });

        for (int i = 0; i < hitted.size(); i++) {
            if (yourHitted[i] == 1) {
                hitted.get(i).setStyle("-fx-background-color: #000000");
            } else if (yourHitted[i] == 2) {
                hitted.get(i).setStyle("-fx-background-color: #586F7C");
            }
        }
    }

    public void changeDisable(int index) {
        activeRow = guessRows[index];
        GridPane row = (GridPane) activeRow.getChildren().get(0);
        row.getChildren().forEach(btn -> {
            btn.setDisable(false);
        });
        if (index > 0) {
            for (int i = index - 1; i >= 0; i--) {
                GridPane rowToDisable = (GridPane) guessRows[i].getChildren().get(0);
                rowToDisable.getChildren().forEach(btn -> {
                    btn.setDisable(true);
                });
            }
        }
    }

    public void setDisable() {
        buttons = gridChoice.getChildren();
        buttons.forEach((btn) -> {
            btn.setDisable(true);
        });
        finishBtn.setDisable(true);
        checkBtn.setDisable(true);

        for (int i = 0; i < guessRows.length; i++) {
            GridPane activeRow = (GridPane) guessRows[i].getChildren().get(0);
            activeRow.getChildren().forEach(btn -> {
                btn.setDisable(true);
            });
        }
    }

    public void initBalls() {
        startBtn.setDisable(true);
        finishBtn.setDisable(false);
        checkBtn.setDisable(false);
        buttons.forEach(btn -> {
            btn.setDisable(false);
        });

        changeDisable(counterGuess);
    }

    public void viewWinColors() {
        for (int i = 0; i < winColors.length; i++) {
            winnersColors.getChildren().get(i).setStyle("-fx-background-color: " + winColors[i]);
        }
    }

    public void setMainApp(MainApp main) {
        this.mainApp = main;
    }
}
