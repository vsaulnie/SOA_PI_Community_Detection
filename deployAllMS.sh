#!/bin/bash

HERE=$PWD

ms_name='Discovery_MS'
cd ../$msname/EurekaServer 
gnome-terminal -e 'sh -c "mvn spring-boot:run"'
cd $HERE

ms_name='Launcher_MS'
cd ../$msname/launcher 
gnome-terminal -e 'sh -c "mvn spring-boot:run"'
cd $HERE

ms_name='Giraph_API_MS'
cd ../$msname/giraph 
gnome-terminal -e 'sh -c "mvn spring-boot:run"'
cd $HERE

ms_name='Storer_MS'
cd ../$msname/storer 
gnome-terminal -e 'sh -c "mvn spring-boot:run"'
cd $HERE

exit


