import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class ActivityLogger {

    // Log file name
    private static final String LOG_FILE = "activity_log.txt";

    // This method saves an activity in the log file
    public static void logActivity(String activity) {

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Set date and time format
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String time = now.format(formatter);

        try {

            // true means new log will be added
            FileWriter writer = new FileWriter(LOG_FILE, true);

            // Write date, time and activity
            writer.write("[" + time + "] " + activity + "\n");

            // Close the file
            writer.close();

            // Show alert popup
            JOptionPane.showMessageDialog(
                    null,
                    "USB Security Alert!\n\n"
                    + activity
                    + "\n\nPlease check this device.",
                    "USB Security Alert",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (IOException e) {

            // Show error if log file cannot be written
            System.out.println("Error writing activity log.");
        }
    }


    // Test the ActivityLogger separately
    public static void main(String[] args) {

        logActivity("Unknown USB device connected.");

        logActivity("USB device disconnected.");

        System.out.println("Activity logged successfully.");
    }
}