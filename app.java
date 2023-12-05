public class Application {
    public static void main(String[] args) {
        ILog logger = new LogComponent("log.txt");
        logger.write("Log message 1");
        logger.write("Log message 2");
        logger.stopAndWait();
    }
}