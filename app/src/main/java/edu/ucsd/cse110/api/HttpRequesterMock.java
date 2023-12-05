package edu.ucsd.cse110.api;

import java.util.*;
import java.lang.IllegalArgumentException;

public class HttpRequesterMock implements HttpRequesterInterface {
    Queue<ServerResponse> responses;

    HttpRequesterMock() {
        responses = new LinkedList<>();
    }

    public void addResponse(ServerResponse response) {
        responses.add(response);
    }

    public ServerResponse makeHttpRequest(String urlString, String methodType, String requestBody) {
        if (responses.size() == 0)
            throw new IllegalArgumentException("Mock Http Requester has no more responses");
        return responses.remove();
    }
}
