package tstapp

import groovy.json.StringEscapeUtils

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

    static def cassandraTemplate

    //static mapWith = "cassandra"

    static def listByTag(String tag) {

        String cql = "SELECT * FROM tstapp.mobileapp WHERE apptags CONTAINS '" +
                StringEscapeUtils.escapeJava(tag) + "'";

        List apps = cassandraTemplate.select(cql, MobileApp.class)
        return apps
    }
}
