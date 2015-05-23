package tstapp

class TopAppsController {

    def couchBucket

    def index() {
        redirect (action: byScore, controller: TopApps)
    }

    def byScore() {



    }
}
