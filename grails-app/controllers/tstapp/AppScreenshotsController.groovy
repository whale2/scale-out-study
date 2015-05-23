package tstapp

class AppScreenshotsController {

    def index() { }

    def fetchScreenshotImage() {

        def id = params.id
        int n = Integer.parseInt(params.subid)

        if (id == null) {
            render (view: "notfound")
            return
        }
        def img = AppScreenshots.get(id)
        if (img == null) {
            render(view: "notfound")
            return
        }

        response.setHeader('Content-length', img.contentLength(n))
        response.contentType = img.mimeType[n]
        response.outputStream << img.toBytes(n)
        response.outputStream.flush()
    }
}
