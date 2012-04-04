#!/bin/bash
set -e
# get dirs
DAEMON_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BIN_DIR="$( dirname "$DAEMON_DIR" )"
APP_DIR="$( dirname "$BIN_DIR" )"
# get paths
PID_FILE="$APP_DIR"/.pid
# get java
JAVA="$( "$DAEMON_DIR"/find-java.sh )"
# check pid file
if [ -f "$PID_FILE" ] ; then
    echo "Pid file exists, path: "$PID_FILE", pid: $( cat "$PID_FILE" )"
    exit 1
fi
# set JVM arguments
JVM_ARGS="-Dlogback.configurationFile=$APP_DIR/config/logger.xml -cp $APP_DIR/lib/*:$BIN_DIR/wallcology-notifier.jar"
# startup
"$JAVA" $JVM_ARGS "ltg.es.wallcology.notifier.Notifier" "$APP_DIR/config/notifier.xml" &
PID="$!"
echo "$PID" > "$PID_FILE"
echo "Started wallcology-notifier-agent, pid: $PID"
