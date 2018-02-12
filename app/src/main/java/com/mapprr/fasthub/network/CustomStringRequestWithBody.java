package com.mapprr.fasthub.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class CustomStringRequestWithBody extends StringRequest {

    private int mStatusCode;
    private Map<String, String> headerParams = null;
    private String body = null;

    CustomStringRequestWithBody(int method, Map<String, String> headerParams, String body,
                                String url, Response.Listener<String> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.headerParams = headerParams;
        this.body = body;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headerParams == null) {
            headerParams = new HashMap<>();
        }

        return headerParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body.getBytes();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }
}


