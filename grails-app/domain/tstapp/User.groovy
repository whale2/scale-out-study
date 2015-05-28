package tstapp

class User {

    Map<String,Integer> appScores

    static constraints = {
    }

    static User findUserByCookie(cookieService) {

        def userID = cookieService.getCookie('tstapp_user')

        User currentUser = null
        if (userID != null)
            currentUser = User.get(userID)


        if (currentUser == null) {
            currentUser = new User(appScores: [ "empty" : 0 ])
            currentUser.insert()

            cookieService.setCookie('tstapp_user', currentUser.id.toString(), 600)
        }

        if (currentUser.appScores == null)
            currentUser.appScores = [:]

        return currentUser
    }
}
