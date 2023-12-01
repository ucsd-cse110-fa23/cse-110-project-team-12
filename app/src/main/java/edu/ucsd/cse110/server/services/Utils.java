package edu.ucsd.cse110.server.services;

import java.util.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import java.nio.charset.StandardCharsets;
import java.net.URI;

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
}
