# SOA_PI_Community_Detection - Launcher MicroService
A microservice based application for the comparision of community detection solutions on large graphs

## Developpement setup
IDE : Eclipse 2019  
Language : Java  
JDK : Java 8  
Continuous integration : Jenkins 2.222.3  
Serveur : Tomcat 7  
Microservice framework : Spring boot  

## Intent
Microservice that load a graph in txt or gz format, convert it (eg. to csv or json) and send it to Neo4J or Giraph through their API. 
It also makes request on all available solutions.

