#export JAVA_HOME=/usr/java/jdk.1.5.0_12
#export PATH=$PATH:$JAVA_HOME
sudo nohup java -server -Xmx2048m -Xms512m -Doracle.jdbc.autoCommitSpecCompliant=false -Dfile.encoding=UTF-8 -jar app-bazresi-*.jar --server.port=8881 & echo $! > ./pid.file &
tail -f nohup.out
