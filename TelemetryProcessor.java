import java.util.*;
import java.io.*;
class TelemetryData
{
    double cpuUsage;
    double memoryUsage;
    Date timestamp;
    public TelemetryData(double cpuUsage, double memoryUsage, Date timestamp)
    {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.timestamp = timestamp;
    }
    @Override
    public String toString()
    {
        return String.format("Time: %s | CPU Usage: %.2f%% | Memory Usage: %.2f%%",
                timestamp.toString(), cpuUsage, memoryUsage);
    }
}
public class TelemetryProcessor
{
    // Step 2: Simulate Telemetry Data
    public static TelemetryData generateTelemetryData()
    {
        Random random = new Random();
        double cpu = 10 + random.nextDouble() * 90;     // 10–100%
        double memory = 20 + random.nextDouble() * 70;  // 20–90%
        return new TelemetryData(cpu, memory, new Date());
    }
    // Step 3: Process Data
    public static List<TelemetryData> processTelemetry(int count)
    {
        List<TelemetryData> telemetryList = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            telemetryList.add(generateTelemetryData());
            try
            {
                Thread.sleep(500); // simulate time delay between readings
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        return telemetryList;
    }
    // Step 4: Store Data (write to file)
    public static void saveToFile(List<TelemetryData> dataList, String filename)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
        {
            for (TelemetryData data : dataList)
            {
                writer.write(data.toString());
                writer.newLine();
            }
            System.out.println("Processed telemetry data saved to " + filename);
        } catch (IOException e)
        {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    // Step 5: Testing and Display
    public static void main(String[] args)
    {
        System.out.println("Processing Telemetry Data...\n");
        // Generate and process 5 readings
        List<TelemetryData> telemetryDataList = processTelemetry(5);
        // Display processed data
        for (TelemetryData data : telemetryDataList)
        {
            System.out.println(data);
        }
        // Save to file
        saveToFile(telemetryDataList, "telemetry_output.txt");
    }
}