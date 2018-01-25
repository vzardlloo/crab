package crab;


import crab.handler.HttpHandler;
import crab.http.HttpRequest;
import crab.http.HttpResponse;
import crab.http.HttpSession;

import java.util.List;

public class CrabExecutor implements Runnable{

    private HttpRequest httpRequest;
    private List<HttpHandler> handlers;

    public CrabExecutor(HttpRequest httpRequest, List<HttpHandler> handlers) {
        this.httpRequest = httpRequest;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        for (HttpHandler httpHandler : handlers){
            HttpResponse response = httpHandler.handle(httpRequest);
            HttpSession httpSession = httpRequest.getSession();
            httpSession.sendResponse(response);
            //httpSession.close();
            return;
        }
    }
}
