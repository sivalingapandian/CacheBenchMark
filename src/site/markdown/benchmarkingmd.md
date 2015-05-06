Cache BenchMarking
==================

### Introduction

Purpose of this document is to outline steps taken to determine best cache implementation for configuration
management.

### Requirement

Following are basic requirements which needs to be addressed as part of design.

  * Centralized configuration management

  * One tool to admin configuration

  * Performance should not be affected

  * Configuration changes need to get effective immediate or near immediate

  * No service/component outage

### Design Options

Following design options have been considered

#### In-memory Cache (ehCache & infinispan embedded)

EhCache and Infinispan caches can be used as in-memory cache and widely used by multiple framework including
Hibernate.
Implemented as below within Connex product.

**in-memory cache**

![in-memory cache](./images/inmemorycache.jpg "In memory") 

#### Standalone distributed Cache

Infinispan standalone implementation has been taken for benchmarking, since this supports out-of-box rest interface
and easy to install/maintain. Implemented as below within Connex product.

**distributed stand-alone cache**

![distributed stand-alone cache](./images/distributedstandalone.jpg "distributed stand-alone cache") 

#### Embedded data-grid (infinispan)

Infinispan forms cluster of distributed in-memory cache to achieve performance and avoid making external call
overhead. This will be very helpful when there is large data which needs to be shared between distributed application.
Infinispan takes care of replicating/distributing data between cluster that helps enterprise application focus on
business logic. Implemented as below within Connex product.

**embedded data-grid cache**

![embedded data-grid cache](./images/embeddeddatagrid.jpg "embedded data-grid cache") 

#### Stand-alone facility database access

This is not an option we would like to implement, but considered this for benchmarking with external cache approaches.

**Stand alone facility database**

![Stand alone facility database](./images/standalonefacilitydatabase.jpg "Stand alone facility database")

#### Centralized database access (corporate facility)

This is not an option we would like to implement, but considered this for benchmarking with external cache approaches.
In this scenario database resides at corporate

**Stand alone facility database**

![Stand alone facility database](./images/corporatedatabase.jpg "Stand alone facility database")


### BenchMarking

To determine best approach each design has been validated with requirement and one of the requirement is to see very
little performance degradation. This has been measured with JMeter Java Sampler which does 10 threads with 1000 test
execution. Each test reads 10 configuration values from memory.

**Note:** This metrics collection does not include loading data into cache

##### Benchmarking data

Type of Implementation           / Total elapsed time (ms) / Average time (ms per 10 configuration) 

ehcache                          /                     394 /                    0.0394
 
stand alone                      /                  306609 /                   30.6609
 
facility database                /                  317090 /                   31.7090
 
corp dataabse                    /                 1550589 /                  155.0727
 
infinispan embedded              /                     494 /                    0.0494
 
infinispan embedded data grid    /                     101 /                    0.0101
 


##### BenchMarking graph

![BenchMarking graph](./images/benchmarksummary.jpg "BenchMarking graph")

### Conclusion

Based on above performance number and requirements in-memory cache would be better solution for configuration
management at this time. We also considered memory foot print impact because of in-memory cache, based on
analysis provided ( [Link](http://blog.infinispan.org/2013/01/infinispan-memory-overhead.html) ) it will increase
memory foot print by 1MB maximum for configuration management within each class loader and that is very small
increase.




