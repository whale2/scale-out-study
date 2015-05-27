package tstapp

class MobileApp {

    String appName
    Long appVotes
    Long appScores
    Float displayScore
    Integer userScore
    String shortDescr
    String fullDescr
    String appAuthor
    int nScreenShots
    Long visitCounter

    Set<String> appTags

    static transients = [ "visitCounter", "displayScore", "userScore" ]

    static constraints = {
    }

    //static mapWith = "cassandra"
}
