<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>${app.appName}</title>
    </head>
    <body>
        Салют<br/>
        <div id="imgDiv"><img src="/tstapp/appImage/fetchAppImage/${app.id}"/></div>
        <div id="topDiv">${app.appName}</div>
        <div id="centerDiv">Рейтинг: ${app.appScore}</div>
        <div id="descrDiv">${app.shortDescr}</div>
        <div id="fullDescrDiv">${app.fullDescr}</div>
        <div id="authorDiv">${app.appAuthor}</div
        <div id="lowerDiv">Количество просмотров: ${app.visitCounter}</div>
        <div id="screenshotsDiv">
            <g:each var="i" in="${ (0..<app.nScreenShots) }">
                <img src="/tstapp/appScreenshots/fetchScreenshotImage/${app.id}/${i}"/>
            </g:each>
        </div>
    </body>
</html>
