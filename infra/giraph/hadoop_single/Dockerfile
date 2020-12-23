FROM ubuntu:java8
USER root
WORKDIR /usr/local
ADD ./*-site.xml ./
ADD ./hadoop_* ./
ADD ./startup /bin/startup
RUN 	groupadd hadoop && \
	useradd -ms /bin/bash --groups hadoop,sudo hduser && \
	wget http://archive.apache.org/dist/hadoop/core/hadoop-0.20.203.0/hadoop-0.20.203.0rc1.tar.gz && \
	tar xzf hadoop-0.20.203.0rc1.tar.gz && \
	mv hadoop-0.20.203.0 hadoop && \
	mv ./*-site.xml hadoop/conf/ && \
	chown -R hduser:hadoop hadoop && \
	mkdir -p /app/hadoop/tmp && \
	chown hduser:hadoop /app/hadoop/tmp && \
	chmod 750 /app/hadoop/tmp && \
	echo '$HADOOP_HOME/bin/hadoop "$@"' > /usr/bin/hadoop && \
	chmod +x /usr/bin/hadoop && \
	chown hduser:hadoop /usr/bin/hadoop && \
	mv hadoop_* /usr/bin/ && \
	chmod +x /usr/bin/hadoop_start && \
	chmod +x /usr/bin/hadoop_stop && \
	chmod +x /bin/startup && \
	chown hduser:hadoop /usr/bin/hadoop_start && \
	chown hduser:hadoop /usr/bin/hadoop_start && \
	apt-get install openssh-server -y && \
	apt-get install sudo -y

USER hduser
WORKDIR /home/hduser
RUN 	echo 'export HADOOP_HOME=/usr/local/hadoop' >> /home/hduser/.bashrc && \
	echo 'export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64' >> /home/hduser/.bashrc && \
	echo 'export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64' >> /usr/local/hadoop/conf/hadoop-env.sh && \
	echo 'export HADOOP_OPTS=-Djava.net.preferIPv4Stack=true' >> /usr/local/hadoop/conf/hadoop-env.sh && \
	ssh-keygen -t rsa -P "" -f /home/hduser/.ssh/id_rsa && \
	cat /home/hduser/.ssh/id_rsa.pub >> /home/hduser/.ssh/authorized_keys && \
	echo hdnode01 > /usr/local/hadoop/conf/masters && \
	echo hdnode01 > /usr/local/hadoop/conf/slaves
USER root
WORKDIR /
RUN	echo "Switched back to root"
CMD ["/bin/bash"]
