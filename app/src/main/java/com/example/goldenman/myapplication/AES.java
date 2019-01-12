package com.example.goldenman.myapplication;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shruthi
 */
public class AES {

    static KeyGenerator kgen;
    static SecretKey skey;
    static SecretKeySpec skeySpec;
    static Cipher cipher;
    static byte[] raw;
    
    public  void initCipher() throws Exception{
        kgen = KeyGenerator.getInstance("AES");
       kgen.init(128); // 192 and 256 bits may not be available
       // Generate the secret key specs.
       skey = kgen.generateKey();
     
       // Instantiate the cipher
       cipher = Cipher.getInstance("AES");

     
       cipher.init(Cipher.ENCRYPT_MODE, skey);
        
        
        
    }
    
    byte[] encrypt(byte[] msg) throws Exception{
        byte[] result= cipher.doFinal(msg);
        return result;
    }
    
}
