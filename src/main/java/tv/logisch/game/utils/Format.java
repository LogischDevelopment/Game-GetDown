package tv.logisch.game.utils;

public class Format {

    public static String time(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (sec > 0 || sb.isEmpty()) {
            sb.append(sec).append("s");
        }
        return sb.toString().trim();
    }

}
