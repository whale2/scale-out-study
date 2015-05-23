package tstapp

class AppScreenshots {

    UUID appId
    List<String> mimeType
    List<String> screenShot

    ArrayList<byte[]> screenShotBytes

    static transients = [ "screenShotBytes" ]

    static mapping = {
        id name: "appId", generator: "assigned"
    }

    static constraints = {
    }

    byte[] toBytes(int n) {

        if (screenShotBytes == null) {
            screenShotBytes = new ArrayList<byte[]>()
            mimeType.each {
                screenShotBytes.add(null)
            }
        }

        if (screenShotBytes.get(n) != null)
            return screenShotBytes.get(n)

        String tmpImage = screenShot[n]
        if (tmpImage == null)
            return null

        screenShotBytes.set(n,tmpImage.decodeBase64())

        return screenShotBytes.get(n)
    }

    String contentLength(int n) {
        toBytes(n)
        return screenShotBytes[n].length.toString()
    }

    int size() {
        return mimeType.size()
    }
}
