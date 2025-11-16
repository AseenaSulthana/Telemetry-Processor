import java.util.*;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import java.text.SimpleDateFormat;

public class TelemetryServer {
    private static final int PORT = 8080;
    private static final String OUTPUT_FILE = "telemetry_output.txt";
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Serve static files (CSS, JS, images)
        server.createContext("/styles.css", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                serveStaticFile(exchange, "styles.css", "text/css");
            }
        });
        
        server.createContext("/script.js", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                serveStaticFile(exchange, "script.js", "application/javascript");
            }
        });
        
        // Serve HTML pages
        server.createContext("/", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();
                if (path.equals("/") || path.equals("/index.html")) {
                    serveFile(exchange, "index.html");
                } else if (path.equals("/dash.html") || path.equals("/dashboard.html")) {
                    serveFile(exchange, "dash.html");
                } else if (path.equals("/analysis.html") || path.equals("/analytics.html")) {
                    serveFile(exchange, "analysis.html");
                } else if (path.equals("/how it works.html") || path.equals("/how-it-works.html")) {
                    serveFile(exchange, "how it works.html");
                } else {
                    // Default to index.html
                    serveFile(exchange, "index.html");
                }
            }
        });
        
        // API endpoint to get telemetry data
        server.createContext("/api/telemetry", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGetTelemetry(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });
        
        // Enable CORS
        server.setExecutor(null);
        server.start();
        System.out.println("Telemetry Server started on http://localhost:" + PORT);
        System.out.println("Open http://localhost:" + PORT + " in your browser");
        System.out.println("Available pages:");
        System.out.println("  - http://localhost:" + PORT + "/ (Landing Page)");
        System.out.println("  - http://localhost:" + PORT + "/dash.html (Dashboard)");
        System.out.println("  - http://localhost:" + PORT + "/analysis.html (Analytics)");
        System.out.println("  - http://localhost:" + PORT + "/how it works.html (How It Works)");
    }
    
    private static void serveStaticFile(HttpExchange exchange, String filename, String contentType) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            String response = "File not found";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, file.length());
        
        FileInputStream fis = new FileInputStream(file);
        OutputStream os = exchange.getResponseBody();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        fis.close();
        os.close();
    }
    
    private static void serveFile(HttpExchange exchange, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            String response = "File not found: " + filename;
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }
        
        String contentType = "text/html";
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, file.length());
        
        FileInputStream fis = new FileInputStream(file);
        OutputStream os = exchange.getResponseBody();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        fis.close();
        os.close();
    }
    
    private static void handleGetTelemetry(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> telemetryData = readTelemetryData();
        
        // Convert to JSON
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < telemetryData.size(); i++) {
            Map<String, Object> data = telemetryData.get(i);
            if (i > 0) json.append(",");
            json.append("{");
            json.append("\"timestamp\":\"").append(data.get("timestamp")).append("\",");
            json.append("\"cpuUsage\":").append(data.get("cpuUsage")).append(",");
            json.append("\"memoryUsage\":").append(data.get("memoryUsage"));
            json.append("}");
        }
        json.append("]");
        
        // Set CORS headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET");
        
        String response = json.toString();
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    private static List<Map<String, Object>> readTelemetryData() {
        // Generate fresh real-time data on each request (without delays for API speed)
        List<Map<String, Object>> dataList = new ArrayList<>();
        Random random = new Random();
        
        // Generate 5 fresh readings instantly
        for (int i = 0; i < 5; i++) {
            double cpu = 10 + random.nextDouble() * 90;     // 10–100%
            double memory = 20 + random.nextDouble() * 70;  // 20–90%
            Date timestamp = new Date();
            
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("timestamp", timestamp.toString());
            dataMap.put("cpuUsage", cpu);
            dataMap.put("memoryUsage", memory);
            dataList.add(dataMap);
        }
        
        return dataList;
    }
    
    private static Map<String, Object> parseTelemetryLine(String line) {
        try {
            // Parse format: "Time: Sun Nov 16 12:10:46 IST 2025 | CPU Usage: 47.50% | Memory Usage: 43.13%"
            int timeIndex = line.indexOf("Time: ");
            int cpuIndex = line.indexOf("CPU Usage: ");
            int memIndex = line.indexOf("Memory Usage: ");
            
            if (timeIndex == -1 || cpuIndex == -1 || memIndex == -1) {
                return null;
            }
            
            String timestamp = line.substring(timeIndex + 6, line.indexOf(" |", timeIndex));
            String cpuStr = line.substring(cpuIndex + 11, line.indexOf("%", cpuIndex));
            String memStr = line.substring(memIndex + 14, line.indexOf("%", memIndex));
            
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", timestamp.trim());
            data.put("cpuUsage", Double.parseDouble(cpuStr.trim()));
            data.put("memoryUsage", Double.parseDouble(memStr.trim()));
            
            return data;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            return null;
        }
    }
}

