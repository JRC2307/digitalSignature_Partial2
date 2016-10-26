import java.sql.Timestamp;
import java.util.Date;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
 
 
public class checksumTest {
     
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        boolean result = verifyChecksum("./File.txt", "bb07227cc1571342cb4c29a74e5c7aa3");
        
        System.out.println("Is the hash the same?" + result);
        java.util.Date date= new java.util.Date();
        //System.out.println(new Timestamp(date.getTime()).toString());

         Writer writer = new FileWriter("File.txt", true);

         writer.write(new Timestamp(date.getTime()).toString());
         writer.write("\n");
         writer.close();

    }
    
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