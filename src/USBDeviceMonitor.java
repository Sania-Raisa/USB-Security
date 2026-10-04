import java.io.BufferedReader;
import java.io.InputStreamReader;

public class USBDeviceMonitor {

    // Get currently connected USB devices
    public static String getUSBDevices() {

        StringBuilder usbDevices = new StringBuilder();

        try {

            String command =
                    "powershell.exe -Command " +
                    "\"Get-CimInstance Win32_PnPEntity | " +
                    "Where-Object {$_.PNPDeviceID -like 'USB*'} | " +
                    "Select-Object Name,PNPDeviceID\"";

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (!line.isEmpty()
                        && !line.startsWith("Name")
                        && !line.startsWith("----")) {

                    usbDevices.append(line).append("\n");
                }
            }

            process.waitFor();

        } catch (Exception e) {

            System.out.println("Error detecting USB devices.");
        }

        return usbDevices.toString().trim();
    }

    // Continuously monitor USB devices
    public static void monitorUSB() {

        String previousUSB = getUSBDevices();

        System.out.println("USB Device Monitor");
        System.out.println("========================");
        System.out.println("Monitoring USB devices...");
        System.out.println();

        while (true) {

            try {

                Thread.sleep(2000);

                String currentUSB = getUSBDevices();

                // USB device connected
                if (!currentUSB.equals(previousUSB)) {

                    if (previousUSB.isEmpty() && !currentUSB.isEmpty()) {

                        System.out.println("USB Device Connected!");
                        System.out.println(currentUSB);
                    }

                    // USB device removed
                    else if (!previousUSB.isEmpty() && currentUSB.isEmpty()) {

                        System.out.println("USB Device Disconnected!");
                    }

                    // USB device list changed
                    else {

                        System.out.println("USB Device List Changed!");
                        System.out.println(currentUSB);
                    }

                    System.out.println("------------------------");

                    previousUSB = currentUSB;
                }

            } catch (InterruptedException e) {

                System.out.println("Monitoring stopped.");
                break;
            }
        }
    }
}

    public static void main(String[] args) {
    System.out.println("Detected USB Devices:");
    System.out.println("========================");
    System.out.println(getUSBDevices());
}