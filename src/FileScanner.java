import java.io.File;
public class FileScanner {
    // Suspicious file extensions
    private static final String[] SUSPICIOUS_EXTENSIONS = {
        ".exe",
        ".bat",
        ".cmd",
        ".vbs",
        ".scr",
        ".js"
    };
    // USB drive-এর files scan করার method
    public static void scanDrive(String drivePath) {
        File drive = new File(drivePath);
        // Drive exists কিনা check
        if (!drive.exists()) {
            System.out.println("Drive not found: " + drivePath);
            return;
        }
        System.out.println("\nScanning drive: " + drivePath);
        System.out.println("--------------------------------");
        scanFiles(drive);
    }
    // File এবং folder recursively scan করবে
    private static void scanFiles(File folder) {
        File[] files = folder.listFiles();
        // Folder access করা না গেলে
        if (files == null) {
            return;
        }
        for (File file : files) {
            // যদি folder হয়, তার ভিতরেও scan করবে
            if (file.isDirectory()) {
                scanFiles(file);
            } else {
                // File-এর নাম দেখাবে
                System.out.println("File: " + file.getName());
                // Suspicious কিনা check
                if (isSuspicious(file)) {
                    System.out.println(
                        "WARNING: Suspicious file detected!"
                    );
                } else {
                    System.out.println("Status: Normal");
                }
            }
        }
    }
    // File-এর extension suspicious কিনা check করে
    private static boolean isSuspicious(File file) {
        String fileName = file.getName().toLowerCase();
        for (String extension : SUSPICIOUS_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
    // Testing-এর জন্য main method
    public static void main(String[] args) {
        // নিজের USB-এর drive letter এখানে দিতে হবে
        // Example: E:
        scanDrive("E:\\");
    }
}