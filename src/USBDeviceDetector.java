import java.io.BufferedReader;
import java.io.InputStreamReader;

public class USBDeviceDetector {

    // USB devices detect করে String হিসেবে return করবে
    public static String getUSBDevices() {

        StringBuilder result = new StringBuilder();

        try {

            String command =
                    "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_PnPEntity | " +
                    "Where-Object {$_.PNPDeviceID -like 'USB*'} | " +
                    "Select-Object Name,PNPDeviceID,Status | " +
                    "Format-List\"";

            Process process =
                    Runtime.getRuntime().exec(command);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()
                            )
                    );

            String line;
            boolean usbFound = false;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (!line.isEmpty()) {

                    result.append(line);
                    result.append("\n");

                    usbFound = true;
                }
            }

            reader.close();

            process.waitFor();

            if (!usbFound) {
                return "No USB device detected.";
            }

        } catch (Exception e) {

            return "Error detecting USB device.";
        }

        return result.toString();

    }


    // আগের method-টাও রাখছি
    public static void detectUSBDevices() {

        System.out.println("USB Device Detection");
        System.out.println("========================");

        System.out.println(getUSBDevices());

    }


    public static void main(String[] args) {

        detectUSBDevices();

    }
}