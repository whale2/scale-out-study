<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Поиск по тэгам</title>
</head>
<body>
    <div id="searchHeader">
        Результаты поиска по тэгу <b>${tag}</b>
    </div>
    <div id="searchResults">
        <div id="mainAppsDiv">
            <g:each in="${apps}" var="app">
                <a href="/tstapp/mobileApp/show/${app.id}">
                    <div class="appDiv">
                        <img src="/tstapp/appImage/fetchAppImage/${app.id}" width="160px" height="110px"/>
                        <p>${app.shortDescr}</p>
                        <p>Автор: ${app.appAuthor}</p>
                    </div>
                </a>
            </g:each>
        </div>
    </div>
</body>
</html>