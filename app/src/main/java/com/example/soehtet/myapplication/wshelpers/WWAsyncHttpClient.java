package com.example.soehtet.myapplication.wshelpers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class WWAsyncHttpClient extends AsyncHttpClient {
	public RequestHandle patch(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
							   ResponseHandlerInterface responseHandler) {
		HttpPatch request = new HttpPatch(url);
		request.setEntity(entity);
		if (headers != null) request.setHeaders(headers);
		return sendRequest((DefaultHttpClient) getHttpClient(), getHttpContext(), (HttpUriRequest) request,
				contentType, responseHandler, context);
	}

	public RequestHandle delete(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
            ResponseHandlerInterface responseHandler) {
		HttpDeleteWithBody request = new HttpDeleteWithBody(url);
		request.setEntity(entity);
		if (headers != null) request.setHeaders(headers);
		return sendRequest((DefaultHttpClient)getHttpClient(), getHttpContext(), (HttpUriRequest) request,
				contentType, responseHandler, context);
	}
}
