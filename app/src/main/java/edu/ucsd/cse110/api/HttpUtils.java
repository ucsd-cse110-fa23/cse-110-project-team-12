package edu.ucsd.cse110.api;

import java.io.*;
import java.net.*;
import java.util.*;

class ServerResponse {
    private int statusCode;
    private String responseBody;

    public ServerResponse(int code, String body) {
        statusCode = code;
        responseBody = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

public class HttpUtils {
    public static ServerResponse makeHttpRequest(String urlString, String methodType, String requestBody) {
        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(methodType);

            // Every method type can have body except GET.
            if (methodType != "GET") {
                conn.setDoOutput(true);
    
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(requestBody);
                out.flush();
                out.close();
            }

            // Success
            int responseCode = conn.getResponseCode();
            if (responseCode / 100 == 2) {
                System.out.println(responseCode + " Successful HTTP Request: " + methodType + " - " + urlString);
                Scanner in = new Scanner(conn.getInputStream());
                String data = "";
                while (in.hasNext())
                    data += in.nextLine();
                in.close();
                return new ServerResponse(responseCode, data);
            }
            else {
                System.out.println(responseCode + " Unsuccessful HTTP Request: " + methodType + " - " + urlString);
                return new ServerResponse(responseCode, "");
            }
        }
        catch (Exception e) {
            System.out.println("HTTP Request Exception: " + methodType + " - " + urlString);
            e.printStackTrace();
        }
        return new ServerResponse(500, "");
    }

    public static boolean isServerAvailable(String urlString) {
        try {
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getResponseCode() == 200;
        }
        catch (Exception e) {
            return false;
        }
    }
}
