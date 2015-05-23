<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Добавление приложения</title>
</head>
<body>
    <g:form name="appForm" url="[controller:'mobileApp', action:'newapp']">
        <label>Название</label>
        <g:textField name="appName" placeholder="Название"/><br/>
        <label>Краткое описание</label>
        <g:textField name="shortDescr" placeholder="Краткое описание"/><br/>
        <label>Тэги</label>
        <g:textField name="appTags" placeholder="через запятую"/><br/>
        <label>Автор</label>
        <g:textField name="appAuthor" placeholder="Автор"/><br/>
        <label>Полное описание</label>
        <g:textArea name="fullDescr"/><br/>
        <label>Иконка</label>
        <g:hiddenField name="imgPath" id="imgPath"/>
        <g:hiddenField name="imgHost" id="imgHost"/>
        <uploader:uploader id="appImgUploader" url="${[controller:'imgUpload', action:'upload']}"
            multiple="false">
            <uploader:onComplete>
                $('#imgPath').val(responseJSON.path);
                $('#imgHost').val(responseJSON.host);
            </uploader:onComplete>
        </uploader:uploader><br/>
        <label>Скриншоты</label>
        <g:hiddenField name="scrPath" id="scrPath"/>
        <g:hiddenField name="scrHost" id="scrHost"/>
        <uploader:uploader id="appScrUploader" url="${[controller:'imgUpload', action:'upload']}"
            multiple="true">
            <uploader:onComplete>
                $('#scrPath').val($('#scrPath').val() + responseJSON.path + ',');
                $('#scrHost').val($('#scrHost').val() + responseJSON.host + ',');
            </uploader:onComplete>
        </uploader:uploader><br/>
        <g:actionSubmit value="Добавить" action="newapp" />
    </g:form>
</body>
</html>
