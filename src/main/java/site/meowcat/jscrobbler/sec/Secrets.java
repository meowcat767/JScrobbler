package site.meowcat.jscrobbler.sec;

public class Secrets {
    public static final String API_KEY = "efc7553962e734a4eac0d9d193ff3077";

    public static String getApiSecret() {
        return decode(new int[]{57, 99, 55, 53, 48, 55, 50, 49, 48, 98, 57, 97, 100, 54, 57, 102, 50, 102, 100, 54, 49, 102, 56, 97, 102, 98, 97, 99, 53, 54, 50, 51});
    }

    private static String decode(int[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i : data) {
            sb.append((char) i);
        }
        return sb.toString();
    }
}
