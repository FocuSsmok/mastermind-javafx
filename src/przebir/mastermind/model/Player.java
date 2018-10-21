package przebir.mastermind.model;

public class Player {

    private static String name;
    private static int points;

    private Player() {};

    public static void setName(String name) {
        PlayerHolder.PLAYER.name = name;
    }

    public static void setPoints(int points) {
        PlayerHolder.PLAYER.points = points;
    }

    public static String getName() {
        return PlayerHolder.PLAYER.name;
    }

    public static int getPoints() {
        return PlayerHolder.PLAYER.points;
    }

    public static Player getInstance() {
        return PlayerHolder.PLAYER;
    }

    private static class PlayerHolder {
        private static final Player PLAYER = new Player();
    }
}
