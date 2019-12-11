package com.example.easydriveapp;

import android.util.Log;

import com.google.android.gms.common.util.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FiletoMD5 {

    private static final String TAG = "MD5";

    public static String calcMD5hash(FileInputStream fis) {
        //byte[] bytes = { 0x35, 0x24, 0x76, 0x12 };
        FileInputStream is = fis;
        MessageDigest md = null; {
        };
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                md.update(buffer, 0, read);
            }
            byte[] md5sum = md.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }


    public static String calculateMD5(FileInputStream fis) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is = fis;

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }
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
