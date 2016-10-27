/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bdn;

import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;
import java.math.*;
import java.sql.Timestamp;

/**
 *
 * @author Luis
 */
public class SocketServer {

    public static void main(String argv[]) throws Exception {

        SocketServer server = new SocketServer();
        server.run();
    }

    public void run() throws Exception {

        ServerSocket srvsock = new ServerSocket(4444);
        Socket sock = srvsock.accept();
        InputStreamReader IR = new InputStreamReader(sock.getInputStream());
        BufferedReader BR = new BufferedReader(IR);

        String msg = BR.readLine();
        System.out.println(msg);

        if (msg != null) {
            //Este path hay que cambiarlo para cada equipo diferente
            boolean result = verifyChecksum("C:\\Users\\Luis\\Desktop\\File.txt", msg);

            System.out.println("Comprobando hashes... \n" + result);
            java.util.Date date = new java.util.Date();
            System.out.println(new Timestamp(date.getTime()).toString());

            Writer writer1 = new FileWriter("C:\\Users\\Luis\\Desktop\\File.txt", true);

            writer1.write(new Timestamp(date.getTime()).toString() + "\n");
            writer1.close();
            //RSAPSS();
            //writer.write("Simon\n");

            PrintStream PS = new PrintStream(sock.getOutputStream());
            PS.print("Hash recibido: " + msg + "Timestamp: ");
            PS.print(new Timestamp(date.getTime()).toString() + "Signature: " + RSAPSS());

        }
    }

    public static boolean verifyChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException {
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

    public static String RSAPSS() throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String plaintext = "0000000000000000000000000000000000000000";

        // Compute signature
        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(privateKey);
        instance.update((plaintext).getBytes());
        byte[] signature = instance.sign();

        Writer writer = new FileWriter("C:\\Users\\Luis\\Desktop\\File.txt", true);

        writer.write("Signature: " + bytes2String(signature));
        writer.close();
        return bytes2String(signature);

    }

    private static String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }

}
