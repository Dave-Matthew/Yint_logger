import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public interface ILog {
    void write(String message);
    void stopImmediately();
    void stopAndWait();
}

public class LogComponent implements ILog {
    private final String logFilePath;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timestampFormatter;
    private final BlockingQueue<String> logQueue;
    private final Thread writerThread;
    private volatile boolean isRunning;

    public LogComponent(String logFilePath) {
        this.logFilePath = logFilePath;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        this.logQueue = new LinkedBlockingQueue<>();
        this.isRunning = true;

        this.writerThread = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                while (isRunning) {
                    String logMessage = logQueue.take(); // Blocks until a log message is available
                    writer.write(logMessage);
                    writer.newLine();
                    writer.flush();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        writerThread.start();
    }

    @Override
    public void write(String message) {
        logQueue.offer(getFormattedTimestamp() + " " + message);
    }

    @Override
    public void stopImmediately() {
        isRunning = false;
        writerThread.interrupt();
    }

    @Override
    public void stopAndWait() {
        isRunning = false;
        try {
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getFormattedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        if (now.toLocalDate().equals(LocalDate.now())) {
            return now.format(timestampFormatter);
        } else {
            return now.format(dateFormatter);
        }
    }
}
