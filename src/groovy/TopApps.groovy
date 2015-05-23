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

        ViewQuery vqs = ViewQuery.from("someDesign","scoreView").descending(true).limit(100)
        ViewResult vrScore = couchBucket.getScoreBucket.query(vqs)

        ViewQuery vqc = ViewQuery.from("someDesign","counterView").descending(true).limit(1000)
        ViewResult vrCounter = couchBucket.getCounterBucket.query(vqc)

        def visits = new UUID[1000]
        vrCounter.rows().each { row ->

            visits[row.key()] = row.value().getLong()
        }


        TreeSet<TopApp> topApps = new TreeSet<>(TopApp.getScoreComparator())

        vrScore.rows().each { row ->

            UUID k = row.key()
            JsonObject o = row.value()
            TopApp app = new TopApp( appId: k, shortDescr: o.getString("shortDescr"),
                    author: o.getString("author"), score: o.getDouble("score"), visits: visits[k])

            topApps.add(app)
        }

        return topApps
    }

    static def appListByVisits() {

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
        ViewResult vrScore = couchBucket.getScoreBucket.query(vqs)

        ViewQuery vqc = ViewQuery.from("someDesign","counterView").descending(true).limit(100)
        ViewResult vrCounter = couchBucket.getCounterBucket.query(vqc)

        def apps = new UUID[1000]
        vrCounter.rows().each { row ->

            apps[row.key()] = row.value()
        }

        TreeSet<TopApp> topApps = new TreeSet<>(TopApp.getVisitsComparator())

        vrScore.rows().each { row ->

            UUID k = row.key()
            Long visits = row.value().getLong()
            JsonObject o = apps[k]
            TopApp app = new TopApp( appId: k, shortDescr: o.getString("shortDescr"),
                    author: o.getString("author"), score: o.getDouble("score"), visits: visits)

            topApps.add(app)
        }

        return topApps
    }

}
