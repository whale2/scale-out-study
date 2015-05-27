package tstapp

import com.couchbase.client.java.Cluster
import com.couchbase.client.java.CouchbaseBucket
import com.couchbase.client.java.CouchbaseCluster

import java.util.concurrent.TimeUnit

class CouchBaseConnectorService {

    /* We'll have many buckets -
        for keeping counter of app visits
        for keeping counter of app votes
        for keeping sum rating

    */

    CouchbaseBucket visitsBucket
    CouchbaseBucket votesBucket
    CouchbaseBucket scoresBucket
    CouchbaseBucket appsBucket

    CouchBaseConnectorService() {
        println "Connecting to couchbase..."
        try {
            Cluster cluster = CouchbaseCluster.create("couchbase1.tst", "couchbase0.tst")

            scoresBucket = cluster.openBucket("scores", 30, TimeUnit.SECONDS)
            println "score bucket ok"

            Cluster cluster2 = CouchbaseCluster.create("couchbase0.tst", "couchbase1.tst")
            visitsBucket = cluster2.openBucket("visits",30, TimeUnit.SECONDS)
            println "visits bucket ok"

            // Docs says it is resourse-consuming, but I can't work around this bug in other ways
            // Hmm, it works with 2-node cluster. Good for me
            // Not anymore :(


            Cluster cluster3 = CouchbaseCluster.create("couchbase1.tst", "couchbase0.tst")
            votesBucket = cluster3.openBucket("votes", 30, TimeUnit.SECONDS)
            println "votes bucket ok"
            Cluster cluster4 = CouchbaseCluster.create("couchbase0.tst", "couchbase1.tst")
            appsBucket = cluster4.openBucket("apps", 30, TimeUnit.SECONDS)
            println "votes bucket ok"

        } catch (Exception e) {
            print "Failed opening bucket: "
            e.printStackTrace()
            throw new RuntimeException(e)
        }

        println "opened bucket " + visitsBucket.bucketManager().info().name() + ", bucket placed on " +
                visitsBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + scoresBucket.bucketManager().info().name() + ", bucket placed on " +
                scoresBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + votesBucket.bucketManager().info().name() + ", bucket placed on " +
                votesBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + appsBucket.bucketManager().info().name() + ", bucket placed on " +
                appsBucket.bucketManager().info().nodeCount() + " nodes"
    }
}
