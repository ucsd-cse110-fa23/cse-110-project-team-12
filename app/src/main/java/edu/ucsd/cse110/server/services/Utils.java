package edu.ucsd.cse110.server.services;

import java.util.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Utils {
    public static Map<String, String> getQueryPairs(HttpExchange httpExchange) {
        Map<String, String> result = new HashMap<>();
        try {
            String queryString = httpExchange.getRequestURI().getRawQuery().replace("+", " ");
            String[] queries = queryString.split("&");
    
            for (String query : queries) {
                String[] pair = query.split("=");
                result.put(pair[0], pair[1]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public static <T> T unmarshalJson(String jsonString, Class<T> type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, type);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String marshalJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String encodeBufferedImageToBase64(BufferedImage bImg) {
        try {
           ByteArrayOutputStream s = new ByteArrayOutputStream();
           ImageIO.write(bImg, "jpg", s);
           byte[] res = s.toByteArray();
           String encoded = encodeBase64(res);
           return encoded;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage decodeBase64ToBufferedImage(String encoded) {
        try {
           byte[] decoded = decodeBase64(encoded);
           ByteArrayInputStream inp = new ByteArrayInputStream(decoded);
           return ImageIO.read(inp);
        }
        catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }
}
