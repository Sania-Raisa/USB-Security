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

    // Device name পাওয়ার method
    public String getName() {
        return name;
    }

    // Device ID পাওয়ার method
    public String getDeviceID() {
        return deviceID;
    }

    // Device status পাওয়ার method
    public String getStatus() {
        return status;
    }

    // Device information update করার method
    public void setStatus(String status) {
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

    // Object-কে readable text হিসেবে দেখানোর method
    @Override
    public String toString() {

        return "Name: " + name
                + " | Device ID: " + deviceID
                + " | Status: " + status;
    }

    // Testing-এর জন্য main method
    
    public static void main(String[] args) {

        // Test device information
        USBDeviceInfo device =
                new USBDeviceInfo(
                        "USB Input Device",
                        "USB\\VID_1EA7&PID_0066",
                        "Connected"
                );

        device.displayInfo();

        System.out.println(device);
    }
}
