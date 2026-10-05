# Smart USB Security System

A Java desktop application that monitors USB devices, logs USB activity, and scans removable drives for suspicious files.

## Features

* Detects connected USB devices
* Shows device name, ID, and status
* Monitors USB connect/disconnect events
* Logs activities with timestamps
* Scans for suspicious file types
* Shows alerts for unknown USB devices
* Java Swing dashboard

## Tech Stack

* Java
* Java Swing
* PowerShell / WMI
* OOP and DSA

## Requirements

* Windows OS
* JDK 8 or higher
* Git

## Run Locally

```bash
git clone https://github.com/Sania-Raisa/USB-Security.git
cd USB-Security
```

```bash
javac -encoding UTF-8 -d out Dashboard.java src/*.java
java -cp out Dashboard
```

## Project Workflow

```text
USB Device
    ↓
Device Detection
    ↓
Device Monitoring
    ↓
Security Check
    ↓
Unknown Device → Security Alert
    ↓
Activity Log
    ↓
Dashboard
```

## Main Functions

| Function         | Description                 |
| ---------------- | --------------------------- |
| Refresh Devices  | Shows connected USB devices |
| Start Monitoring | Detects USB events          |
| Security Status  | Checks device status        |
| Scan USB Drive   | Finds suspicious files      |
| Clear Log        | Clears activity history     |

## Project Structure

```text
USB-Security/
├── Dashboard.java
├── activity_log.txt
└── src/
    ├── ActivityLogger.java
    ├── FileScanner.java
    ├── SecurityManager.java
    ├── USBDeviceDetector.java
    ├── USBDeviceInfo.java
    └── USBDeviceMonitor.java
```

## Suspicious File Detection

Checks file types such as `.exe`, `.bat`, `.cmd`, `.vbs`, `.scr`, and `.js`.

> Rule-based scanner for educational purposes. Not a replacement for antivirus software.

## Future Improvements

* Improve trusted/blacklisted device management
* Add automatic blocking of untrusted devices
* Improve file scanning rules

## Note

Developed for educational purposes using Core Java, OOP, DSA, and Java Swing.
