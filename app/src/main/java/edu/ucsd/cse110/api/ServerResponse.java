package edu.ucsd.cse110.api;

public class ServerResponse {
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
