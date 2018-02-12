package com.mapprr.fasthub.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class CustomStringRequest extends StringRequest {

    private int mStatusCode;
    private Map<String, String> headerParams = null;
    private Map<String, String> bodyParams = null;

    CustomStringRequest(int method, Map<String, String> headerParams, Map<String, String> bodyParams,
                        String url, Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.headerParams = headerParams;
        this.bodyParams = bodyParams;
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
    protected Map<String, String> getParams() throws AuthFailureError {
        if (bodyParams == null) {
            return super.getParams();
        }
        return bodyParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }
}