FROM hadoop:single
WORKDIR /home/hduser
ADD ./Graphari.jar ./Graphari.jar

RUN 	chown hduser:hadoop ./Graphari.jar && \
	echo "Added giraph custom jars, use deployment scripts before using them"

