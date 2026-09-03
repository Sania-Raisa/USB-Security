import java.io.BufferedReader;
import java.io.InputStreamReader;
public class USBDetector {
    public static void detectUSBDevices() {
        try {
            String command = "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_LogicalDisk | " +
                    "Where-Object {$_.DriveType -eq 2} | " +
                    "Select-Object DeviceID,Size,FreeSpace\"";

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            boolean usbFound = false;
            System.out.println("USB Device Detection");
            System.out.println("========================");
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()
                        && !line.startsWith("DeviceID")
                        && !line.startsWith("----------")) {
                    System.out.println(line);
                    usbFound = true;
                }
            }
            if (!usbFound) {
                System.out.println("No USB device detected.");
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Error detecting USB device.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        detectUSBDevices();
    }
}
