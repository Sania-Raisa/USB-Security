import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Set;

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
        loadActivityLog();
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
        JButton flagButton =
                new JButton("Flag as Untrusted");
        JButton removeSecurityButton =
                new JButton("Remove Security");
        JButton scanButton =
                new JButton("Scan USB Drive");
        JButton clearButton =
                new JButton("Clear Log");
        // =================================================
        // REFRESH DEVICES
        // =================================================
        refreshButton.addActionListener(e -> {
            loadUSBDevices();
            addLog(
                    "USB device list refresh requested."
            );
        });
        // =================================================
        // START MONITORING
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
        // TRUST DEVICE
        // =================================================
        trustButton.addActionListener(e -> {
            trustSelectedDevice();
        });
        // =================================================
        // FLAG AS UNTRUSTED
        // =================================================
        flagButton.addActionListener(e -> {
            flagSelectedDevice();
        });
        // =================================================
        // REMOVE SECURITY
        // =================================================
        removeSecurityButton.addActionListener(e -> {
            removeSelectedDeviceSecurity();
        });
        // =================================================
        // SCAN USB DRIVE
        // =================================================
        scanButton.addActionListener(e -> {
            scanUSBDrive();
        });
        // =================================================
        // CLEAR LOG
        // =================================================
        clearButton.addActionListener(e -> {
            clearActivityLog();
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
        buttonPanel.add(flagButton);
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
        statusLabel.setText(
                "Status: Loading USB devices..."
        );
        SwingWorker<Set<USBDeviceInfo>, Void> worker =
                new SwingWorker<Set<USBDeviceInfo>, Void>() {
                    @Override
                    protected Set<USBDeviceInfo> doInBackground()
                            throws Exception {
                        Set<USBDeviceInfo> devices =
                                new java.util.LinkedHashSet<>();
                        String command =
                                "powershell.exe -Command "
                                        +
                                        "\"Get-CimInstance Win32_PnPEntity | "
                                        +
                                        "Where-Object {$_.PNPDeviceID -like 'USB*'} | "
                                        +
                                        "Select-Object Name,PNPDeviceID,Status | "
                                        +
                                        "ForEach-Object { "
                                        +
                                        "$_.Name + '|' + $_.PNPDeviceID + '|' + $_.Status "
                                        +
                                        "}\"";
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
                            line = line.trim();
                            if (line.isEmpty()) {
                                continue;
                            }
                            String[] parts =
                                    line.split(
                                            "\\|",
                                            3
                                    );
                            if (parts.length == 3) {
                                devices.add(
                                        new USBDeviceInfo(
                                                parts[0].trim(),
                                                parts[1].trim(),
                                                parts[2].trim()
                                        )
                                );
                            }
                        }
                        reader.close();
                        process.waitFor();
                        return devices;
                    }
                    @Override
                    protected void done() {
                        try {
                            Set<USBDeviceInfo> devices =
                                    get();
                            tableModel.setRowCount(0);
                            for (USBDeviceInfo device : devices) {
                                String securityStatus =
                                        checkDeviceSecurity(
                                                device.getDeviceID()
                                        );
                                tableModel.addRow(
                                        new Object[]{
                                                device.getName(),
                                                device.getDeviceID(),
                                                device.getStatus(),
                                                securityStatus
                                        }
                                );
                            }
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
                };
        worker.execute();
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
                return "UNTRUSTED";
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
            // =================================================
            // ONLY TRUST DEVICE SHOWS USB SECURITY ALERT
            // =================================================
            JOptionPane.showMessageDialog(
                    this,
                    "Device has been added to the TRUSTED list.\n\n"
                            +
                            "Device: "
                            + deviceName
                            + "\n\n"
                            +
                            "Security Status: TRUSTED",
                    "USB Security Alert",
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
    // FLAG DEVICE AS UNTRUSTED
    // =====================================================
    private void flagSelectedDevice() {
        int selectedRow =
                deviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a USB device first.",
                    "Flag Device",
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
                        "Flag this device as untrusted?\n\n"
                                +
                                "Device: "
                                +
                                deviceName
                                +
                                "\n\n"
                                +
                                "The device will NOT be physically disabled.",
                        "Flag USB Device",
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
                    "UNTRUSTED",
                    selectedRow,
                    3
            );
            statusLabel.setText(
                    "Status: Device marked as UNTRUSTED"
            );
            addLog(
                    "Device flagged as untrusted: "
                            + deviceName
            );
            // =================================================
            // ONLY FLAG DEVICE SHOWS USB SECURITY ALERT
            // =================================================
            JOptionPane.showMessageDialog(
                    this,
                    "WARNING: Device has been flagged as UNTRUSTED.\n\n"
                            +
                            "Device: "
                            + deviceName
                            + "\n\n"
                            +
                            "Security Status: UNTRUSTED\n\n"
                            +
                            "The device is not physically disabled.",
                    "USB Security Alert",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not flag this device.",
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
    // START USB MONITORING
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
                    Set<String> previousDevices =
                            USBDeviceMonitor.getUSBDevices();
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
                            Set<String> currentDevices =
                                    USBDeviceMonitor.getUSBDevices();
                            // =================================
                            // USB DEVICE CONNECTED
                            // =================================
                            for (String deviceID :
                                    currentDevices) {
                                if (!previousDevices.contains(
                                        deviceID)) {
                                    final String connectedDevice =
                                            deviceID;
                                    SwingUtilities.invokeLater(() -> {
                                        loadUSBDevices();
                                        String securityStatus =
                                                checkDeviceSecurity(
                                                        connectedDevice
                                                );
                                        addLog(
                                                "USB device connected: "
                                                        + connectedDevice
                                        );
                                        statusLabel.setText(
                                                "Status: USB device detected"
                                        );
                                        /*
                                         * NO USB SECURITY ALERT HERE.
                                         *
                                         * Monitoring only records the
                                         * connection and updates the table.
                                         */
                                        addLog(
                                                "USB device security status: "
                                                        + securityStatus
                                        );
                                    });
                                }
                            }
                            // =================================
                            // USB DEVICE DISCONNECTED
                            // =================================
                            for (String deviceID :
                                    previousDevices) {
                                if (!currentDevices.contains(
                                        deviceID)) {
                                    final String disconnectedDevice =
                                            deviceID;
                                    SwingUtilities.invokeLater(() -> {
                                        loadUSBDevices();
                                        addLog(
                                                "USB device disconnected: "
                                                        + disconnectedDevice
                                        );
                                        statusLabel.setText(
                                                "Status: USB device disconnected"
                                        );
                                    });
                                }
                            }
                            previousDevices =
                                    currentDevices;
                        } catch (InterruptedException e) {
                            SwingUtilities.invokeLater(() -> {
                                addLog(
                                        "USB monitoring stopped."
                                );
                                statusLabel.setText(
                                        "Status: Monitoring stopped"
                                );
                            });
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
                        "Now connect or disconnect any USB device.",
                "USB Monitoring",
                JOptionPane.INFORMATION_MESSAGE
        );
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
        if (securityStatus.equals("UNTRUSTED")) {
            JOptionPane.showMessageDialog(
                    this,
                    message
                            + "\n\nWarning: This device is flagged as untrusted.",
                    "Security Status",
                    JOptionPane.WARNING_MESSAGE
            );
        } else if (securityStatus.equals("TRUSTED")) {
            JOptionPane.showMessageDialog(
                    this,
                    message
                            + "\n\nThis device is trusted.",
                    "Security Status",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    message
                            + "\n\nThis device is not in the trusted or untrusted list.",
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
        if (drive.length() == 1) {
            drive = drive + ":";
        }
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
                        file + "\n"
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
                    "Scan Result",
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
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        if (logArea != null) {
            logArea.append(
                    message + "\n"
            );
            logArea.setCaretPosition(
                    logArea.getDocument().getLength()
            );
        }
        try {
            ActivityLogger.logActivity(message);
        } catch (Exception e) {
            System.out.println(
                    "Could not save activity log."
            );
        }
    }
    // =====================================================
    // LOAD SAVED ACTIVITY LOG
    // =====================================================
    private void loadActivityLog() {
        try {
            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(
                                    "activity_log.txt"
                            )
                    );
            String line;
            while ((line = reader.readLine()) != null) {
                if (logArea != null) {
                    logArea.append(
                            line + "\n"
                    );
                }
            }
            reader.close();
            if (logArea != null) {
                logArea.setCaretPosition(
                        logArea.getDocument().getLength()
                );
            }
        } catch (Exception e) {
            // File may not exist on first run
        }
    }
    // =====================================================
    // CLEAR ACTIVITY LOG
    // =====================================================
    private void clearActivityLog() {
        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Clear all saved activity logs?",
                        "Clear Activity Log",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(
                                    "activity_log.txt",
                                    false
                            )
                    );
            writer.close();
            logArea.setText("");
            statusLabel.setText(
                    "Status: Activity log cleared"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not clear activity log.",
                    "Log Error",
                    JOptionPane.ERROR_MESSAGE
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
