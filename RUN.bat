@echo off
echo ========================================
echo   ClassLinker Web App - Starting...
echo ========================================
echo.

set PROJECT_DIR=%~dp0
set MAVEN_DIR=%PROJECT_DIR%maven
set MVN_EXE=%MAVEN_DIR%\bin\mvn.cmd

REM Check if Maven already downloaded
if exist "%MVN_EXE%" goto RUN_APP

echo Maven not found. Downloading Maven 3.9.6 (one-time setup ~8MB)...
echo Please wait...

powershell -Command "Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile '%PROJECT_DIR%maven.zip' -UseBasicParsing"

echo Extracting Maven...
powershell -Command "Expand-Archive -Path '%PROJECT_DIR%maven.zip' -DestinationPath '%PROJECT_DIR%maven_tmp' -Force"
powershell -Command "Move-Item -Path '%PROJECT_DIR%maven_tmp\apache-maven-3.9.6' -Destination '%MAVEN_DIR%' -Force"
powershell -Command "Remove-Item -Path '%PROJECT_DIR%maven.zip' -Force"
powershell -Command "Remove-Item -Path '%PROJECT_DIR%maven_tmp' -Recurse -Force"

echo Maven downloaded successfully!
echo.

:RUN_APP
echo Starting ClassLinker on http://localhost:8080
echo Press Ctrl+C to stop.
echo.
"%MVN_EXE%" -f "%PROJECT_DIR%pom.xml" spring-boot:run
pause
