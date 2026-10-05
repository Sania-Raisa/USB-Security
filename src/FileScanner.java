import java.io.File;

public class FileScanner {

    private static final String[] SUSPICIOUS_EXTENSIONS = {
        ".exe",
        ".bat",
        ".cmd",
        ".vbs",
        ".scr",
        ".js"
    };

    public static void scanDrive(String drivePath) {

        File drive = new File(drivePath);

        if (!drive.exists()) {
            System.out.println("Drive not found: " + drivePath);
            return;
        }

        System.out.println("\nScanning drive: " + drivePath);
        System.out.println("--------------------------------");

        scanFiles(drive);
    }

    private static void scanFiles(File folder) {

        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                scanFiles(file);

            } else {

                System.out.println("File: " + file.getName());

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

    private static boolean isSuspicious(File file) {

        String fileName = file.getName().toLowerCase();

        for (String extension : SUSPICIOUS_EXTENSIONS) {

            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }
}