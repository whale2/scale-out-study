package tstapp

import grails.converters.JSON
import uk.co.desirableobjects.ajaxuploader.*
import uk.co.desirableobjects.ajaxuploader.exception.FileUploadException

class ImgUploadController extends AjaxUploadController {

    def upload =  {
        try {

            File uploaded = createTemporaryFile()
            InputStream inputStream = selectInputStream(request)

            ajaxUploaderService.upload(inputStream, uploaded)

            session.uploadedFile = uploaded

            return render(text:
                    [success:true, host:InetAddress.localHost.canonicalHostName, path:uploaded.getName()]
                    as JSON, contentType:'text/json')

        } catch (FileUploadException e) {

            log.error("Failed to upload file.", e)
            return render(text: [success:false] as JSON, contentType:'text/json')

        }
    }
}
