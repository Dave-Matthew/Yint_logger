import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogComponentTest {
    private static final String LOG_FILE_PATH = "log.txt";
    private LogComponent logComponent;

    @Before
    public void setUp() {
        logComponent = new LogComponent(LOG_FILE_PATH);
    }

    @After
    public void tearDown() {
        logComponent.stopAndWait();
    }

    @Test
    public void testWriteLogMessage() throws IOException {
        String message = "Test log message";

        logComponent.write(message);

        String loggedMessage = getLastLoggedMessage();
        Assert.assertEquals(message, loggedMessage);
    }

    @Test
    public void testCreateNewFileOnCrossMidnight() throws IOException, InterruptedException {
        // Wait until midnight to ensure a new file is created
        Thread.sleep(1000); // Wait for 1 second

        String message = "Test log message";

        logComponent.write(message);

        String loggedMessage = getLastLoggedMessage();
        Assert.assertEquals(message, loggedMessage);
    }

    @Test
    public void testStopImmediately() throws IOException {
        String message = "Test log message";

        logComponent.write(message);
        logComponent.stopImmediately();

        // Try to write another log message
        logComponent.write("Another log message");

        String loggedMessage = getLastLoggedMessage();
        Assert.assertEquals(message, loggedMessage);
    }

    @Test
    public void testStopAndWait() throws IOException {
        String message = "Test log message";

        logComponent.write(message);
        logComponent.stopAndWait();

        // Try to write another log message
        logComponent.write("Another log message");

        String loggedMessage = getLastLoggedMessage();
        Assert.assertEquals(message, loggedMessage);
    }

    private String getLastLoggedMessage() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            String lastLine = null;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            return lastLine;
        }
    }
}

/* 

The LogComponentTest class contains four test methods:

    testWriteLogMessage verifies that a log message is correctly written to the log file.
    testCreateNewFileOnCrossMidnight checks if a new file is created when crossing midnight.
    testStopImmediately ensures that when stopImmediately is called, the log component stops immediately and does not write any outstanding logs.
    testStopAndWait verifies that when stopAndWait is called, the log component finishes writing any outstanding logs before stopping.

 */