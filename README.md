# Telemetry Processor

A real-time telemetry data processing system with a modern web dashboard for monitoring CPU and Memory usage.

👥 Contributors

M. Aseena Sulthana – SRM Institute of Science and Technology (SRMIST)

Amanpreet – SRM Institute of Science and Technology (SRMIST)

## Features

- 🚀 **Real-Time Data Processing** - Generate and process telemetry data in real-time
- 📊 **Interactive Dashboard** - Beautiful dark-themed dashboard with live data visualization
- 📈 **Analytics Page** - View trends and performance metrics
- 🔄 **RESTful API** - JSON API endpoint for telemetry data
- 🎨 **Modern UI** - Responsive design with animated background and particles

## Project Structure

```
Telemetry Processor/
├── TelemetryProcessor.java    # Core telemetry data processing
├── TelemetryServer.java       # HTTP server for web interface
├── index.html                 # Landing page
├── dash.html                  # Dashboard page
├── analysis.html              # Analytics page
├── how it works.html          # How it works page
├── styles.css                 # Global stylesheet
├── script.js                  # JavaScript functionality
└── README.md                  # This file
```

## Prerequisites

- Java JDK 8 or higher
- Web browser (Chrome, Firefox, Edge, etc.)

## How to Run

1. **Compile the Java files:**
   ```bash
   javac TelemetryProcessor.java
   javac TelemetryServer.java
   ```

2. **Start the server:**
   ```bash
   java TelemetryServer
   ```

3. **Open your browser:**
   - Landing Page: `http://localhost:8080/`
   - Dashboard: `http://localhost:8080/dash.html`
   - Analytics: `http://localhost:8080/analysis.html`
   - How It Works: `http://localhost:8080/how it works.html`

## Usage

### Generate Telemetry Data

Run the main processor to generate sample data:
```bash
java TelemetryProcessor
```

This will generate 5 telemetry readings and save them to `telemetry_output.txt`.

### View Dashboard

1. Start the server (see above)
2. Navigate to `http://localhost:8080/dash.html`
3. Click "🔄 Fetch Telemetry Data" button
4. View real-time CPU and Memory usage metrics

## API Endpoints

- `GET /api/telemetry` - Returns JSON array of telemetry data
  ```json
  [
    {
      "timestamp": "Sun Nov 16 12:10:46 IST 2025",
      "cpuUsage": 47.50,
      "memoryUsage": 43.13
    }
  ]
  ```

## Technologies Used

- **Backend:** Java (HttpServer)
- **Frontend:** HTML5, CSS3, JavaScript
- **Data Format:** JSON

## Features Breakdown

### Landing Page
- Modern dark theme with animated background
- Navigation to all pages
- Call-to-action buttons

### Dashboard
- Real-time telemetry data fetching
- Visual progress bars for CPU and Memory
- Summary statistics (averages, max values)
- Responsive card-based layout

### Analytics
- Performance metrics visualization
- Trend charts
- Export functionality (UI ready)

### How It Works
- Step-by-step process explanation
- Architecture highlights
- Integration examples

📄 License

This project is open-source and created for learning and educational purposes under Wipro’s Value Added Course.

## Contributing

Feel free to fork this project and submit pull requests for any improvements.

