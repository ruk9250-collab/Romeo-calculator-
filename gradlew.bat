@echo off
set DIRNAME=%~dp0
"%JAVA_HOME%\bin\java.exe" -jar "%DIRNAME%gradle\wrapper\gradle-wrapper.jar" %*
