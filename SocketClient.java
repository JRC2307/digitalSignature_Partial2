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
import java.sql.Timestamp;
import java.math.*;

public class SocketClient {

    public static void main(String argv[]) throws Exception {

        SocketClient client = new SocketClient();
        client.run();
    }

    public void run() throws Exception {

        String hash; //10.25.88.28
        //Hay que cambiar esta direccion, si se quiere correr local usar "localhost"
        Socket sock = new Socket("10.25.88.28", 4444);
        PrintStream PS = new PrintStream(sock.getOutputStream());

        MessageDigest md = MessageDigest.getInstance("MD5");
        //Este path hay que cambiarlo para cada equipo diferente
        FileInputStream fis = new FileInputStream("C:\\Users\\Luis\\Desktop\\File.txt");

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        hash = sb.toString();
        //Envia el hash del cliente al servidor
        PS.println(hash);

        InputStreamReader IR = new InputStreamReader(sock.getInputStream());
        BufferedReader BR = new BufferedReader(IR);

        //Imprime lo que recive del servidor
        String msg = BR.readLine();
        System.out.println(msg);

        Writer writer1 = new FileWriter("C:\\Users\\Luis\\Desktop\\File.txt", true);

        writer1.write(msg);
        writer1.close();

    }

    public static Writer RSAPSS() throws Exception {
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
        return writer;

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
