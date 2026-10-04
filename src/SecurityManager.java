
import java.util.ArrayList;
import java.util.List;

public class SecurityManager {

    private List<String> trustedDevices;
    private List<String> blacklistedDevices;

    public SecurityManager() {
        trustedDevices = new ArrayList<>();
        blacklistedDevices = new ArrayList<>();
    }

    public void addTrustedDevice(String deviceName) {
        trustedDevices.add(deviceName);
        System.out.println(deviceName + " added to trusted devices.");
    }

    public void addBlacklistedDevice(String deviceName) {
        blacklistedDevices.add(deviceName);
        System.out.println(deviceName + " added to blacklist.");
    }

    public String checkDevice(String deviceName) {

        if (blacklistedDevices.contains(deviceName)) {
            return "BLACKLISTED";
        }

        if (trustedDevices.contains(deviceName)) {
            return "TRUSTED";
        }

        return "UNKNOWN";
    }

    public void showSecurityStatus(String deviceName) {

        System.out.println("Device: " + deviceName);
        System.out.println(
            "Security Status: " + checkDevice(deviceName)
        );
    }

    public void showTrustedDevices() {

        System.out.println();
        System.out.println("Trusted Devices");

        for (String device : trustedDevices) {
            System.out.println(device);
        }
    }

    public void showBlacklistedDevices() {

        System.out.println();
        System.out.println("Blacklisted Devices");

        for (String device : blacklistedDevices) {
            System.out.println(device);
        }
    }

    public static void main(String[] args) {

        SecurityManager security = new SecurityManager();

        security.addTrustedDevice("My USB");

        security.addBlacklistedDevice("Unknown USB");

        security.showSecurityStatus("My USB");

        security.showSecurityStatus("Unknown USB");

        security.showSecurityStatus("New USB");

        security.showTrustedDevices();

        security.showBlacklistedDevices();
    }
}


