#!/bin/bash
# This script launches VMLauncher 2.0 as long as the maven-shade-plugin does not work properly!
# @author: Timon Borter
# @version: 1.0.0

cat << "EOF"
--------------------------------------------------------------
---------------------------------------------------------------
 __   ____  __ _                      _              ___   __  
 \ \ / /  \/  | |   __ _ _  _ _ _  __| |_  ___ _ _  |_  ) /  \ 
  \ V /| |\/| | |__/ _` | || | ' \/ _| ' \/ -_) '_|  / / | () |
   \_/ |_|  |_|____\__,_|\_,_|_||_\__|_||_\___|_|   /___(_)__/ 
---------------------------------------------------------------
EOF

echo "---------------------------------------------------------------"
echo "Setting temporary environment variables.."

echo "M2_HOME to "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && PWD )"/apache-maven-3.3.9"
set M2_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && PWD )"/lib/apache-maven-3.3.9

echo "JAVA_HOME to "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && PWD )"/jdklib/1.8.0_101"
set JAVA_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && PWD )"/lib/jdk1.8.0_101

echo "---------------------------------------------------------------"
echo "Launching maven with temporal properties.."
./lib/apache-maven-3.3.9/bin/mvn clean install exec:java

exit 0