# SOA_PI_Community_Detection
A microservice based application for the comparision of community detection solutions on large graphs

## Developpement setup
IDE : Eclipse 2019  
Language : Java  
JDK : Java 8  
Continuous integration : Jenkins 2.222.3  
Serveur : Tomcat 7  
Microservice framework : Spring boot  


## Intent
Implement a SOA/Microservice based application related to integration project of 5 SDBD.  
The project is about community detection on large graph.  
The objective is to make a complete comparision of main graph storage and processing systems. Especially while running community detection algorithms.  

## Specs
1. Take in input a .txt graph file (a file downloaded on http://snap.stanford.edu/mappr/data.html) and translate it to be usable on Neo4J and Giraph  
2. Add translated graphs at the right place on Neo4J and Giraph's dockers  
3. Launch execution of choosen algorithms on Neo4J and Giraph  
4. Get executions results in a database  
5. Load graphs from a local or web url through a frontend  
6. Initiate the launch of execution orders through a frontend  
7. Display execution results on a frontend  

## Architecture

![Latest Architecture](https://github.com/vsaulnie/SOA_PI_Community_Detection/blob/main/doc/Architecutre_v2.jpg)


## Sample Graphs
All graphs can be find in /graphs
They are found on snap.stanford.edu/mappr/data.html: 
- email (directed n=1005 ; e=25 571)
- Amazon (undirected n=334 863 ; e=925 872)
- Friendster (undirected n=65 608 366 ; e=1 806 067 135)

## Launch commands

### Jenkins 
`java -jar jenkins.war --httpPort=9090`


## Project Management 
See all details in `project_management/` 
### Agile software development/Scrum/INVEST 


### JIRA 
