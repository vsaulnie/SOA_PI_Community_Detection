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
1. Prendre en entrée un graphe au format txt (format téléchargé via le site de Standford) et être capable de le traduire au bon format pour Neo4J et Giraph  
2. Ajouter des graphes traduits aux bon endroits sur les Docker de Neo4J et Giraph  
3. Lancer des exécutions d’algorithmes choisis sur Neo4J et Giraph  
4. Récupérer les résultats des exécutions dans une base de donnée (ou pseudo base)  
5. Charger des graphes au format txt depuis un url local ou web  
6. Initier les ordres de lancement d’exécution directement depuis le frontend  
7. Afficher les résultats d’exécution sur le frontend  

## Architecture

![Latest Architecture](https://github.com/vsaulnie/SOA_PI_Community_Detection/blob/main/doc/architecture.jpg)


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
