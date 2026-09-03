import java.io.BufferedReader;
import java.io.InputStreamReader;
public class USBDeviceMonitor{
    public static String getUSBDevices(){
        StringBuilder usbDevices=new StringBuilder();
        try{
            String command="powershell.exe -Command "+
                    "\"Get-CimInstance Win32_LogicalDisk |"+
                    "Where-Object {$_.DriveType -eq 2} |"+
                    "Select-Object DeviceID,Size,FreeSpace\"";

            Process process=Runtime.getRuntime().exec(command);

            BufferedReader reader=new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while((line=reader.readLine())!=null){
                line=line.trim();
                if (!line.isEmpty()
                        && !line.startsWith("DeviceID")
                        && !line.startsWith("----------")){
                    usbDevices.append(line).append("\n");
                }
            }
            process.waitFor();
        }catch(Exception e){
          System.out.println("Error monitoring USB device.");
        }
        return usbDevices.toString().trim();
    }
    public static void monitorUSB(){
        String previousUSB=getUSBDevices();
        System.out.println("USB Device Monitor");
        System.out.println("========================");
        System.out.println("Monitoring USB devices...");
        System.out.println();
        while (true) {
            try {
                Thread.sleep(2000);
                String currentUSB=getUSBDevices();
                // USB device connected
                if (!currentUSB.equals(previousUSB)){
                    if (previousUSB.isEmpty() && !currentUSB.isEmpty()){
                     System.out.println("USB Device Connected!");
                        System.out.println(currentUSB);
                    }
                    // USB device removed
                    else if (!previousUSB.isEmpty() && currentUSB.isEmpty()) {
                        System.out.println("USB Device Disconnected!");
                    }
                    // USB devices changed
                    else{
                        System.out.println("USB Device List Changed!");
                        System.out.println(currentUSB);
                    }
                    System.out.println("------------------------");
                    previousUSB = currentUSB;
                }
            } catch (InterruptedException e) {
                System.out.println("Monitoring stopped.");
                break;
            }
        }
    }
    public static void main(String[] args) {
        monitorUSB();
    }
}
