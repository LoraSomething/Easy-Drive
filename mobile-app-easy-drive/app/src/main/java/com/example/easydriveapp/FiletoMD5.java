package com.example.easydriveapp;

import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FiletoMD5 {

    private static final String TAG = "MD5";

        public static String convertFiletoMD5 (FileInputStream fis){
            InputStream inputStream = null;
            try {
                inputStream = fis;
                byte[] buffer = new byte[1024];
                MessageDigest digest = MessageDigest.getInstance("MD5");
                int numRead = 0;
                while (numRead != -1) {
                    numRead = inputStream.read(buffer);
                    if (numRead > 0)
                        digest.update(buffer, 0, numRead);
                }
                byte[] md5Bytes = digest.digest();
                return convertHashToString(md5Bytes);
            } catch (Exception e) {
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        private static String convertHashToString ( byte[] md5Bytes){
            String returnVal = "";
            for (int i = 0; i < md5Bytes.length; i++) {
                returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
            }
            return returnVal;
        }
    }
