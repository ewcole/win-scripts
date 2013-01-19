import java.security.MessageDigest
def MB = 1048576;

args.each {
  fileName ->
    File f = new File(fileName)
    if (! f.exists() ) {
      println "No such file: $fileName"
    } else {
      def md = MessageDigest.getInstance("SHA1")
      f.eachByte(MB) {
        byte[] buf, int bytesRead ->
          md.update(buf, 0, bytesRead)
      }
      def sha1Hex = new BigInteger(1, md.digest()).toString(16).padLeft(40, '0')
      println "$sha1Hex $fileName"
    }
}