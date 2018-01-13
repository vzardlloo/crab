package crab.handler;


import crab.http.HttpRequest;
import crab.http.HttpResponse;



public interface HttpHandler {

    public HttpResponse handle(HttpRequest request);

}
