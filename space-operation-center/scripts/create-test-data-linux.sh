#!/bin/bash

# ============================================
# Скрипт для создания тестовых данных
# для проверки Mission Scheduler
# ============================================

BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Настройка тестовых данных для Mission Scheduler${NC}"
echo "=============================================="

# Проверка доступности сервера
echo -e "${YELLOW}Проверка подключения к серверу на порту 8080...${NC}"
if ! curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/overview" | grep -q "200"; then
    echo -e "${RED} Сервер не доступен! Запустите space-operation-center на порту 8080${NC}"
    exit 1
fi
echo -e "${GREEN} Сервер доступен${NC}"
echo ""

# ============================================
# 1. GeoStationary
# ============================================
echo -e "${YELLOW} Создание группировки GeoStationary...${NC}"
curl -X POST "$BASE_URL/add-satellites" \
  -H "Content-Type: application/json" \
  -d '{
    "constellationName": "GeoStationary",
    "satelliteParams": [
      {
        "type": "COMMUNICATION",
        "name": "Geo-Sat-1",
        "batteryLevel": 100.0,
        "bandwidth": 500
      },
      {
        "type": "COMMUNICATION",
        "name": "Geo-Sat-2",
        "batteryLevel": 100.0,
        "bandwidth": 500
      }
    ]
  }'

if [ $? -eq 0 ]; then
    echo -e "${GREEN} GeoStationary создана${NC}"
else
    echo -e "${RED} Ошибка создания GeoStationary${NC}"
fi
echo ""

# ============================================
# 2. LowOrbit
# ============================================
echo -e "${YELLOW}  Создание группировки LowOrbit...${NC}"
curl -X POST "$BASE_URL/add-satellites" \
  -H "Content-Type: application/json" \
  -d '{
    "constellationName": "LowOrbit",
    "satelliteParams": [
      {
        "type": "IMAGE",
        "name": "Sat-1",
        "batteryLevel": 100.0,
        "resolution": 96
      },
      {
        "type": "IMAGE",
        "name": "Sat-2",
        "batteryLevel": 100.0,
        "resolution": 128
      }
    ]
  }'

if [ $? -eq 0 ]; then
    echo -e "${GREEN} LowOrbit создана${NC}"
else
    echo -e "${RED} Ошибка создания LowOrbit${NC}"
fi
echo ""

# ============================================
# 3. TestConstellation
# ============================================
echo -e "${YELLOW} Создание группировки TestConstellation...${NC}"
curl -X POST "$BASE_URL/add-satellites" \
  -H "Content-Type: application/json" \
  -d '{
    "constellationName": "TestConstellation",
    "satelliteParams": [
      {
        "type": "COMMUNICATION",
        "name": "Test-Sat-1",
        "batteryLevel": 100.0,
        "bandwidth": 100
      }
    ]
  }'

if [ $? -eq 0 ]; then
    echo -e "${GREEN} TestConstellation создана${NC}"
else
    echo -e "${RED} Ошибка создания TestConstellation${NC}"
fi
echo ""

# ============================================
# 4. Активация всех спутников
# ============================================
echo -e "${YELLOW} Активация всех спутников...${NC}"
curl -X POST "$BASE_URL/activateAllSatellites" \
  -H "Content-Type: application/json"

if [ $? -eq 0 ]; then
    echo -e "${GREEN} Все спутники активированы${NC}"
else
    echo -e "${RED} Ошибка активации${NC}"
fi
echo ""