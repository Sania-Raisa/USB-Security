# Smart USB Security System

A Java desktop application that monitors USB devices, logs USB activity, and scans removable drives for suspicious files.

## Features

* Detects connected USB devices
* Shows device name, ID, and status
* Monitors USB connect/disconnect events
* Logs activities with timestamps
* Scans USB drives for suspicious file types
* Supports trusted and blacklisted devices
* Provides a simple Java Swing dashboard

## Tech Stack

* Java
* Java Swing
* PowerShell / WMI
* Object-Oriented Programming

## Requirements

* Windows OS
* JDK 8 or higher
* Git

## Run Locally

Clone the repository:

```bash
git clone https://github.com/Sania-Raisa/USB-Security.git
cd USB-Security
```

Compile and run:

```bash
javac -encoding UTF-8 -d out Dashboard.java src/*.java
java -cp out Dashboard
```

To run the console USB monitor:

```bash
java -cp out USBDeviceMonitor
```

## Main Functions

| Function         |Description                                  |
| ---------------- | -------------------------------------------- |
| Refresh Devices  | Shows currently connected USB devices        |
| Start Monitoring | Detects USB connect/disconnect events        |
| Security Status  | Checks the selected device                   |
| Scan USB Drive   | Scans a removable drive for suspicious files |
| Clear Log        | Clears the activity log                      |

## Suspicious File Detection

The scanner checks for potentially risky file types such as:

`.exe` `.bat` `.cmd` `.vbs` `.scr` `.js`

> This is a rule-based scanner for educational purposes. It is not a replacement for antivirus software.

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

## Future Improvements

* Show scan results directly in the GUI
* Improve trusted/blacklisted device management
* Add automatic alerts for unknown devices
* Add automatic blocking of untrusted devices

## Note

This project is developed for educational purposes using Core Java, OOP, DSA, and Java Swing.
