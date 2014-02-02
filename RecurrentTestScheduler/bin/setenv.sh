#
# Alerting Environment
#

echo Setting the Recurrent Test Scheduler environment..

export JAVA_HOME=/usr/java/jdk1.6.0_26
export PATH="$JAVA_HOME/bin":"$PATH"

SCHEDULER_HOME=`dirname "$0"`
export SCHEDULER_HOME=`cd "$SCHEDULER_HOME/.."; pwd`

LOG_DIR=/dfs1/ruxit/scheduler/recur
if [ -d $LOG_DIR ]; then LOG_BASE=$LOG_DIR
else
   LOG_BASE=$SCHEDULER_HOME
fi 

export SCHEDULER_LOG=$LOG_BASE/logs/
export SCHEDULER_CFG=$SCHEDULER_HOME/config
export SCHEDULER_LIB=$SCHEDULER_HOME/lib

export SCHEDULER_CLASSPATH=$SCHEDULER_LIB/bootstrap-1.0.0.jar

export SCHEDULER_OPTS="-Dcompuware.application.home=$SCHEDULER_HOME -Dcompuware.application.engine=com.compuware.ruxit.synthetic.scheduler.recur.engine.RecurrentTestSchedulingEngine -Duser.timezone=GMT -Dlog.extra.appenders=,daily_rolling -Dlog.dir=$SCHEDULER_LOG"

# choose a port
if [ -s /usr/bin/netcat ]; then export JMX_PORT=`netcat -zvv localhost 15200-15220 2>&1 | grep ".*refused" | awk '{print $3}'|grep -m 1 "[0-9]\{5\}"`
elif [ -s /usr/bin/nc ]; then export JMX_PORT=`nc -zvv localhost 15200-15220 2>&1 | grep ".*refused" | awk '{print $6}'|grep -m 1 "[0-9]\{5\}"`
else
   echo "JMX_PORT could not be determined"
   export JMX_PORT=50844
fi


echo --------------------------
echo Home Dir:   $SCHEDULER_HOME
echo Config Dir: $SCHEDULER_CFG
echo Lib Dir:    $SCHEDULER_LIB
echo Log Dir:    $SCHEDULER_LOG
echo Classpath:  $SCHEDULER_CLASSPATH
echo Java Opts:  $SCHEDULER_OPTS
echo JMX port:   $JMX_PORT
echo --------------------------