import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
 
 
public class checksumTest {
     
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        boolean result = verifyChecksum("./README.txt", "5957743bb6dc27ec6abc6593baa00913");
        
        System.out.println("Does the file's checksum matches the expected one? " + result);
    }
     
    /**
     * Verifies file's SHA1 checksum
     * @param Filepath and name of a file that is to be verified
     * @param testChecksum the expected checksum
     * @return true if the expeceted SHA1 checksum matches the file's SHA1 checksum; false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean verifyChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
  
        byte[] data = new byte[1024];
        int read = 0; 
        while ((read = fis.read(data)) != -1) {
            sha1.update(data, 0, read);
        };
        byte[] hashBytes = sha1.digest();
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        String fileHash = sb.toString();
        System.out.println(fileHash);
        return fileHash.equals(testChecksum);


    }
 
 
}