package przebir.mastermind.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Ranking {

    private Map<String, Integer> players;

    public Ranking() {
        players = new TreeMap<>();
    };

    public void viewRanking() {
        try (BufferedReader br = new BufferedReader(new FileReader("ranking.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {

        }
    }
}
