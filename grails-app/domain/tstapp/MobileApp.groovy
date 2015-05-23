package tstapp

class MobileApp {

    String appName
    Float appScore
    String shortDescr
    String fullDescr
    String appAuthor
    int nScreenShots
    Long visitCounter

    Set<String> appTags

    static transients = [ "visitCounter" ]

    static constraints = {
    }

    //static mapWith = "cassandra"
}
