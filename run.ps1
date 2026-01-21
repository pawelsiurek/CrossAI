$ErrorActionPreference = "Stop"

function Print-Header {
    param([string]$text)
    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host " $text" -ForegroundColor Cyan
    Write-Host "========================================`n" -ForegroundColor Cyan
}

function Check-ExitCode {
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error encountered. Stopping pipeline." -ForegroundColor Red
        exit 1
    }
}

# ==========================================
# PHASE 1: C++ Build & Test
# ==========================================
Print-Header "PHASE 1: Building and Testing C++ Core"

if (-not (Test-Path "cpp/build")) {
    Write-Host "Creating cpp/build directory..."
    New-Item -ItemType Directory -Force -Path "cpp/build" | Out-Null
    Set-Location "cpp/build"
    cmake ..
} else {
    Set-Location "cpp/build"
}

Write-Host "Compiling C++..." -ForegroundColor Yellow
cmake --build . --config Debug
Check-ExitCode

Write-Host "Running C++ Tests..." -ForegroundColor Yellow
ctest -C Debug --output-on-failure
Check-ExitCode

Write-Host "C++ Verified!" -ForegroundColor Green

# ==========================================
# PHASE 2: Java Build & Test
# ==========================================
Set-Location ../../java

Print-Header "PHASE 2: Building & Testing Java App"

Write-Host "Cleaning and Compiling Java..." -ForegroundColor Yellow
cmd /c mvn clean compile
Check-ExitCode

Write-Host "Running Java Tests..." -ForegroundColor Yellow
cmd /c mvn test
Check-ExitCode

Write-Host "Java Verified!" -ForegroundColor Green

# ==========================================
# PHASE 3: Execution
# ==========================================
Print-Header "PHASE 3: Launching Application"

Write-Host "Starting CrossAI..." -ForegroundColor Green
cmd /c mvn exec:java