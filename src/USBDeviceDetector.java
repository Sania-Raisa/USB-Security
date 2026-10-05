import java.io.BufferedReader;
import java.io.InputStreamReader;
public class USBDeviceDetector {
    // Computer-এর USB devices detect করার method
    public static void detectUSBDevices() {
        try {

            // Windows PowerShell command
            // USB connected devices-এর Name, Device ID এবং Status নেয়
            String command =
                    "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_PnPEntity | " +
                    "Where-Object {$_.PNPDeviceID -like 'USB*'} | " +
                    "Select-Object Name,PNPDeviceID,Status\"";

            // PowerShell command চালানো
            Process process = Runtime.getRuntime().exec(command);

            // Command-এর output পড়া
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

            String line;
            boolean usbFound = false;

            System.out.println("USB Device Detection");
            System.out.println("========================");

            // প্রতিটি USB device-এর information পড়া
            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (!line.isEmpty()) {

                    System.out.println(line);

                    usbFound = true;
                }
            }

            // কোনো USB device না পাওয়া গেলে
            if (!usbFound) {

                System.out.println("No USB device detected.");
            }

            reader.close();

            process.waitFor();

        } catch (Exception e) {

            System.out.println("Error detecting USB device.");
            e.printStackTrace();
        }
    }
    // Program আলাদাভাবে Run করার জন্য main method
   
    public static void main(String[] args) {

        detectUSBDevices();
    }
}