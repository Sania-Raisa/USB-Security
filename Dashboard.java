import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Dashboard extends JFrame {

    private JTable deviceTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JLabel statusLabel;

    private SecurityManager securityManager;

    private boolean monitoringStarted = false;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Dashboard() {

        setTitle("Smart USB Security System");

        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

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

        // Double click device
        deviceTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        if (e.getClickCount() == 2) {

                            showSecurityMessage();
                        }
                    }
                }
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

        JButton trustButton =
                new JButton("Trust Device");

        JButton blockButton =
                new JButton("Block Device");

        JButton removeSecurityButton =
                new JButton("Remove Security");

        JButton scanButton =
                new JButton("Scan USB Drive");

        JButton clearButton =
                new JButton("Clear Log");

        // =================================================
        // REFRESH
        // =================================================

        refreshButton.addActionListener(e -> {

            loadUSBDevices();

            addLog(
                    "USB device list refreshed."
            );
        });

        // =================================================
        // MONITOR
        // =================================================

        monitorButton.addActionListener(e -> {

            startMonitoring();

        });

        // =================================================
        // SECURITY STATUS
        // =================================================

        securityButton.addActionListener(e -> {

            showSecurityMessage();

        });

        // =================================================
        // TRUST
        // =================================================

        trustButton.addActionListener(e -> {

            trustSelectedDevice();

        });

        // =================================================
        // BLOCK
        // =================================================

        blockButton.addActionListener(e -> {

            blockSelectedDevice();

        });

        // =================================================
        // REMOVE SECURITY
        // =================================================

        removeSecurityButton.addActionListener(e -> {

            removeSelectedDeviceSecurity();

        });

        // =================================================
        // SCAN USB
        // =================================================

        scanButton.addActionListener(e -> {

            scanUSBDrive();

        });

        // =================================================
        // CLEAR LOG
        // =================================================

        clearButton.addActionListener(e -> {

            logArea.setText("");

        });

        // =================================================
        // BUTTON PANEL
        // =================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                6,
                                5
                        )
                );

        buttonPanel.add(refreshButton);
        buttonPanel.add(monitorButton);
        buttonPanel.add(securityButton);
        buttonPanel.add(trustButton);
        buttonPanel.add(blockButton);
        buttonPanel.add(removeSecurityButton);
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
                    "powershell.exe -Command "
                            +
                            "\"Get-CimInstance Win32_PnPEntity | "
                            +
                            "Where-Object {$_.PNPDeviceID -like 'USB*'} | "
                            +
                            "Select-Object Name,PNPDeviceID,Status | "
                            +
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

            if (securityManager.isBlacklisted(deviceID)) {

                return "BLOCKED";
            }

            if (securityManager.isTrusted(deviceID)) {

                return "TRUSTED";
            }

            return "UNKNOWN";

        } catch (Exception e) {

            return "UNKNOWN";
        }
    }

    // =====================================================
    // TRUST DEVICE
    // =====================================================

    private void trustSelectedDevice() {

        int selectedRow =
                deviceTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a USB device first.",
                    "Trust Device",
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

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Add this device to trusted devices?\n\n"
                                +
                                "Device: "
                                +
                                deviceName,
                        "Trust USB Device",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (confirmation != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            if (securityManager.isBlacklisted(deviceID)) {

                securityManager.removeBlacklistedDevice(
                        deviceID
                );
            }

            securityManager.addTrustedDevice(
                    deviceID
            );

            tableModel.setValueAt(
                    "TRUSTED",
                    selectedRow,
                    3
            );

            statusLabel.setText(
                    "Status: Device marked as TRUSTED"
            );

            addLog(
                    "Device trusted: "
                            + deviceName
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Device has been added to the trusted list.",
                    "Device Trusted",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not add device to trusted list.",
                    "Security Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // BLOCK DEVICE
    // =====================================================

    private void blockSelectedDevice() {

        int selectedRow =
                deviceTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a USB device first.",
                    "Block Device",
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

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Add this device to the blacklist?\n\n"
                                +
                                "Device: "
                                +
                                deviceName,
                        "Block USB Device",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmation != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            if (securityManager.isTrusted(deviceID)) {

                securityManager.removeTrustedDevice(
                        deviceID
                );
            }

            securityManager.addBlacklistedDevice(
                    deviceID
            );

            tableModel.setValueAt(
                    "BLOCKED",
                    selectedRow,
                    3
            );

            statusLabel.setText(
                    "Status: Device marked as BLOCKED"
            );

            addLog(
                    "Device blocked: "
                            + deviceName
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Device has been added to the blacklist.",
                    "Device Blocked",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not block this device.",
                    "Security Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // REMOVE SECURITY
    // =====================================================

    private void removeSelectedDeviceSecurity() {

        int selectedRow =
                deviceTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a USB device first.",
                    "Remove Security",
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

        String currentStatus =
                checkDeviceSecurity(deviceID);

        if (currentStatus.equals("UNKNOWN")) {

            JOptionPane.showMessageDialog(
                    this,
                    "This device is already UNKNOWN.",
                    "Remove Security",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Remove this device from its security list?\n\n"
                                +
                                "Device: "
                                +
                                deviceName,
                        "Remove Security Status",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (confirmation != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            if (securityManager.isTrusted(deviceID)) {

                securityManager.removeTrustedDevice(
                        deviceID
                );
            }

            if (securityManager.isBlacklisted(deviceID)) {

                securityManager.removeBlacklistedDevice(
                        deviceID
                );
            }

            tableModel.setValueAt(
                    "UNKNOWN",
                    selectedRow,
                    3
            );

            statusLabel.setText(
                    "Status: Security status removed"
            );

            addLog(
                    "Security status removed: "
                            + deviceName
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not remove security status.",
                    "Security Error",
                    JOptionPane.ERROR_MESSAGE
            );
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

                                        if (securityStatus.equals(
                                                "UNKNOWN")) {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "Unknown USB device detected!\n\n"
                                                            +
                                                            "Drive: "
                                                            + newDrive
                                                            +
                                                            "\n\nSecurity Status: UNKNOWN\n\n"
                                                            +
                                                            "Please check this device.",
                                                    "USB Security Alert",
                                                    JOptionPane.WARNING_MESSAGE
                                            );

                                            addLog(
                                                    "Security alert: Unknown USB device."
                                            );
                                        }

                                        else if (securityStatus.equals(
                                                "BLOCKED")) {

                                            JOptionPane.showMessageDialog(
                                                    this,
                                                    "BLOCKED USB device detected!\n\n"
                                                            +
                                                            "Drive: "
                                                            + newDrive
                                                            +
                                                            "\n\nSecurity Status: BLOCKED\n\n"
                                                            +
                                                            "Do not use this device.",
                                                    "USB Security Alert",
                                                    JOptionPane.ERROR_MESSAGE
                                            );

                                            addLog(
                                                    "Security alert: Blocked USB device."
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

                        } catch (InterruptedException e) {

                            break;
                        }
                    }
                });

        monitorThread.setDaemon(true);

        monitorThread.start();

        JOptionPane.showMessageDialog(
                this,
                "USB monitoring started.\n\n"
                        +
                        "Now connect your pendrive.",
                "USB Monitoring",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // FIND DEVICE FOR DRIVE
    // =====================================================

    private int findDeviceForDrive(String drive) {

        loadUSBDevices();

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
    // GET REMOVABLE DRIVES
    // =====================================================

    private String getRemovableDrives() {

        StringBuilder drives =
                new StringBuilder();

        try {

            String command =
                    "powershell.exe -Command "
                            +
                            "\"Get-CimInstance Win32_LogicalDisk | "
                            +
                            "Where-Object {$_.DriveType -eq 2} | "
                            +
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
    // SECURITY STATUS MESSAGE
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

        String securityStatus =
                checkDeviceSecurity(deviceID);

        tableModel.setValueAt(
                securityStatus,
                selectedRow,
                3
        );

        String message =
                "Device: "
                        + deviceName
                        + "\n\n"
                        +
                        "Device ID: "
                        + deviceID
                        + "\n\n"
                        +
                        "Security Status: "
                        + securityStatus;

        if (securityStatus.equals("BLOCKED")) {

            JOptionPane.showMessageDialog(
                    this,
                    message
                            + "\n\nWarning: This device is blocked.",
                    "Security Status",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        else if (securityStatus.equals("TRUSTED")) {

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
                            + "\n\nThis device is not in the trusted or blocked list.",
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
                        "Enter USB drive letter:\nExample: E:",
                        "Scan USB Drive",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (drive == null
                || drive.trim().isEmpty()) {

            return;
        }

        drive = drive.trim();

        // If user enters E instead of E:
        if (drive.length() == 1) {

            drive = drive + ":";
        }

        // Remove final backslash if user enters E:\
        if (drive.endsWith("\\")) {

            drive =
                    drive.substring(
                            0,
                            drive.length() - 1
                    );
        }

        final String selectedDrive = drive;

        addLog(
                "USB drive scan started: "
                        + selectedDrive
        );

        statusLabel.setText(
                "Status: Scanning USB drive..."
        );

        // =================================================
        // RUN SCANNER IN BACKGROUND
        // =================================================

        Thread scanThread =
                new Thread(() -> {

                    try {

                        FileScanner.ScanResult result =
                                FileScanner.scanDrive(
                                        selectedDrive
                                );

                        SwingUtilities.invokeLater(() -> {

                            statusLabel.setText(
                                    "Status: USB scan completed"
                            );

                            addLog(
                                    "USB drive scan completed: "
                                            + selectedDrive
                            );

                            showScanResult(
                                    selectedDrive,
                                    result
                            );
                        });

                    } catch (Exception e) {

                        SwingUtilities.invokeLater(() -> {

                            statusLabel.setText(
                                    "Status: Scan error"
                            );

                            addLog(
                                    "USB drive scan failed: "
                                            + selectedDrive
                            );

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Could not scan the USB drive.\n\n"
                                            +
                                            e.getMessage(),
                                    "Scanner Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        });
                    }

                });

        scanThread.setDaemon(true);

        scanThread.start();

        JOptionPane.showMessageDialog(
                this,
                "USB drive scanning started.\n\n"
                        +
                        "Drive: "
                        + selectedDrive
                        +
                        "\n\n"
                        +
                        "Please wait for the scan to complete.",
                "File Scanner",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // SHOW SCAN RESULT
    // =====================================================

    private void showScanResult(
            String drive,
            FileScanner.ScanResult result) {

        JTextArea resultArea =
                new JTextArea();

        resultArea.setEditable(false);

        resultArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        resultArea.setLineWrap(false);

        StringBuilder text =
                new StringBuilder();

        text.append(
                "USB SECURITY SCAN RESULT\n"
        );

        text.append(
                "====================================\n\n"
        );

        text.append(
                "Drive: "
                        + drive
                        + "\n\n"
        );

        text.append(
                "Total Files       : "
                        + result.getTotalFiles()
                        + "\n"
        );

        text.append(
                "Safe Files        : "
                        + result.getSafeFiles()
                        + "\n"
        );

        text.append(
                "Suspicious Files  : "
                        + result.getSuspiciousCount()
                        + "\n\n"
        );

        text.append(
                "====================================\n"
        );

        if (result.getSuspiciousCount() > 0) {

            text.append(
                    "SUSPICIOUS FILES\n"
            );

            text.append(
                    "====================================\n\n"
            );

            for (String file :
                    result.getSuspiciousFiles()) {

                text.append(
                        file
                                + "\n"
                );
            }

        } else {

            text.append(
                    "No suspicious files were detected.\n"
            );
        }

        resultArea.setText(
                text.toString()
        );

        JScrollPane scrollPane =
                new JScrollPane(resultArea);

        scrollPane.setPreferredSize(
                new Dimension(
                        750,
                        450
                )
        );

        JPanel panel =
                new JPanel(new BorderLayout(5, 5));

        JLabel title =
                new JLabel(
                        "Scan Result - "
                                + drive
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        if (result.getSuspiciousCount() > 0) {

            JOptionPane.showMessageDialog(
                    this,
                    panel,
                    "USB Security Alert",
                    JOptionPane.WARNING_MESSAGE
            );

            addLog(
                    "WARNING: "
                            + result.getSuspiciousCount()
                            + " suspicious file(s) detected."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    panel,
                    "USB Scan Completed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            addLog(
                    "Scan completed: No suspicious files found."
            );
        }
    }

    // =====================================================
    // ADD ACTIVITY LOG
    // =====================================================

    private void addLog(String message) {

        if (logArea != null) {

            logArea.append(
                    message + "\n"
            );

            logArea.setCaretPosition(
                    logArea.getDocument().getLength()
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