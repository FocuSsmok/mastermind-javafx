package przebir.mastermind.model;

public class Colors {
    private String[] colors = {
            "#FF0000", "#0000FF", "#008000", "#FFFF00", "#800080", "#FFA500", "#F08080", "#98FB98"
    };
//    private String[] winColors;
    public Colors() {
//        winColors = new String[4];
    }

    public String[] getWinColors() {

        String winColors[] = new String[4];

        for (int i = 0; i < winColors.length; i++) {
            winColors[i] = colors[(int) (Math.random() * colors.length)];
        }

        return winColors;
    }
}
