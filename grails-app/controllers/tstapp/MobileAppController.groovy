package tstapp

import com.couchbase.client.java.document.JsonLongDocument
import static groovyx.net.http.ContentType.JSON
import grails.converters.JSON

class MobileAppController {

    def couchBucket
    def cookieService
    def TopApps

    def index() {

        def order = params.byVisits

        def apps
        if (order)
            apps = TopApps.appListByVisits()
        else
            apps = TopApps.appListByScore()

        [apps:apps, byVisits: (order == null ? 0:1)]
    }

    def addapp() {
        render(view: "addapp")
    }

    def newapp() {

        def response
        try {
            withHttp(uri: "http://" + params.imgHost + ":8080") {
                response = get(path : '/tstapp/appImage/fetchTmpImage',
                    query : [path: params.imgPath],
                    contentType: contentType.JSON)
            }

        } catch (Exception e) {
            println "Failed getting image from " + params.imgHost + " at path " + params.imgPath + ": " + e
            render(view: "error")
            return
        }

        def hosts = params.scrHost.split(',')

        def newApp = new MobileApp(appName: params.appName, shortDescr: params.shortDescr,
                fullDescr: params.fullDescr, appAuthor: params.appAuthor,
                appTags: params.appTags.split(','), appScores: 0, appVotes: 0,
                nScreenShots: hosts.length, visitCounter: 0)
        newApp.insert()
        println response.mime
        def newAppImg = new AppImage(appId: newApp.id, appImage: response.image, appImageMime: response.mime)
        newAppImg.insert()

        // remove tmp image file
        withHttp(uri: "http://" + params.imgHost + ":8080") {
            get(path: '/tstapp/appImage/purgeTmpImage',
                    query: [path: params.imgPath],
                    contentType: contentType.JSON)
        }

        // Fetch screenshots
        def newAppScreenshots = new AppScreenshots(appId: newApp.id, mimeType: [], screenShot: [])
        def i = 0
        try {
            params.scrPath.split(',').each { path ->

                if (path == "") return
                withHttp(uri: "http://" + hosts[i] + ":8080") {
                    response = get(path : '/tstapp/appImage/fetchTmpImage',
                        query : [path: path],
                        contentType: contentType.JSON)
                }

                newAppScreenshots.appendToMimeType(response.mime)
                newAppScreenshots.appendToScreenShot(response.image)
                println("i="+i+",screenshots:" + newAppScreenshots.size())
                // remove tmp image file
                withHttp(uri: "http://" + hosts[i] + ":8080") {
                    get(path: '/tstapp/appImage/purgeTmpImage',
                        query: [path: path],
                        contentType: contentType.JSON)
                }

                i ++
            }
        } catch (Exception e) {

            println "Failed getting screenshot:" + e
            render(view: "error")
            return
        }

        newAppScreenshots.insert()

        render(view: "show", model:[app:newApp])
    }

    def show() {

        def id = params.id

        if (id == null) {
            render (view: "notfound")
            return
        }

        // Get application details from Cassandra
        MobileApp app = MobileApp.get(id)
        if (app == null) {
            render(view: "notfound")
            return
        }

        app.displayScore = app.appVotes == 0 ? 0 : (app.appScores / app.appVotes)
        // Get and increment counter from couchBase
        JsonLongDocument cnt = couchBucket.getVisitsBucket().counter(id, 1, 1)
        Long visitCounter = cnt.content()

        app.visitCounter = visitCounter

        // Get user score from Cassandra
        User currentUser = User.findUserByCookie(cookieService)

        app.userScore = currentUser.appScores[id]
        if (app.userScore == null)
            app.userScore = 0

        [app:app]
    }

    def addScore() {

        def id = params.id
        def newScore = params.int('rating')

        // Increment counters for number of ratings and add a value to rating itself
        // Both are inplemented using couchbase counters for better consistency


        // Check if this app is present in Couchbase
        // However this check is postponed while development is going on

        boolean devel = true
        def v = null
        if (!devel) {
            v = couchBucket.getVotesBucket().get(id)
        }

        if (devel || v) {
            couchBucket.getScoresBucket().counter(id, newScore, newScore)
            couchBucket.getVotesBucket().counter(id, 1, 1)
        }

        // Now update app info in cassandra
        MobileApp app = MobileApp.get(id)
        if (app == null) {
            return render(text: [ appScore: 0 ] as JSON, contentType:'text/json')
        }
        else {
            app.appScores += newScore
            app.appVotes ++
            app.update([flush:true])
        }

        // Now save users vote
        User currentUser = User.findUserByCookie(cookieService)

        currentUser.appendToAppScores([ (id) : newScore ])
        currentUser.update()

        def newAppScore = app.appScores / app.appVotes

        return render(text: [ appScore: newAppScore ] as JSON, contentType:'text/json')
    }

    def searchTags() {

        def tag = params.tag
        def apps = MobileApp.listByTag(tag)

        if (apps == null || apps.size() == 0) {
            render(view: "notfound")
            return
        }
        render(view: "searchtags", model:[apps:apps, tag:tag])
    }
}
