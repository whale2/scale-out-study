<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>The TstApp</title>
</head>
<body>
    <div id="topDiv"><div id="topInnerDiv">The TstApp</div></div>
    <div id="sortSelector">
        <div class="sortDiv">
            <input type="checkbox" id="chbByRating" value="byRating" name="sortBy"
                <g:if test="${byVisits == 0}">checked="1"</g:if>
                onchange="renderByScore()"
            /><label for="chbByRating">По рейтингу</label>
        </div>
        <div class="sortDiv">
            <input type="checkbox" id="chbByVisits" value="byVisits" name="sortBy"
                <g:if test="${byVisits == 1}">checked="1"</g:if>
                onchange="renderByVisits()"
            /><label for="chbByVisits">По популярности</label>
        </div>
    </div>
    <div id="tagSearch">
        <g:form name="tagSearchForm" url="[controller:'mobileApp', action:'searchTags']" method="GET">
            <input type="text" name="tag" id="tagInput" size="40" placeholder="Введите тэг для поиска"/>
        </g:form>
    </div>
    <div id="mainAppsDiv">
        <g:each in="${apps}" var="app">
            <a href="/tstapp/mobileApp/show/${app.appId}">
                <div class="appDiv">
                    <img src="/tstapp/appImage/fetchAppImage/${app.appId}" width="160px" height="110px"/>
                    <p>${app.shortDescr}</p>
                    <p>Автор: ${app.author}</p>
                    <p>Рейтинг: ${app.score}</p>
                    <p>Просмотров: ${app.visits}</p>
                </div>
            </a>
        </g:each>
    </div>
    <script type="text/javascript">
        function renderByVisits() {
            $("#chbByRating").prop('checked',false);
            document.location="/tstapp/mobileApp/?byVisits=1";
        }

        function renderByScore() {
            $("chbByVisits").prop('checked',false);
            document.location="/tstapp/mobileApp/?byScore=1";
        }
    </script>
</body>
</html>