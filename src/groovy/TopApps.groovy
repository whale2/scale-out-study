package tstapp

import com.couchbase.client.java.document.json.JsonObject
import com.couchbase.client.java.view.ViewResult
import tstapp.TopApp
import com.couchbase.client.java.view.ViewQuery

class TopApps {

    static def couchBucket

    static def appListByScore() {

        /*
        /  get scores (and all app data from couchScoreBucket using a view)
        /  get counters
        /
        /
        /  build app list
        */

        ViewQuery vqs = ViewQuery.from("someDesign","scoreView").descending(true).limit(1000)
        ViewResult vrScore = couchBucket.getScoresBucket.query(vqs)

        ViewQuery vqv = ViewQuery.from("someDesign","votesView").descending(true).limit(1000)
        ViewResult vrVotes = couchBucket.getVotesBucket.query(vqv)

        ViewQuery vqc = ViewQuery.from("someDesign","visitsView").descending(true).limit(1000)
        ViewResult vrVisits = couchBucket.getVisitsBucket.query(vqc)

        def visits = new Long[1000]
        vrVisits.rows().each { row ->

            visits[row.key()] = row.value().getLong()
        }

        def scores = new Long[1000]
        vrScore.rows().each { row ->

            scores[row.key()] = row.value().getLong()
        }

        def realScores = new Float[1000]
        vrVotes.rows().each { row ->

            def votes = row.value().getLong()
            realScores[row.key()] = votes == 0 ? 0 : (scores[row.key()] / votes)
        }

        realScores = realScores.sort { a, b -> a.value <=> b.value }

        def topApps = new TopApp[100]
        def i = 0
        realScores.each { id, score ->

            if (i > 100) return
            JsonObject jApp = couchBucket.getAppsBucket.get(id)
            topApps[i++] = new TopApp( appId: id, shortDescr: jApp.getString("shortDescr"),
                author: jApp.getString("author"), score: score, visits: visits[id])
        }

        return topApps
    }

    static def appListByVisits() {

        /*
        /  get scores (and all app data from couchScoreBucket using a view)
        /  get counters
        /
        /  build app list
        */

        ViewQuery vqs = ViewQuery.from("someDesign","scoreView").descending(true).limit(1000)
        ViewResult vrScore = couchBucket.getScoresBucket.query(vqs)

        ViewQuery vqv = ViewQuery.from("someDesign","votesView").descending(true).limit(1000)
        ViewResult vrVotes = couchBucket.getVotesBucket.query(vqv)

        ViewQuery vqc = ViewQuery.from("someDesign","visitsView").descending(true).limit(1000)
        ViewResult vrVisits = couchBucket.getVisitsBucket.query(vqc)

        def visits = new Long[1000]
        vrVisits.rows().each { row ->

            visits[row.key()] = row.value().getLong()
        }

        def scores = new Long[1000]
        vrScore.rows().each { row ->

            scores[row.key()] = row.value().getLong()
        }

        def realScores = new Float[1000]
        vrVotes.rows().each { row ->

            dev votes = row.value().getLong()
            realScores[row.key()] = votes == 0 ? 0 : (scores[row.key()] / votes)
        }

        visits = visits.sort { a, b -> a.value <=> b.value }

        def topApps = new TopApp[100]
        def i = 0
        visits.each { id, nVisits ->

            if (i > 100) return
            def jApp = couchBucket.getAppsBucket.get(id)
            topApps[i++] = new TopApp( appId: id, shortDescr: jApp.getString("shortDescr"),
                    author: jApp.getString("author"), score: realScores[id], visits: nVisits)
        }

        return topApps
    }

}
