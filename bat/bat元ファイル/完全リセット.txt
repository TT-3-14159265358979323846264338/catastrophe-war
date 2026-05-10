@echo off
echo 【警告】データベースのデータも含め、すべて削除されます。
echo application.properties が update モードになっているか確認してください。
echo.

choice /M "本当に削除を実行しますか？"
if errorlevel 2 (
    echo キャンセルしました。
    pause
    exit
)

cd /d %~dp0
cd ..
docker-compose down -v
echo すべて削除されました。
pause