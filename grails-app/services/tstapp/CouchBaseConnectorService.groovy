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
            Cluster cluster = CouchbaseCluster.create("couchbase0", "couchbase1")

            visitsBucket = cluster.openBucket("visits",30, TimeUnit.SECONDS)
            println "visits bucket ok"

            // Docs says it is resourse-consuming, but I can't work around this bug in other ways
            // Hmm, it works with 2-node cluster. Good for me
            //Cluster cluster2 = CouchbaseCluster.create("couchbase0")
            scoresBucket = cluster.openBucket("score", 30, TimeUnit.SECONDS)
            println "score bucket ok"
            votesBucket = cluster.openBucket("votes", 30, TimeUnit.SECONDS)
            println "votes bucket ok"
            appsBucket = cluster.openBucket("apps", 30, TimeUnit.SECONDS)
            println "votes bucket ok"

        } catch (Exception e) {
            print "Failed opening bucket: "
            e.printStackTrace()
            throw new RuntimeException(e)
        }

        println "opened bucket " + visitsBucket.bucketManager().info().name() + ", bucket placed on " +
                visitsBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + scoresBucket.bucketManager().info().name() + ", cluster running on " +
                scoresBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + votesBucket.bucketManager().info().name() + ", cluster running on " +
                votesBucket.bucketManager().info().nodeCount() + " nodes"
        println "opened bucket " + appsBucket.bucketManager().info().name() + ", cluster running on " +
                appsBucket.bucketManager().info().nodeCount() + " nodes"
    }
}
