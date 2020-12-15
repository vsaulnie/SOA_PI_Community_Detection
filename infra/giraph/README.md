# Docker deployment ressources for a single-cluster Hadoop-based Giraph service

## Images Role
The deployment is decomposed into 3 images, with an ubuntu:xenial as basis
- ubuntu:java8 adds a java8 jdk to the base image as well as common shell utilities
- hadoop:single ads the package and host-configuration to run a hadoop cluster based on hadoop 0.20
- giraph:sdbd adds a jar containing apache giraph core + additionnal algorithms developped during the project

## Build Images
```
sudo bash dockerbuild
```
This will build the threee images in the correct order and associate the correct tags
You only need to run this once. Remove all existing images before rebuilding.
## Launch the container
```
sudo bash deploy
```
A container named giraph will be launched in detached mode with an hadoop single node cluster running.
## Access the container
```
sudo bash connect
```
Will log you into the giraph container as user hduser, with all rights to use hadoop and giraph
## Use hadoop
```
hadoop version
hadoop fs -put src hdfsDest
hadoop fs -mkdir -p /user/hduser/graphs/....
hadoop fs -copyToLocal /user/hduser/output/p* ./result.txt
hadoop fs -rmr /user/hduser
...
```
## Run a giraph job 
The jar /home/hduser/Graphari.jar contains giraph core and some algorithms
Generic syntax and example, see https://giraph.apache.org/ or https://github.com/NyuB/Graphari for more
```
#Template
hadoop jar Graphari.jar org.apache.giraph.GiraphRunner <ComputationClass> -mc <MasterComputeClass> -vif <VertexInputClass> -vip <HdfsInputPath> -vof <VertexOutputClass> -op <HdfsOutputPath> -w 1
#SCC example
hadoop jar Graphari.jar  org.apache.giraph.GiraphRunner insa.sdbd.community.scc.SCCComputation -mc insa.sdbd.community.scc.SCCMasterComputation -vif insa.sdbd.community.formats.vif.JsonLongLongDirectedDouble -vip /user/hduser/graphs/simple.txt -vof insa.sdbd.community.formats.vof.VertexWithLongValueTextOutput -op /user/hduser/output/example -w 1
```
