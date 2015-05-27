<!doctype html>
<html xmlns:r="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="layout" content="main"/>
        <title>${app.appName}</title>
    </head>
    <body>
        Салют<br/>
        <div id="imgDiv"><img src="/tstapp/appImage/fetchAppImage/${app.id}"/></div>
        <div id="topDiv">${app.appName}</div>
        <div id="centerDiv">Рейтинг: <span id="appScore">${app.displayScore}</span></div>
        <div id="userScoreDiv">Ваша оценка:
            <div id="allStarsDiv">
              <g:each var="i" in="${(0..4)}">
                  <div class="starDiv">
                      <div class="innerStarDiv silverStar" id="scoreStar_${i}" sequence="${i}"></div>
                  </div>
              </g:each>
            </div>
        </div>
        <div id="descrDiv">${app.shortDescr}</div>
        <div id="fullDescrDiv">${app.fullDescr}</div>
        <div id="authorDiv">${app.appAuthor}</div>
        <div id="lowerDiv">Количество просмотров: ${app.visitCounter}</div>
        <div id="screenshotsDiv">
            <g:each var="i" in="${ (0..<app.nScreenShots) }">
                <img src="/tstapp/appScreenshots/fetchScreenshotImage/${app.id}/${i}"/>
            </g:each>
        </div>
        <script type="text/javascript">
            var appId = "${app.id}";
            var appScore = ${app.displayScore};
            var userScore = ${app.userScore};
        </script>
        <script src="/tstapp/static/js/showMobApp.js"></script>
    </body>
</html>
