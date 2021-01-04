#!/bin/bash

#JAVA_HOME used : usr/lib/jvm/java-11-openjdk-amd64

cd services/

#Launch discovery service 
gnome-terminal -- /bin/bash -c "./start_discovery.sh"

#Launch storer service 
gnome-terminal -- /bin/bash -c "./start_storer.sh"

#Launch launcher service 
gnome-terminal -- /bin/bash -c "./start_launcher.sh"


