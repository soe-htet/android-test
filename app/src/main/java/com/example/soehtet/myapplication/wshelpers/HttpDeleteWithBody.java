package com.example.soehtet.myapplication.wshelpers;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

// http://stackoverflow.com/questions/3773338/httpdelete-with-body
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";
    public String getMethod() { return METHOD_NAME; }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }
    public HttpDeleteWithBody() { super(); }
}