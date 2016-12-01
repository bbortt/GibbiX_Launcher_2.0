@ECHO off

TITLE VMLauncher 2.0

CALL lib/figbat /f Doom "VMLauncher 2.0"
                                                            
:: This script launches VMLauncher 2.0 as long as the maven-shade-plugin does not work properly!
:: @author: Timon Borter
:: @version: 1.0.0

ECHO "---------------------------------------------------------------"
ECHO Setting temporary environment variables

ECHO M2_HOME to  %~dp0lib\apache-maven-3.3.9
SET M2_HOME=%~dp0lib\apache-maven-3.3.9

ECHO JAVA_HOME to  %~dp0jdklib\1.8.0_101
SET JAVA_HOME=%~dp0lib\jdk1.8.0_101

ECHO "---------------------------------------------------------------"
ECHO Launching maven with temporal properties
call %~dp0lib\apache-maven-3.3.9\bin\mvn clean install exec:java

EXIT