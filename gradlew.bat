@echo off
setlocal
set GRADLE_VERSION=8.5
set DIST_URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip
set BASE_DIR=%~dp0
set WRAPPER_DIR=%BASE_DIR%\.gradle\wrapper\dists
set DIST_DIR=%WRAPPER_DIR%\gradle-%GRADLE_VERSION%-bin
set ZIP_PATH=%DIST_DIR%\gradle-%GRADLE_VERSION%-bin.zip
set UNZIPPED_BIN=%DIST_DIR%\gradle-%GRADLE_VERSION%\bin\gradle.bat

if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"

if not exist "%ZIP_PATH%" (
  echo Descargando Gradle %GRADLE_VERSION%...
  where curl >nul 2>nul
  if %ERRORLEVEL%==0 (
    curl -fL "%DIST_URL%" -o "%ZIP_PATH%"
  ) else (
    where wget >nul 2>nul
    if %ERRORLEVEL%==0 (
      wget -O "%ZIP_PATH%" "%DIST_URL%"
    ) else (
      echo Necesitas curl o wget para descargar Gradle.
      exit /b 1
    )
  )
)

if not exist "%UNZIPPED_BIN%" (
  echo Descomprimiendo Gradle...
  powershell -Command "Expand-Archive -Path '%ZIP_PATH%' -DestinationPath '%DIST_DIR%' -Force"
)

call "%UNZIPPED_BIN%" %*
