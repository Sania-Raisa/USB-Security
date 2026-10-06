# Smart USB Security System

A Java desktop application that monitors USB devices, logs connection activity, and scans removable drives for suspicious files.

## Features

- **Device detection**: lists connected USB devices with name, device ID and status
- **Live monitoring**: detects connect/disconnect events (polls every 2 seconds)
- **Activity logging**: records events with timestamps in `activity_log.txt`
- **Drive scanner**: recursively scans a drive and flags risky file types (`.exe`, `.bat`, `.cmd`, `.vbs`, `.scr`, `.js`)
- **Device trust model**: `SecurityManager` supports trusted and blacklisted device lists
- **Swing dashboard**: simple GUI to access all of the above

## Tech Stack

Java (Swing) · PowerShell / WMI (`Win32_PnPEntity`)

## Requirements

- **Windows** (device detection uses PowerShell)
- **JDK 8 or higher** (JDK 17+ recommended)
- Git

## Run Locally

```bash
git clone https://github.com/<your-username>/USB-Security.git
cd USB-Security
javac -version
javac -encoding UTF-8 -d out Dashboard.java src/*.java
java -cp out Dashboard
```

Run the console monitor instead of the GUI:

```bash
java -cp out USBDeviceMonitor
```

## Usage

| Button | Action |
|---|---|
| Refresh Devices | Reloads the list of connected USB devices |
| Start Monitoring | Watches for USB changes in the background |
| Security Status | Shows the status of the selected device |
| Scan USB Drive | Scans a drive (e.g. `E:\`); results appear in the console |
| Clear Log | Clears the on-screen activity log |

## Project Structure

```
USB-Security/
├── Dashboard.java            # Swing GUI (entry point)
├── main.java
├── activity_log.txt          # Generated event log
└── src/
    ├── ActivityLogger.java   # Timestamped event logging
    ├── FileScanner.java      # Suspicious file detection
    ├── SecurityManager.java  # Trusted / blacklisted devices
    ├── USBDeviceDetector.java
    ├── USBDeviceInfo.java    # Device data model
    └── USBDeviceMonitor.java # Connect/disconnect monitor
```

## Roadmap

- Integrate trusted/blacklist checks into the dashboard
- Show scan results inside the GUI
- Auto-block untrusted devices

## Notes
This project is for educational pusposes.
