package tstapp

class TopApp {

    UUID appId
    String shortDescr
    String author
    Float score
    Long visits


    static TopAppScoreComparator getScoreComparator() {
        return TopAppScoreComparator
    }

    static TopAppScoreComparator getVisitsComparator() {
        return TopAppVisitsComparator
    }

    static class TopAppScoreComparator implements Comparator<TopApp> {

        int compare(TopApp a1, TopApp a2) {
            if (a1.score > a2.score) {
                return 1
            }
            else if(a1.score == a2.score) {
                if (a1.visits > a2.visits)
                    return 1
                else if (a1.visits == a2.visits)
                    return 0
                else
                    return -1
            }
            else {
                return -1
            }
        }
    }

    static class TopAppVisitsComparator implements Comparator<TopApp> {

        int compare(TopApp a1, TopApp a2) {
            if (a1.visits > a2.visits) {
                return 1
            }
            else if(a1.visits == a2.visits) {
                if (a1.score > a2.score)
                    return 1
                else if (a1.score == a2.score)
                    return 0
                else
                    return -1
            }
            else {
                return -1
            }
        }
    }
}
