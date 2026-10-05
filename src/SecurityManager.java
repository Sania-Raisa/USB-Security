import java.util.ArrayList;
import java.util.List;

public class SecurityManager {

    // List of trusted USB devices
    private List<String> trustedDevices;

    // List of blacklisted USB devices
    private List<String> blacklistedDevices;


    // Constructor
    public SecurityManager() {

        // Create empty lists for trusted and blacklisted devices
        trustedDevices = new ArrayList<>();
        blacklistedDevices = new ArrayList<>();
    }


    // Add a device to the trusted device list
    public void addTrustedDevice(String deviceName) {

        trustedDevices.add(deviceName);

        System.out.println(
            deviceName + " added to trusted devices."
        );
    }


    // Add a device to the blacklist
    public void addBlacklistedDevice(String deviceName) {

        blacklistedDevices.add(deviceName);

        System.out.println(
            deviceName + " added to blacklist."
        );
    }


    // Check the security status of a USB device
    public String checkDevice(String deviceName) {

        // First check whether the device is blacklisted
        if (blacklistedDevices.contains(deviceName)) {

            return "BLACKLISTED";
        }

        // Then check whether the device is trusted
        if (trustedDevices.contains(deviceName)) {

            return "TRUSTED";
        }

        // If it is not in either list, it is unknown
        return "UNKNOWN";
    }


    // Display the security status of a device
    public void showSecurityStatus(String deviceName) {

        System.out.println("Device: " + deviceName);

        System.out.println(
            "Security Status: " + checkDevice(deviceName)
        );
    }


    // Display all trusted devices
    public void showTrustedDevices() {

        System.out.println();
        System.out.println("Trusted Devices");

        for (String device : trustedDevices) {

            System.out.println(device);
        }
    }


    // Display all blacklisted devices
    public void showBlacklistedDevices() {

        System.out.println();
        System.out.println("Blacklisted Devices");

        for (String device : blacklistedDevices) {

            System.out.println(device);
        }
    }


    // Main method for testing the SecurityManager
    public static void main(String[] args) {

        // Create a SecurityManager object
        SecurityManager security = new SecurityManager();


        // Add a trusted USB device
        security.addTrustedDevice("My USB");


        // Add a blacklisted USB device
        security.addBlacklistedDevice("Unknown USB");


        // Check the security status of different devices
        security.showSecurityStatus("My USB");

        security.showSecurityStatus("Unknown USB");

        security.showSecurityStatus("New USB");


        // Display trusted devices
        security.showTrustedDevices();


        // Display blacklisted devices
        security.showBlacklistedDevices();
    }
}