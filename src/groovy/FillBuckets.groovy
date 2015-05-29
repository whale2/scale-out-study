import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.java.document.JsonLongDocument
import com.couchbase.client.java.document.json.JsonObject
import tstapp.MobileApp

class FillBuckets {

    def couchBucket

    def fill() {
        // Now let's put some apps from Cassandra into Couchbase. In reality this should happen in
        // some off-line process. We hardly have correct ORDER BY in Cassandra, so just load some 1000 apps :)

        MobileApp.withStatelessSession {

            def apps = MobileApp.list(max: 1000)
            println "Syncing " + apps.size() + " apps to Couchbase"
            for (a in apps) {

                def app = [ appId: a.id.toString(), shortDescr: a.shortDescr, author: a.appAuthor ]

                JsonObject couchApp = JsonObject.from(app)
                couchBucket.getAppsBucket().upsert(JsonDocument.create(a.id.toString(), couchApp))

                JsonLongDocument couchScores = JsonLongDocument.create(a.id.toString(), a.appScores)
                couchBucket.getScoresBucket().upsert(couchScores)

                JsonLongDocument couchVotes = JsonLongDocument.create(a.id.toString(), a.appVotes)
                couchBucket.getVotesBucket().upsert(couchVotes)
            }
        }

    }

}
