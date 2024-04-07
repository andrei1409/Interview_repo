package constants;

public class Constants {
    public static class RunVariable {
        public static String server = Servers.OFFICIAL_JOKE_URL;
        public static String path = Path.OFFICIAL_JOKE_PATH;
    }

    public static class Servers {
        public static String OFFICIAL_JOKE_URL = "https://official-joke-api.appspot.com/";
    }

    public static class Path {
        public static String OFFICIAL_JOKE_PATH = "jokes/";
    }
}
