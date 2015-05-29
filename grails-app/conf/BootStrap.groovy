import tstapp.AppScreenshots

class BootStrap {

    def fillBuckets

    def init = { servletContext ->
        fillBuckets.fill()
    }

    def destroy = {
    }
}
