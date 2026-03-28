# ============================================
# Скрипт для создания тестовых данных
# для проверки Mission Scheduler (Windows)
# ============================================

$BASE_URL = "http://localhost:8080/api"

Write-Host "Настройка тестовых данных для Mission Scheduler" -ForegroundColor Yellow
Write-Host "==============================================" -ForegroundColor Cyan

# Проверка доступности сервера
Write-Host "Проверка подключения к серверу на порту 8080..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$BASE_URL/overview" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "Сервер доступен" -ForegroundColor Green
} catch {
    Write-Host "Сервер не доступен! Запустите space-operation-center на порту 8080" -ForegroundColor Red
    exit 1
}
Write-Host ""

# ============================================
# 1. GeoStationary
# ============================================
Write-Host "Создание группировки GeoStationary..." -ForegroundColor Yellow
$body = @{
    constellationName = "GeoStationary"
    satelliteParams = @(
        @{
            type = "COMMUNICATION"
            name = "Geo-Sat-1"
            batteryLevel = 100.0
            bandwidth = 500
        },
        @{
            type = "COMMUNICATION"
            name = "Geo-Sat-2"
            batteryLevel = 100.0
            bandwidth = 500
        }
    )
} | ConvertTo-Json -Depth 5

try {
    Invoke-RestMethod -Uri "$BASE_URL/add-satellites" -Method POST -ContentType "application/json" -Body $body
    Write-Host "GeoStationary создана" -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# 2. LowOrbit
# ============================================
Write-Host "Создание группировки LowOrbit..." -ForegroundColor Yellow
$body = @{
    constellationName = "LowOrbit"
    satelliteParams = @(
        @{
            type = "IMAGE"
            name = "Sat-1"
            batteryLevel = 100.0
            resolution = 96
        },
        @{
            type = "IMAGE"
            name = "Sat-2"
            batteryLevel = 100.0
            resolution = 128
        }
    )
} | ConvertTo-Json -Depth 5

try {
    Invoke-RestMethod -Uri "$BASE_URL/add-satellites" -Method POST -ContentType "application/json" -Body $body
    Write-Host "LowOrbit создана" -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# 3. TestConstellation
# ============================================
Write-Host "Создание группировки TestConstellation..." -ForegroundColor Yellow
$body = @{
    constellationName = "TestConstellation"
    satelliteParams = @(
        @{
            type = "COMMUNICATION"
            name = "Test-Sat-1"
            batteryLevel = 100.0
            bandwidth = 100
        }
    )
} | ConvertTo-Json -Depth 5

try {
    Invoke-RestMethod -Uri "$BASE_URL/add-satellites" -Method POST -ContentType "application/json" -Body $body
    Write-Host "TestConstellation создана" -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}
Write-Host ""

# ============================================
# 4. Активация всех спутников
# ============================================
Write-Host "Активация всех спутников..." -ForegroundColor Yellow
try {
    Invoke-RestMethod -Uri "$BASE_URL/activateAllSatellites" -Method POST -ContentType "application/json" -Body "{}"
    Write-Host "Все спутники активированы" -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}
Write-Host ""
