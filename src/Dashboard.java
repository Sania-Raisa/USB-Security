import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Dashboard extends JFrame {
        
// USB device table
private JTable deviceTable;

// Table data model
private DefaultTableModel tableModel;

// Activity log area
private JTextArea logArea;

// Status label
private JLabel statusLabel;

// Security manager
private SecurityManager securityManager;

// Prevent multiple monitoring threads
private boolean monitoringStarted = false;

// Constructor
public Dashboard() {

    setTitle("Smart USB Security System");

    setSize(950, 650);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLocationRelativeTo(null);

    // Create SecurityManager object
    securityManager = new SecurityManager();

    createGUI();

    loadUSBDevices();
}

// =====================================================
// CREATE GUI
// =====================================================

private void createGUI() {

    JPanel mainPanel =
            new JPanel(new BorderLayout(10, 10));

    mainPanel.setBorder(
            BorderFactory.createEmptyBorder(
                    15, 15, 15, 15
            )
    );

    // =================================================
    // HEADER
    // =================================================

    JLabel titleLabel =
            new JLabel(
                    "SMART USB SECURITY SYSTEM",
                    SwingConstants.CENTER
            );

    titleLabel.setFont(
            new Font("Arial", Font.BOLD, 25)
    );

    JLabel subtitleLabel =
            new JLabel(
                    "USB Device Monitoring and Security Dashboard",
                    SwingConstants.CENTER
            );

    subtitleLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
    );

    JPanel headerPanel =
            new JPanel(new BorderLayout());

    headerPanel.add(
            titleLabel,
            BorderLayout.CENTER
    );

    headerPanel.add(
            subtitleLabel,
            BorderLayout.SOUTH
    );

    mainPanel.add(
            headerPanel,
            BorderLayout.NORTH
    );

    // =================================================
    // USB DEVICE TABLE
    // =================================================

    String[] columns = {
            "Device Name",
            "Device ID",
            "Status",
            "Security"
    };

    tableModel =
            new DefaultTableModel(columns, 0) {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column) {

                    return false;
                }
            };

    deviceTable =
            new JTable(tableModel);

    deviceTable.setRowHeight(28);

    deviceTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
    );

    deviceTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
    );

    deviceTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
    );

    JScrollPane tableScrollPane =
            new JScrollPane(deviceTable);

    JLabel deviceTitle =
            new JLabel("Connected USB Devices");

    deviceTitle.setFont(
            new Font("Arial", Font.BOLD, 18)
    );

    JPanel devicePanel =
            new JPanel(new BorderLayout(5, 5));

    devicePanel.add(
            deviceTitle,
            BorderLayout.NORTH
    );

    devicePanel.add(
            tableScrollPane,
            BorderLayout.CENTER
    );

    // =================================================
    // BUTTONS
    // =================================================

    JButton refreshButton =
            new JButton("Refresh Devices");

    JButton monitorButton =
            new JButton("Start Monitoring");

    JButton securityButton =
            new JButton("Security Status");

    JButton scanButton =
            new JButton("Scan USB Drive");

    JButton clearButton =
            new JButton("Clear Log");

    // =================================================
    // REFRESH BUTTON
    // =================================================

    refreshButton.addActionListener(e -> {

        loadUSBDevices();

        addLog(
                "USB device list refreshed."
        );
    });

    // =================================================
    // MONITOR BUTTON
    // =================================================

    monitorButton.addActionListener(e -> {

        startMonitoring();

    });

    // =================================================
    // SECURITY BUTTON
    // =================================================

    securityButton.addActionListener(e -> {

        showSecurityMessage();

    });

    // =================================================
    // SCAN BUTTON
    // =================================================

    scanButton.addActionListener(e -> {

        scanUSBDrive();

    });

    // =================================================
    // CLEAR LOG BUTTON
    // =================================================

    clearButton.addActionListener(e -> {

        logArea.setText("");

    });

    JPanel buttonPanel =
            new JPanel(
                    new FlowLayout(
                            FlowLayout.LEFT,
                            8,
                            5
                    )
            );

    buttonPanel.add(refreshButton);
    buttonPanel.add(monitorButton);
    buttonPanel.add(securityButton);
    buttonPanel.add(scanButton);
    buttonPanel.add(clearButton);

    devicePanel.add(
            buttonPanel,
            BorderLayout.SOUTH
    );

    mainPanel.add(
            devicePanel,
            BorderLayout.CENTER
    );

    // =================================================
    // ACTIVITY LOG
    // =================================================

    logArea =
            new JTextArea(8, 30);

    logArea.setEditable(false);

    logArea.setLineWrap(true);

    logArea.setWrapStyleWord(true);

    logArea.setFont(
            new Font(
                    "Monospaced",
                    Font.PLAIN,
                    13
            )
    );

    JScrollPane logScrollPane =
            new JScrollPane(logArea);

    JLabel logTitle =
            new JLabel("Activity Log");

    logTitle.setFont(
            new Font("Arial", Font.BOLD, 17)
    );

    JPanel logPanel =
            new JPanel(new BorderLayout(5, 5));

    logPanel.add(
            logTitle,
            BorderLayout.NORTH
    );

    logPanel.add(
            logScrollPane,
            BorderLayout.CENTER
    );

    // =================================================
    // STATUS
    // =================================================

    statusLabel =
            new JLabel("Status: Ready");

    statusLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
    );

    JPanel bottomPanel =
            new JPanel(new BorderLayout(5, 5));

    bottomPanel.add(
            logPanel,
            BorderLayout.CENTER
    );

    bottomPanel.add(
            statusLabel,
            BorderLayout.SOUTH
    );

    mainPanel.add(
            bottomPanel,
            BorderLayout.SOUTH
    );

    add(mainPanel);
}

// =====================================================
// LOAD USB DEVICES
// =====================================================

private void loadUSBDevices() {

    tableModel.setRowCount(0);

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

        String name = "";
        String deviceID = "";
        String status = "";

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.startsWith("Name")) {

                name =
                        line.substring(
                                line.indexOf(":") + 1
                        ).trim();
            }

            else if (line.startsWith("PNPDeviceID")) {

                deviceID =
                        line.substring(
                                line.indexOf(":") + 1
                        ).trim();
            }

            else if (line.startsWith("Status")) {

                status =
                        line.substring(
                                line.indexOf(":") + 1
                        ).trim();

                // Check device security
                String securityStatus =
                        checkDeviceSecurity(deviceID);

                tableModel.addRow(
                        new Object[]{
                                name,
                                deviceID,
                                status,
                                securityStatus
                        }
                );

                name = "";
                deviceID = "";
                status = "";
            }
        }

        reader.close();

        process.waitFor();

        statusLabel.setText(
                "Status: USB devices loaded"
        );

    } catch (Exception e) {

        statusLabel.setText(
                "Status: Error detecting USB devices"
        );

        addLog(
                "Error detecting USB devices."
        );
    }
}

// =====================================================
// CHECK DEVICE SECURITY
// =====================================================

private String checkDeviceSecurity(String deviceID) {

    if (deviceID == null
            || deviceID.trim().isEmpty()) {

        return "UNKNOWN";
    }

    try {

        String result =
                securityManager.checkDevice(
                        deviceID
                );

        if (result == null
                || result.trim().isEmpty()) {

            return "UNKNOWN";
        }

        return result.toUpperCase();

    } catch (Exception e) {

        addLog(
                "Security check failed for device."
        );

        return "UNKNOWN";
    }
}

// =====================================================
// START MONITORING
// =====================================================

private void startMonitoring() {

    if (monitoringStarted) {

        JOptionPane.showMessageDialog(
                this,
                "USB monitoring is already running.",
                "USB Monitoring",
                JOptionPane.INFORMATION_MESSAGE
        );

        return;
    }

    monitoringStarted = true;

    Thread monitorThread =
            new Thread(() -> {

                String previousDrives =
                        getRemovableDrives();

                SwingUtilities.invokeLater(() -> {

                    addLog(
                            "USB monitoring started."
                    );

                    statusLabel.setText(
                            "Status: Monitoring USB devices..."
                    );
                });

                while (true) {

                    try {

                        Thread.sleep(2000);

                        String currentDrives =
                                getRemovableDrives();

                        // New removable drive detected
                        if (!currentDrives.equals(
                                previousDrives)) {

                            String newDrive =
                                    findNewDrive(
                                            previousDrives,
                                            currentDrives
                                    );

                            if (!newDrive.isEmpty()
                                    && !previousDrives.contains(
                                            newDrive)) {

                                SwingUtilities.invokeLater(() -> {

                                    loadUSBDevices();

                                    addLog(
                                            "New USB drive detected: "
                                                    + newDrive
                                    );

                                    statusLabel.setText(
                                            "Status: USB DEVICE DETECTED"
                                    );

                                    // Find security status
                                    int selectedRow =
                                            findDeviceForDrive(
                                                    newDrive
                                            );

                                    String securityStatus =
                                            "UNKNOWN";

                                    if (selectedRow != -1) {

                                        securityStatus =
                                                tableModel.getValueAt(
                                                        selectedRow,
                                                        3
                                                ).toString();
                                    }

                                    // Alert only for UNKNOWN
                                    if (securityStatus.equals(
                                            "UNKNOWN")) {

                                        JOptionPane.showMessageDialog(
                                                this,
                                                "Unknown USB device detected!\n\n"
                                                        + "Drive: "
                                                        + newDrive
                                                        + "\n\n"
                                                        + "Security Status: UNKNOWN\n\n"
                                                        + "Please check this device.",
                                                "USB Security Alert",
                                                JOptionPane.WARNING_MESSAGE
                                        );

                                        addLog(
                                                "Security alert: Unknown USB device."
                                        );
                                    }

                                    else if (securityStatus.equals(
                                            "BLACKLISTED")) {

                                        JOptionPane.showMessageDialog(
                                                this,
                                                "BLACKLISTED USB device detected!\n\n"
                                                        + "Drive: "
                                                        + newDrive
                                                        + "\n\n"
                                                        + "Security Status: BLACKLISTED\n\n"
                                                        + "Do not use this device.",
                                                "USB Security Alert",
                                                JOptionPane.ERROR_MESSAGE
                                        );

                                        addLog(
                                                "Security alert: Blacklisted USB device."
                                        );
                                    }

                                    else {

                                        addLog(
                                                "USB device security status: "
                                                        + securityStatus
                                        );
                                    }
                                });
                            }

                            // Check for removed USB
                            if (currentDrives.length()
                                    < previousDrives.length()) {

                                SwingUtilities.invokeLater(() -> {

                                    loadUSBDevices();

                                    addLog(
                                            "USB device disconnected."
                                    );

                                    statusLabel.setText(
                                            "Status: USB device disconnected"
                                    );
                                });
                            }

                            previousDrives =
                                    currentDrives;
                        }

                    } catch (
                            InterruptedException e) {

                        break;
                    }
                }
            });

    monitorThread.setDaemon(true);

    monitorThread.start();

    JOptionPane.showMessageDialog(
            this,
            "USB monitoring started.\n\n"
                    + "Now connect your pendrive.",
            "USB Monitoring",
            JOptionPane.INFORMATION_MESSAGE
    );
}

// =====================================================
// FIND DEVICE FOR DRIVE
// =====================================================

private int findDeviceForDrive(String drive) {

    // Refresh table first
    loadUSBDevices();

    // Current implementation uses the first
    // available USB storage device.
    for (int i = 0;
         i < tableModel.getRowCount();
         i++) {

        String deviceID =
                tableModel.getValueAt(
                        i,
                        1
                ).toString();

        if (deviceID.contains("USBSTOR")) {

            return i;
        }
    }

    return -1;
}

// =====================================================
// GET REMOVABLE USB DRIVES
// =====================================================

private String getRemovableDrives() {

    StringBuilder drives =
            new StringBuilder();

    try {

        String command =
                "powershell.exe -Command " +
                "\"Get-CimInstance Win32_LogicalDisk | " +
                "Where-Object {$_.DriveType -eq 2} | " +
                "Select-Object -ExpandProperty DeviceID\"";

        Process process =
                Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                process.getInputStream()
                        )
                );

        String line;

        while ((line = reader.readLine()) != null) {

            if (!line.trim().isEmpty()) {

                drives.append(
                        line.trim()
                );

                drives.append("\n");
            }
        }

        reader.close();

        process.waitFor();

    } catch (Exception e) {

        return "";
    }

    return drives.toString();
}

// =====================================================
// FIND NEW DRIVE
// =====================================================

private String findNewDrive(
        String oldDrives,
        String newDrives) {

    String[] drives =
            newDrives.split("\\R");

    for (String drive : drives) {

        drive = drive.trim();

        if (!drive.isEmpty()
                && !oldDrives.contains(drive)) {

            return drive;
        }
    }

    return "";
}

// =====================================================
// SECURITY STATUS
// =====================================================

private void showSecurityMessage() {

    int selectedRow =
            deviceTable.getSelectedRow();

    if (selectedRow == -1) {

        JOptionPane.showMessageDialog(
                this,
                "Please select a USB device first.",
                "Security Status",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    String deviceName =
            tableModel.getValueAt(
                    selectedRow,
                    0
            ).toString();

    String deviceID =
            tableModel.getValueAt(
                    selectedRow,
                    1
            ).toString();

    // Call SecurityManager
    String securityStatus =
            checkDeviceSecurity(deviceID);

    // Update table
    tableModel.setValueAt(
            securityStatus,
            selectedRow,
            3
    );

    String message =
            "Device: "
                    + deviceName
                    + "\n\n"
                    + "Device ID: "
                    + deviceID
                    + "\n\n"
                    + "Security Status: "
                    + securityStatus;

    if (securityStatus.equals(
            "BLACKLISTED")) {

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\nWarning: This device is blacklisted.",
                "Security Status",
                JOptionPane.ERROR_MESSAGE
        );
    }

    else if (securityStatus.equals(
            "TRUSTED")) {

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\nThis device is trusted.",
                "Security Status",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    else {

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\nThis device is not in the trusted list.",
                "Security Status",
                JOptionPane.WARNING_MESSAGE
        );
    }

    addLog(
            "Security status checked: "
                    + deviceName
                    + " -> "
                    + securityStatus
    );
}

// =====================================================
// USB DRIVE SCANNER
// =====================================================

private void scanUSBDrive() {

    String drive =
            JOptionPane.showInputDialog(
                    this,
                    "Enter USB drive letter:\nExample: E:\\",
                    "Scan USB Drive",
                    JOptionPane.QUESTION_MESSAGE
            );

    if (drive == null
            || drive.trim().isEmpty()) {

        return;
    }

    drive = drive.trim();

    FileScanner.scanDrive(drive);

    addLog(
            "USB drive scan started: "
                    + drive
    );

    JOptionPane.showMessageDialog(
            this,
            "Drive scan started.\n\n"
                    + "Check the console for scan results.",
            "File Scanner",
            JOptionPane.INFORMATION_MESSAGE
    );
}

// =====================================================
// ADD ACTIVITY LOG
// =====================================================

private void addLog(String message) {

    if (logArea != null) {

        logArea.append(
                message + "\n"
        );
    }
}

// =====================================================
// MAIN METHOD
// =====================================================

public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

        Dashboard dashboard =
                new Dashboard();

        dashboard.setVisible(true);
    });
}

}
