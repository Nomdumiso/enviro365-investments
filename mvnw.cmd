@echo off
rem ----------------------------------------------------------------------------
rem maven wrapper (Windows)
rem ----------------------------------------------------------------------------
setlocal
set PRG=%~dp0%~nx0
set MAVEN_WRAPPER_DIR=%~dp0.mvn\wrapper
if not exist "%MAVEN_WRAPPER_DIR%\maven-wrapper.jar" (
  echo maven-wrapper.jar not found in %MAVEN_WRAPPER_DIR%
  echo Attempting to download maven-wrapper.jar from Maven Central...
  if exist "%MAVEN_WRAPPER_DIR%" goto _found_dir
  mkdir "%MAVEN_WRAPPER_DIR%"
_found_dir
  set PROPERTIES_FILE=%MAVEN_WRAPPER_DIR%\maven-wrapper.properties
  if exist "%PROPERTIES_FILE%" (
    for /f "usebackq tokens=1* delims==" %%a in ("%PROPERTIES_FILE%") do (
      if "%%a"=="distributionUrl" set DISTRIBUTION_URL=%%b
    )
  ) else (
    set DISTRIBUTION_URL=https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar
  )
  powershell -Command "(New-Object System.Net.WebClient).DownloadFile('%DISTRIBUTION_URL%', '%MAVEN_WRAPPER_DIR%\\maven-wrapper.jar')"
)
java -jar "%MAVEN_WRAPPER_DIR%\maven-wrapper.jar" %*
endlocal
