package com.example.soehtet.myapplication.wshelpers;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

// http://stackoverflow.com/questions/3773338/httpdelete-with-body
// http://stackoverflow.com/questions/12207373/http-patch-request-from-android
public class HttpPatch extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "PATCH";
    public String getMethod() { return METHOD_NAME; }

    public HttpPatch(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public HttpPatch(final URI uri) {
        super();
        setURI(uri);
    }
    public HttpPatch() { super(); }
}


