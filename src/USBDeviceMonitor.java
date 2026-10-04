import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class USBDeviceMonitor {

    // Currently connected USB devices-এর PNP ID সংগ্রহ করে
    public static Set<String> getUSBDevices() {

        Set<String> devices = new HashSet<>();

        try {

            String command =
                    "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_PnPEntity | " +
                    "Where-Object {$_.PNPDeviceID -like 'USB*'} | " +
                    "ForEach-Object {$_.PNPDeviceID}\"";

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    process.getInputStream()
                            )
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (!line.isEmpty()) {
                    devices.add(line);
                }
            }

            reader.close();

            process.waitFor();

        } catch (Exception e) {

            System.out.println(
                    "Error detecting USB devices."
            );
        }

        return devices;
    }

    // USB device continuously monitor করবে
    public static void monitorUSB() {

        Set<String> previousDevices = getUSBDevices();

        System.out.println("USB Device Monitor");
        System.out.println("========================");
        System.out.println("Monitoring USB devices...");
        System.out.println();

        while (true) {

            try {

                Thread.sleep(2000);

                Set<String> currentDevices = getUSBDevices();

                // নতুন USB device connected
                for (String device : currentDevices) {

                    if (!previousDevices.contains(device)) {

                        System.out.println(
                                "USB Device Connected!"
                        );

                        System.out.println(
                                "Device ID: " + device
                        );

                        ActivityLogger.logActivity(
                                "USB device connected: " + device
                        );

                        System.out.println(
                                "------------------------"
                        );
                    }
                }

                // USB device disconnected
                for (String device : previousDevices) {

                    if (!currentDevices.contains(device)) {

                        System.out.println(
                                "USB Device Disconnected!"
                        );

                        System.out.println(
                                "Device ID: " + device
                        );

                        ActivityLogger.logActivity(
                                "USB device disconnected: " + device
                        );

                        System.out.println(
                                "------------------------"
                        );
                    }
                }

                // Current list save করে রাখবে
                previousDevices = currentDevices;

            } catch (InterruptedException e) {

                System.out.println(
                        "Monitoring stopped."
                );

                break;
            }
        }
    }

    // Program run করার জন্য main method
    public static void main(String[] args) {

        monitorUSB();
    }
}
