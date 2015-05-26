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
        /  couchScoreBucket view should return - [ score, [ app id, app name, app icon, app author ] ]
        /  couchCounterBucket view should return [ app id, visitCounter ]
        /
        /  We will build list using TreeSet where we will use counters as secondary sort
        /  criteria. I.e. in two apps with score=3.5, the one with greater visitCounter is considered greater
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

            realScores[row.key()] = scores[row.key()] / row.value().getLong()
        }

        realScores = realScores.sort { a, b -> a.value <=> b.value }

        def topApps = new TopApp[100]
        def i = 0
        realScores.each { id, score ->

            if (i > 100) return
            def jApp = couchBucket.getAppsBucket.get(id)
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

            realScores[row.key()] = scores[row.key()] / row.value().getLong()
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
