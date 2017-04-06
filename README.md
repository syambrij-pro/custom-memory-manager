# custom-memory-manager
A custom memory manager in java to persist your java objects in disks(off heap space). So You don't need to worry about heap space. To handle persistance of billion objects project uses Apache Spark framework. 
The basic Idea is to save your objects in local files/HDFS or simply in disk other than Java application heap space. Once you have saved every object with it's associated name you can easily delete the object or retrieve the object by specifying name. There is interface for persisting object/objects by calling single method. Also there is interface for evicting/retrieving object/objects by calling single method.

# Prerequisites:

# Apache Spark 2.0.2
# Java 1.8
# Maven 3.1.0

Setup Guide lines:

simply try to run "mvn clean install" in project directory after checkout.
And then run "MemoryManager" class in "com.memory.manager.tester" package. This class is just to run/test the application.   Please change "folder path" passed in "writer" method.

Other Details:

Persistor Interface - com.memory.manager.prototype.Persistor                                                               
Deallocator Interface- com.memory.manager.prototype.Deallocator


Performace Benchmarking:                                                                                             

For 1 million objects on PC - Intel® Core™ i3-3220 CPU @ 3.30GHz × 4  CPU:                                                                                                         

Using Java 1.7-  It took 109 to 114 seconds.                                                                              
Using Java 1.8(parallel stream api)-  It took 85 to 90 seconds.                                                        

On single machine standalone node mode Apache Spark took 95 to 99 seconds.(Not an ideal environment to run Apache Spark.)

Sooner I will add spark performance with multiple nodes. It is supposed to improve performance dramatically for billion objects.
