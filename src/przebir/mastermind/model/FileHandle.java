package przebir.mastermind.model;

import java.io.*;

public class FileHandle {
    private int points;
    private String name;

    public FileHandle(int points, String name) {
        this.points = points;
        this.name = name;
    }

    public void save() {
        System.out.println("File save");

        try(FileWriter fw = new FileWriter("ranking.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {

            String tekst = "Name: " + this.name + " Points: " + this.points;
            out.println(tekst);

        } catch (IOException e) {
            e.getMessage();
        }
    }
}
