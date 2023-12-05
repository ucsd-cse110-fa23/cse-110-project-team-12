package edu.ucsd.cse110.api;

public interface HttpRequesterInterface {
    ServerResponse makeHttpRequest(String urlString, String methodType, String requestBody);
}
