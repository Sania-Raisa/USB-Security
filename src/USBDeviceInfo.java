import java.io.BufferedReader;
import java.io.InputStreamReader;

public class USBDeviceInfo {

    // USB device-এর information রাখার variables
    private String name;
    private String deviceID;
    private String status;

    // Constructor
    public USBDeviceInfo(String name, String deviceID, String status) {

        this.name = name;
        this.deviceID = deviceID;
        this.status = status;
    }

    // Device information দেখানোর method
    public void displayInfo() {

        System.out.println("================================");
        System.out.println("       USB DEVICE INFORMATION");
        System.out.println("================================");

        System.out.println("Name      : " + name);
        System.out.println("Device ID : " + deviceID);
        System.out.println("Status    : " + status);

        System.out.println("================================");
    }

    public static void main(String[] args) {

        try {

            // Windows PowerShell command
            // এটি computer-এর USB devices খুঁজবে
            String command =
                    "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_PnPEntity | " +
                    "Where-Object {$_.PNPDeviceID -like 'USB*'} | " +
                    "Select-Object Name,PNPDeviceID,Status\"";

            // PowerShell command চালানো
            Process process = Runtime.getRuntime().exec(command);

            // PowerShell-এর output পড়া
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

            String line;

            System.out.println("Checking USB devices...\n");

            boolean found = false;

            // প্রতিটি USB device-এর information পড়া
            while ((line = reader.readLine()) != null) {

                if (!line.trim().isEmpty()) {

                    System.out.println(line);

                    found = true;
                }
            }

            // কোনো USB device না পাওয়া গেলে
            if (!found) {

                System.out.println("No USB device found.");
            }

            reader.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}