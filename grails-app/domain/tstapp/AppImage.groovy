package tstapp

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class AppImage {

    UUID appId
    String appImage
    String appImageMime
    byte[] appImageBytes

    static constraints = {
    }

    static transients = [ "appImageBytes" ]

    static mapping = {
        id name: "appId", generator: "assigned"
    }

    byte[] toBytes() {
        if (appImageBytes == null)
            appImageBytes = appImage.decodeBase64()

        return appImageBytes
    }

    String contentLength() {
        toBytes()
        return appImageBytes.length.toString()
    }

    static AppImage fetchTmpImage(String strPath) {

        Path path = Paths.get(strPath).normalize()
        if (path.getParent() != null)
            throw new RuntimeException("Bad path") // Only filename is allowed - no . or .. or /
        path = Paths.get("/tmp", path.toString())
        def contentType = Files.probeContentType(path)
        FileInputStream fs = new FileInputStream(path.toFile())

        AppImage tmpImg = new tstapp.AppImage(appImageMime: contentType,
                appImage: fs.getBytes().encodeBase64().toString())

        return tmpImg
    }

    static void purgeTmpImage(String strPath) {
        Path path = Paths.get(strPath).normalize()
        if (path.getParent() != null)
            throw new RuntimeException("Bad path") // Only filename is allowed - no . or .. or /

        Files.delete(Paths.get("/tmp", path.toString()))
    }
}
