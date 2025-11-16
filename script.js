// Create animated particles
function createParticles() {
    const background = document.querySelector('.animated-background');
    if (!background) return;

    for (let i = 0; i < 50; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.animationDelay = Math.random() * 15 + 's';
        particle.style.animationDuration = (10 + Math.random() * 10) + 's';
        background.appendChild(particle);
    }
}

// Mobile menu toggle
function initMobileMenu() {
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('navMenu');

    if (hamburger && navMenu) {
        hamburger.addEventListener('click', () => {
            navMenu.classList.toggle('active');
        });

        // Close menu when clicking on a link
        const navLinks = navMenu.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', () => {
                navMenu.classList.remove('active');
            });
        });
    }
}

// Theme toggle (placeholder for future implementation)
function initThemeToggle() {
    const themeToggle = document.getElementById('themeToggle');
    if (themeToggle) {
        themeToggle.addEventListener('click', () => {
            // Theme toggle functionality can be added here
            console.log('Theme toggle clicked');
        });
    }
}

// Telemetry data fetching
async function fetchTelemetryData() {
    const fetchBtn = document.getElementById('fetchBtn');
    const loading = document.getElementById('loading');
    const errorMessage = document.getElementById('errorMessage');
    const dashboard = document.getElementById('dashboard');
    const emptyState = document.getElementById('emptyState');
    const statsSummary = document.getElementById('statsSummary');

    if (!fetchBtn) return;

    // Disable button and show loading
    fetchBtn.disabled = true;
    if (loading) loading.classList.add('show');
    if (errorMessage) errorMessage.classList.remove('show');
    if (dashboard) dashboard.innerHTML = '';
    if (emptyState) emptyState.style.display = 'none';
    if (statsSummary) statsSummary.style.display = 'none';

    try {
        const response = await fetch('http://localhost:8080/api/telemetry');
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        
        if (data.length === 0) {
            if (emptyState) {
                emptyState.textContent = 'No telemetry data available';
                emptyState.style.display = 'block';
            }
        } else {
            if (dashboard) displayTelemetryData(data);
            if (statsSummary) {
                calculateStats(data);
                statsSummary.style.display = 'block';
            }
            if (emptyState) emptyState.style.display = 'none';
        }
    } catch (error) {
        if (errorMessage) {
            errorMessage.textContent = `Error fetching data: ${error.message}. Make sure the server is running on port 8080.`;
            errorMessage.classList.add('show');
        }
        if (emptyState) emptyState.style.display = 'block';
    } finally {
        if (fetchBtn) fetchBtn.disabled = false;
        if (loading) loading.classList.remove('show');
    }
}

function displayTelemetryData(data) {
    const dashboard = document.getElementById('dashboard');
    if (!dashboard) return;

    dashboard.innerHTML = '';

    data.forEach((item, index) => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <div class="card-header">
                <div class="card-title">Reading #${index + 1}</div>
                <div class="timestamp">${item.timestamp}</div>
            </div>
            <div class="metric">
                <div class="metric-label">
                    <span>CPU Usage</span>
                    <span class="metric-value">${item.cpuUsage.toFixed(2)}%</span>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill cpu-progress" style="width: ${item.cpuUsage}%">
                        ${item.cpuUsage.toFixed(1)}%
                    </div>
                </div>
            </div>
            <div class="metric">
                <div class="metric-label">
                    <span>Memory Usage</span>
                    <span class="metric-value">${item.memoryUsage.toFixed(2)}%</span>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill memory-progress" style="width: ${item.memoryUsage}%">
                        ${item.memoryUsage.toFixed(1)}%
                    </div>
                </div>
            </div>
        `;
        dashboard.appendChild(card);
    });
}

function calculateStats(data) {
    const totalReadings = data.length;
    const avgCpu = data.reduce((sum, item) => sum + item.cpuUsage, 0) / totalReadings;
    const avgMemory = data.reduce((sum, item) => sum + item.memoryUsage, 0) / totalReadings;
    const maxCpu = Math.max(...data.map(item => item.cpuUsage));

    const totalReadingsEl = document.getElementById('totalReadings');
    const avgCpuEl = document.getElementById('avgCpu');
    const avgMemoryEl = document.getElementById('avgMemory');
    const maxCpuEl = document.getElementById('maxCpu');

    if (totalReadingsEl) totalReadingsEl.textContent = totalReadings;
    if (avgCpuEl) avgCpuEl.textContent = avgCpu.toFixed(2) + '%';
    if (avgMemoryEl) avgMemoryEl.textContent = avgMemory.toFixed(2) + '%';
    if (maxCpuEl) maxCpuEl.textContent = maxCpu.toFixed(2) + '%';
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    createParticles();
    initMobileMenu();
    initThemeToggle();

    // Auto-fetch if on dashboard page
    if (window.location.pathname.includes('dash.html') || window.location.pathname.includes('dashboard.html')) {
        // Optionally auto-fetch on load
        // fetchTelemetryData();
    }
});

// Auto-refresh functionality
let autoRefreshInterval = null;
let isAutoRefreshOn = false;

function toggleAutoRefresh() {
    const toggleBtn = document.getElementById('autoRefreshToggle');
    
    if (isAutoRefreshOn) {
        // Turn off auto-refresh
        clearInterval(autoRefreshInterval);
        isAutoRefreshOn = false;
        if (toggleBtn) {
            toggleBtn.textContent = '⏸️ Auto-Refresh: OFF';
            toggleBtn.classList.remove('btn-primary');
            toggleBtn.classList.add('btn-secondary');
        }
    } else {
        // Turn on auto-refresh (fetch every 3 seconds)
        fetchTelemetryData(); // Fetch immediately
        autoRefreshInterval = setInterval(() => {
            fetchTelemetryData();
        }, 3000); // Refresh every 3 seconds
        isAutoRefreshOn = true;
        if (toggleBtn) {
            toggleBtn.textContent = '▶️ Auto-Refresh: ON';
            toggleBtn.classList.remove('btn-secondary');
            toggleBtn.classList.add('btn-primary');
        }
    }
}

// Make functions available globally
window.fetchTelemetryData = fetchTelemetryData;
window.toggleAutoRefresh = toggleAutoRefresh;

