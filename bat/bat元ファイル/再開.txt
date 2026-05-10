@echo off
cd /d %~dp0
cd ..
start http://localhost:8080
docker-compose up
pause