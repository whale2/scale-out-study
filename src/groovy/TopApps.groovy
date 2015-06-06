
import com.couchbase.client.java.view.ViewResult
import tstapp.TopApp
import com.couchbase.client.java.view.ViewQuery

class TopApps {

    def couchBucket

    def appListByScore() {

        /*
        /  get scores (and all app data from couchScoreBucket using a view)
        /  get counters
        /
        /
        /  build app list
        */

        ViewQuery vqs = ViewQuery.from("scores","scoresView").limit(1000)
        ViewResult vrScore = couchBucket.getScoresBucket().query(vqs)

        ViewQuery vqv = ViewQuery.from("votes","votesView").limit(1000)
        ViewResult vrVotes = couchBucket.getVotesBucket().query(vqv)

        ViewQuery vqc = ViewQuery.from("visits","visitsView").limit(1000)
        ViewResult vrVisits = couchBucket.getVisitsBucket().query(vqc)

        def visits = [:]
        vrVisits.rows().each { row ->
            visits[row.key()] = row.value().toLong()
        }

        def scores = [:]
        vrScore.rows().each { row ->

            scores[row.key()] = row.value().toLong()
        }

        def realScores = [:]
        vrVotes.rows().each { row ->

            def votes = row.value().toLong()
            realScores[row.key()] = votes == 0 ? 0 : (scores[row.key()] / votes)
        }

        realScores = realScores.sort { a, b -> b.value <=> a.value }

        def topApps = []
        def i = 0
        realScores.each { id, score ->

            if (i > 100) return
            def jApp = couchBucket.getAppsBucket().get(id).content()
            topApps[i++] = new TopApp( appId: UUID.fromString(id), shortDescr: jApp.getString("shortDescr"),
                author: jApp.getString("author"), score: score, visits: visits[id])
        }

        return topApps
    }

    def appListByVisits() {

        /*
        /  get scores (and all app data from couchScoreBucket using a view)
        /  get counters
        /
        /  build app list
        */

        ViewQuery vqs = ViewQuery.from("scores","scoresView").limit(1000)
        ViewResult vrScores = couchBucket.getScoresBucket().query(vqs)

        ViewQuery vqv = ViewQuery.from("votes","votesView").limit(1000)
        ViewResult vrVotes = couchBucket.getVotesBucket().query(vqv)

        ViewQuery vqc = ViewQuery.from("visits","visitsView").limit(1000)
        ViewResult vrVisits = couchBucket.getVisitsBucket().query(vqc)

        def visits = [:]
        vrVisits.rows().each { row ->

            visits[row.key()] = row.value().toLong()
        }

        def scores = [:]
        vrScores.rows().each { row ->

            scores[row.key()] = row.value().toLong()
        }

        def realScores = [:]
        vrVotes.rows().each { row ->

            def votes = row.value().toLong()
            realScores[row.key()] = (votes == 0 || votes == null) ? 0 : (scores[row.key()] / votes)
        }

        visits = visits.sort { a, b -> b.value <=> a.value }

        def topApps = []
        def i = 0
        visits.each { id, nVisits ->

            if (i > 100) return
            def jApp = couchBucket.getAppsBucket().get(id).content()
            topApps[i++] = new TopApp( appId: UUID.fromString(id), shortDescr: jApp.getString("shortDescr"),
                    author: jApp.getString("author"), score: realScores[id], visits: nVisits)
        }

        return topApps
    }

}
