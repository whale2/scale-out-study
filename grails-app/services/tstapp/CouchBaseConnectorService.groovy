package tstapp

import com.couchbase.client.java.Cluster
import com.couchbase.client.java.CouchbaseBucket
import com.couchbase.client.java.CouchbaseCluster

class CouchBaseConnectorService {

    CouchbaseBucket counterBucket
    CouchbaseBucket scoreBucket

    CouchBaseConnectorService() {

        Cluster cluster = CouchbaseCluster.create("couchbase0")
        try {
            counterBucket = cluster.openBucket("counter")
            scoreBucket = cluster.openBucket("score")
        } catch (Exception e) {
            println "Failed opening bucket: " + e.stackTrace()
        }

        println "opened bucket " + counterBucket.bucketManager().info().name() + ", containing " +
                counterBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + scoreBucket.bucketManager().info().name() + ", containing " +
                scoreBucket.bucketManager().info().nodeCount() + " nodes"
    }
}
