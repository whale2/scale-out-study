package tstapp

import com.couchbase.client.java.document.JsonLongDocument
import static groovyx.net.http.ContentType.JSON

class MobileAppController {

    def couchBucket

    def index() { }

    def addapp() {
        render(view: "addapp")
    }

    def newapp() {

        def response
        try {
            withHttp(uri: "http://" + params.imgHost + ":8080") {
                response = get(path : '/tstapp/appImage/fetchTmpImage',
                    query : [path: params.imgPath],
                    contentType: JSON)
            }

        } catch (Exception e) {
            println "Failed getting image from " + params.imgHost + " at path " + params.imgPath + ": " + e
            render(view: "error")
            return
        }

        def hosts = params.scrHost.split(',')

        def newApp = new MobileApp(appName: params.appName, shortDescr: params.shortDescr,
                fullDescr: params.fullDescr, appAuthor: params.appAuthor,
                appTags: params.appTags.split(','), appScore: 0,
                nScreenShots: hosts.length, visitCounter: 0)
        newApp.insert()

        def newAppImg = new AppImage(appId: newApp.id, appImage: response.image, appImageMime: response.mime)
        newAppImg.insert()

        // remove tmp image file
        withHttp(uri: "http://" + params.imgHost + ":8080") {
            get(path: '/tstapp/appImage/purgeTmpImage',
                    query: [path: params.imgPath],
                    contentType: JSON)
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
                        contentType: JSON)
                }

                newAppScreenshots.appendToMimeType(response.mime)
                newAppScreenshots.appendToScreenShot(response.image)
                println("i="+i+",screenshots:" + newAppScreenshots.size())
                // remove tmp image file
                withHttp(uri: "http://" + hosts[i] + ":8080") {
                    get(path: '/tstapp/appImage/purgeTmpImage',
                        query: [path: path],
                        contentType: JSON)
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
        def app = MobileApp.get(id)
        if (app == null) {
            render(view: "notfound")
            return
        }

        // Get and increment counter from couchBase
        JsonLongDocument cnt = couchBucket.getCounterBucket().counter(id, 1, 1)
        Long visitCounter = cnt.content()

        app.visitCounter = visitCounter

        [app:app]
    }

}
