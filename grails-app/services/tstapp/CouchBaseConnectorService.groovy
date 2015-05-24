package tstapp

import com.couchbase.client.java.Cluster
import com.couchbase.client.java.CouchbaseBucket
import com.couchbase.client.java.CouchbaseCluster

import java.util.concurrent.TimeUnit

class CouchBaseConnectorService {

    CouchbaseBucket counterBucket
    CouchbaseBucket scoreBucket

    CouchBaseConnectorService() {
        println "Connecting to couchbase..."
        try {
            Cluster cluster = CouchbaseCluster.create("couchbase0", "couchbase1")

            counterBucket = cluster.openBucket("counter",30, TimeUnit.SECONDS)
            println "counter bucket ok"

            // Docs says it is resourse-consuming, but I can't work around this bug in other ways
            // Hmm, it works with 2-node cluster. Good for me
            //Cluster cluster2 = CouchbaseCluster.create("couchbase0")
            scoreBucket = cluster.openBucket("score", 30, TimeUnit.SECONDS)
            println "score bucket ok"
        } catch (Exception e) {
            print "Failed opening bucket: "
            e.printStackTrace()
            throw new RuntimeException(e)
        }

        println "opened bucket " + counterBucket.bucketManager().info().name() + ", cluster running on " +
                counterBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + scoreBucket.bucketManager().info().name() + ", cluster running on " +
                scoreBucket.bucketManager().info().nodeCount() + " nodes"
    }
}
