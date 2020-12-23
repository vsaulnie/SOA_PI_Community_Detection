FROM hadoop:single
WORKDIR /home/hduser
ADD ./Graphari.jar ./Graphari.jar
ADD ./giraph*.jar ./SpringService.jar

RUN 	chown hduser:hadoop ./Graphari.jar && \
	chown hduser:hadoop ./SpringService.jar && \
	echo "Added giraph custom jars and spring service, use deployment scripts before using them"

