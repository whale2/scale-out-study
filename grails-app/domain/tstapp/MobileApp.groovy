package tstapp

class MobileApp {

    String appName
    Long appVotes
    Long appScores
    Float displayScore
    String shortDescr
    String fullDescr
    String appAuthor
    int nScreenShots
    Long visitCounter

    Set<String> appTags

    static transients = [ "visitCounter", "displayScore" ]

    static constraints = {
    }

    //static mapWith = "cassandra"
}
