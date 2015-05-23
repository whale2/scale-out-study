package tstapp

import grails.converters.JSON

class AppImageController {

    def index() { }

    def fetchTmpImage() {
        try {
            // There should be some access control - only app servers should be allowed
            AppImage tmpImage = AppImage.fetchTmpImage(params.path)

            render (text: [image: tmpImage.appImage, mime: tmpImage.appImageMime]
                    as JSON, contentType:'text/json')
        }
        catch (Exception e) {
            println "Exception: " + e.toString()
            render (view: "/mobileApp/notfound")
        }
    }

    def purgeTmpImage() {
        try {
            AppImage.purgeTmpImage(params.path)
            render (text: [success:true] as JSON, contentType:'text/json')
        }
        catch (Exception e) {
            render (text: [success:false, reason: e.toString()] as JSON, contentType:'text/json')
        }

    }

    def fetchAppImage() {

        def id = params.id

        if (id == null) {
            render (view: "notfound")
            return
        }

        AppImage img = AppImage.get(id)
        if (img == null) {
            render(view: "notfound")
            return
        }

        response.setHeader('Content-length', img.contentLength())
        response.contentType = img.appImageMime
        response.outputStream << img.toBytes()
        response.outputStream.flush()
    }
}
