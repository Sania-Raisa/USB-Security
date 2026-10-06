import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    private static final String[] SUSPICIOUS_EXTENSIONS = {
        ".exe",
        ".bat",
        ".cmd",
        ".vbs",
        ".scr",
        ".js"
    };

    // Stores suspicious files found during scanning
    private static final List<String> suspiciousFiles = new ArrayList<>();

    private static int totalFiles;
    private static int safeFiles;

    public static ScanResult scanDrive(String drivePath) {

        File drive = new File(drivePath);

        // Reset previous scan results
        suspiciousFiles.clear();
        totalFiles = 0;
        safeFiles = 0;

        if (!drive.exists()) {
            System.out.println("Drive not found: " + drivePath);
            return new ScanResult(0, 0, 0, suspiciousFiles);
        }

        System.out.println("\nScanning drive: " + drivePath);
        System.out.println("--------------------------------");

        scanFiles(drive);

        System.out.println("\nScan completed.");
        System.out.println("Total files: " + totalFiles);
        System.out.println("Safe files: " + safeFiles);
        System.out.println("Suspicious files: " + suspiciousFiles.size());

        return new ScanResult(
            totalFiles,
            safeFiles,
            suspiciousFiles.size(),
            new ArrayList<>(suspiciousFiles)
        );
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

                totalFiles++;

                System.out.println("File: " + file.getName());

                if (isSuspicious(file)) {

                    suspiciousFiles.add(file.getAbsolutePath());

                    System.out.println(
                        "WARNING: Suspicious file detected!"
                    );

                } else {

                    safeFiles++;

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

    // Stores the final scan information
    public static class ScanResult {

        private int totalFiles;
        private int safeFiles;
        private int suspiciousCount;
        private List<String> suspiciousFiles;

        public ScanResult(
            int totalFiles,
            int safeFiles,
            int suspiciousCount,
            List<String> suspiciousFiles
        ) {
            this.totalFiles = totalFiles;
            this.safeFiles = safeFiles;
            this.suspiciousCount = suspiciousCount;
            this.suspiciousFiles = suspiciousFiles;
        }

        public int getTotalFiles() {
            return totalFiles;
        }

        public int getSafeFiles() {
            return safeFiles;
        }

        public int getSuspiciousCount() {
            return suspiciousCount;
        }

        public List<String> getSuspiciousFiles() {
            return suspiciousFiles;
        }
    }
}