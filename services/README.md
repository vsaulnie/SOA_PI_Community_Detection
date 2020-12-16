# Microservices for graph infrastructures

## discovery
Eureka server for DNS and load-balancing
```
#default listening port is 8082
java -jar target/discovery*.jar --server.port=<port>
```
## launcher
Exposes methods to launch algorithms/requests computation using different solutions
```
#default listening port is 8083
#default eureka server url is http://127.0.0.1:8082/eureka override when deploying on remote hosts
java -jar target/launcher*.jar --server.port=<port> --eureka.client.serviceUrl.defaultZone=http://<addr>:<port>/eureka
```
## storer
Exposes methods to retrieve computation results and graphs from different solutions
```
#default listening port is 8084
#default eureka server url is http://127.0.0.1:8082/eureka override when deploying on remote hosts
java -jar target/storer*.jar --server.port=<port> --eureka.client.serviceUrl.defaultZone=http://<addr>:<port>/eureka
```
## giraph
Endpoint exposing a REST API to our Apache Giraph based solution(and hadoop cluster)
Designed to be deployed along with a dockerized giraph infrastructure (see /infra/giraph )
```
#default listening port is 8888
java -jar target/giraph*.jar --server.port=<port>
```
