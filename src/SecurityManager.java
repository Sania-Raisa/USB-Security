import java.util.ArrayList;
import java.util.List;

public class SecurityManager {

    // List of trusted USB device IDs
    private List<String> trustedDevices;

    // List of blacklisted USB device IDs
    private List<String> blacklistedDevices;

    // Constructor
    public SecurityManager() {

        trustedDevices = new ArrayList<>();
        blacklistedDevices = new ArrayList<>();
    }

    // Add a device to the trusted list
    public void addTrustedDevice(String deviceID) {

        if (deviceID == null || deviceID.trim().isEmpty()) {
            return;
        }

        if (!trustedDevices.contains(deviceID)) {

            trustedDevices.add(deviceID);

            System.out.println(
                    deviceID + " added to trusted devices."
            );
        }
    }

    // Add a device to the blacklist
    public void addBlacklistedDevice(String deviceID) {

        if (deviceID == null || deviceID.trim().isEmpty()) {
            return;
        }

        if (!blacklistedDevices.contains(deviceID)) {

            blacklistedDevices.add(deviceID);

            System.out.println(
                    deviceID + " added to blacklist."
            );
        }
    }

    // Remove a device from the trusted list
    public void removeTrustedDevice(String deviceID) {

        trustedDevices.remove(deviceID);
    }

    // Remove a device from the blacklist
    public void removeBlacklistedDevice(String deviceID) {

        blacklistedDevices.remove(deviceID);
    }

    // Check the security status of a USB device
    public String checkDevice(String deviceID) {

        if (deviceID == null || deviceID.trim().isEmpty()) {
            return "UNKNOWN";
        }

        // Blacklist has higher priority
        if (blacklistedDevices.contains(deviceID)) {
            return "Flag as UNTRUSTED";
        }

        // Check trusted devices
        if (trustedDevices.contains(deviceID)) {
            return "TRUSTED";
        }

        // Device is not in either list
        return "UNKNOWN";
    }

    // Check whether a device is trusted
    public boolean isTrusted(String deviceID) {

        return trustedDevices.contains(deviceID);
    }

    // Check whether a device is blacklisted
    public boolean isBlacklisted(String deviceID) {

        return blacklistedDevices.contains(deviceID);
    }

    // Get number of trusted devices
    public int getTrustedDeviceCount() {

        return trustedDevices.size();
    }

    // Get number of blacklisted devices
    public int getBlacklistedDeviceCount() {

        return blacklistedDevices.size();
    }

    // Display security status
    public void showSecurityStatus(String deviceID) {

        System.out.println(
                "Device ID: " + deviceID
        );

        System.out.println(
                "Security Status: "
                        + checkDevice(deviceID)
        );
    }

    // Display all trusted devices
    public void showTrustedDevices() {

        System.out.println();
        System.out.println("Trusted Devices");

        if (trustedDevices.isEmpty()) {

            System.out.println(
                    "No trusted devices."
            );

            return;
        }

        for (String device : trustedDevices) {

            System.out.println(device);
        }
    }

    // Display all blacklisted devices
    public void showBlacklistedDevices() {

        System.out.println();
        System.out.println("Blacklisted Devices");

        if (blacklistedDevices.isEmpty()) {

            System.out.println(
                    "No blacklisted devices."
            );

            return;
        }

        for (String device : blacklistedDevices) {

            System.out.println(device);
        }
    }

    // Main method for testing SecurityManager
    public static void main(String[] args) {

        SecurityManager security =
                new SecurityManager();

        String trustedUSB =
                "USB\\TRUSTED_DEVICE_001";

        String blacklistedUSB =
                "USB\\BLACKLISTED_DEVICE_001";

        String unknownUSB =
                "USB\\UNKNOWN_DEVICE_001";

        // Add test devices
        security.addTrustedDevice(
                trustedUSB
        );

        security.addBlacklistedDevice(
                blacklistedUSB
        );

        // Check security status
        System.out.println();
        System.out.println("Security Test Results");
        System.out.println("---------------------");

        security.showSecurityStatus(
                trustedUSB
        );

        security.showSecurityStatus(
                blacklistedUSB
        );

        security.showSecurityStatus(
                unknownUSB
        );

        // Display device lists
        security.showTrustedDevices();

        security.showBlacklistedDevices();
    }
}
