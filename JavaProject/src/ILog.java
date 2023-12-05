public interface ILog {
    void write(String message);
    void stopImmediately();
    void stopAndWait();
}