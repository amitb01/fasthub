package com.mapprr.fasthub.network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.mapprr.fasthub.core.Mapprr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MasterNetworkTask {

    //region Data Members
    private NetworkManager mNetworkManager;
    private CustomStringRequest mCustomStringRequest;
    private CustomStringRequestWithBody mCustomStringRequestWithBody;

    private ResponseCallbacks mResponseCallbacks;
    private Class mResponseClass;
    private Type mType;

    private Map<String, String> headerParams = null;
    private Map<String, String> mUrlParams = null;
    private String TAG = null;
    private int mInitialTimeoutMs = 30000;
    private int mMaxNumRetries = 0;
    private float mBackoffMultiplier = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    private int mStatusCode;

    //endregion

    //region Constructors

    /**
     * @param TAG - Tag related to this API call, can be used to cancel this call
     */
    public MasterNetworkTask(String TAG) {
        this();
        this.TAG = TAG;
    }

    public MasterNetworkTask() {
        this.mNetworkManager = NetworkManager.getInstance();
    }

    //endregion

    //region Getters Setters

    /**
     * @param responseClass     return type ex. String.class
     * @param responseCallbacks callback for response with same responseClass type in callback arguments
     */
    public void setResponseCallbacks(Class responseClass, ResponseCallbacks responseCallbacks) {
        this.mResponseCallbacks = responseCallbacks;
        this.mResponseClass = responseClass;
    }

    /**
     * @param type              return type used if api returns a List, ArrayList of some class object you can use this
     *                          ex. Type type = new TypeToken<ArrayList<YourClassModel>>(){}.getType();
     * @param responseCallbacks callback for response with same responseClass type in callback arguments
     */
    public void setResponseCallbacks(Type type, ResponseCallbacks responseCallbacks) {
        this.mResponseCallbacks = responseCallbacks;
        this.mType = type;
    }

    /**
     * @param mUrlParams url params that will be appended in the url
     *                   NOTE: do not call this function after GET / POST / PUT / DELETE
     */
    public void setUrlParams(Map<String, String> mUrlParams) {
        if (mCustomStringRequestWithBody == null && mCustomStringRequest == null) {
            this.mUrlParams = mUrlParams;
        } else {
            throw new RuntimeException("Url Params can't be set after calling GET/DELETE/POST/PUT");
        }
    }

    /**
     * @param headerParams request header params for this API call
     *                     NOTE: do not call this function after GET / POST / PUT / DELETE
     */
    public void setHeaderParams(Map<String, String> headerParams) {
        if (mCustomStringRequestWithBody == null && mCustomStringRequest == null) {
            if (headerParams != null && headerParams.size() > 0) {
                for (String key : headerParams.keySet()) {
                    this.headerParams.put(key, headerParams.get(key));
                }
            }
        } else {
            throw new RuntimeException("Header can't be set after calling GET/DELETE/POST/PUT");
        }

    }

    public void setBackoffMultiplier(float mBackoffMultiplier) {
        this.mBackoffMultiplier = mBackoffMultiplier;
    }

    /**
     * @param mMaxNumRetries default is 2
     */
    public void setMaxNumRetries(int mMaxNumRetries) {
        this.mMaxNumRetries = mMaxNumRetries;
    }

    public void setInitialTimeoutMs(int mInitialTimeoutMs) {
        this.mInitialTimeoutMs = mInitialTimeoutMs;
    }

    /**
     * @return http statusCode of the response ex. 404
     */
    public int getStatusCode() {
        return mStatusCode;
    }

    //endregion


    //region GET Request

    public void GET(String url) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {

            }
        }
        if (TAG != null) { Log.d(TAG, "GET : " + url); }

        mCustomStringRequest = new CustomStringRequest(Request.Method.GET, headerParams, null, url,
                                                       new Response.Listener<String>() {
                                                           @Override
                                                           public void onResponse(String response) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleSuccess(response, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       },
                                                       new Response.ErrorListener() {
                                                           @Override
                                                           public void onErrorResponse(VolleyError error) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleError(error, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       });
    }

    //endregion

    //region DELETE request

    public void DELETE(String url) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (TAG != null) { Log.d(TAG, "DELETE : " + url); }

        mCustomStringRequest = new CustomStringRequest(Request.Method.DELETE, headerParams, null, url,
                                                       new Response.Listener<String>() {
                                                           @Override
                                                           public void onResponse(String response) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleSuccess(response, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       },
                                                       new Response.ErrorListener() {
                                                           @Override
                                                           public void onErrorResponse(VolleyError error) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleError(error, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       });

    }

    //endregion

    //region POST request

    public void POST(String url, JSONObject jsonObject) {
        POST(url, jsonObject.toString());
    }

    public void POST(String url, JSONArray jsonArray) {
        POST(url, jsonArray.toString());
    }

    public void POST(String url, String body) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {
            }
        }

        if (TAG != null) {
            Log.d(TAG, "POST : " + url + "\nPayload : " + (body != null ? body : "null"));
        }

        mCustomStringRequestWithBody = new CustomStringRequestWithBody(Request.Method.POST, headerParams, body, url,
                                                                       new Response.Listener<String>() {
                                                                           @Override
                                                                           public void onResponse(String response) {
                                                                               if (!mCustomStringRequestWithBody.isCanceled()) {
                                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                                   handleSuccess(response, mCustomStringRequestWithBody.getUrl());
                                                                               }
                                                                           }
                                                                       },
                                                                       new Response.ErrorListener() {
                                                                           @Override
                                                                           public void onErrorResponse(VolleyError error) {
                                                                               if (!mCustomStringRequestWithBody.isCanceled()) {
                                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                                   handleError(error, mCustomStringRequestWithBody.getUrl());
                                                                               }
                                                                           }
                                                                       });

    }

    public void POST(String url, HashMap<String, String> bodyParams) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (TAG != null) { Log.d(TAG, "POST : " + url); }

        mCustomStringRequest = new CustomStringRequest(Request.Method.POST, headerParams, bodyParams, url,
                                                       new Response.Listener<String>() {
                                                           @Override
                                                           public void onResponse(String response) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleSuccess(response, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       },
                                                       new Response.ErrorListener() {
                                                           @Override
                                                           public void onErrorResponse(VolleyError error) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequest.getStatusCode();
                                                                   handleError(error, mCustomStringRequest.getUrl());
                                                               }
                                                           }
                                                       });

    }
    //endregion

    //region PUT request

    public void PUT(String url, JSONObject jsonObject) {
        PUT(url, jsonObject.toString());
    }

    public void PUT(String url, JSONArray jsonArray) {
        PUT(url, jsonArray.toString());
    }

    public void PUT(String url, String body) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {
            }
        }

        if (TAG != null) {
            Log.d(TAG, "PUT : " + url + "\nPayload : " + (body != null ? body : "null"));
        }

        mCustomStringRequestWithBody = new CustomStringRequestWithBody(Request.Method.PUT, headerParams, body, url,
                                                                       new Response.Listener<String>() {
                                                                           @Override
                                                                           public void onResponse(String response) {
                                                                               if (!mCustomStringRequestWithBody.isCanceled()) {
                                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                                   handleSuccess(response, mCustomStringRequestWithBody.getUrl());
                                                                               }
                                                                           }
                                                                       },
                                                                       new Response.ErrorListener() {
                                                                           @Override
                                                                           public void onErrorResponse(VolleyError error) {
                                                                               if (!mCustomStringRequestWithBody.isCanceled()) {
                                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                                   handleError(error, mCustomStringRequestWithBody.getUrl());
                                                                               }
                                                                           }
                                                                       });
    }

    public void PUT(String url, HashMap<String, String> bodyParams) {
        if (mUrlParams != null) {
            try {
                url = NetworkUtils.appendUrlParams(url, mUrlParams);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (TAG != null) { Log.d(TAG, "PUT : " + url); }

        mCustomStringRequest = new CustomStringRequest(Request.Method.PUT, headerParams, bodyParams, url,
                                                       new Response.Listener<String>() {
                                                           @Override
                                                           public void onResponse(String response) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                   handleSuccess(response, mCustomStringRequestWithBody.getUrl());
                                                               }
                                                           }
                                                       },
                                                       new Response.ErrorListener() {
                                                           @Override
                                                           public void onErrorResponse(VolleyError error) {
                                                               if (!mCustomStringRequest.isCanceled()) {
                                                                   mStatusCode = mCustomStringRequestWithBody.getStatusCode();
                                                                   handleError(error, mCustomStringRequestWithBody.getUrl());
                                                               }
                                                           }
                                                       });
    }

    //endregion

    //region Member functions

    public void execute() {
        if (mCustomStringRequest != null) {
            mCustomStringRequest.setTag(TAG);
            mCustomStringRequest.setRetryPolicy(new DefaultRetryPolicy(mInitialTimeoutMs, mMaxNumRetries, mBackoffMultiplier));
            mNetworkManager.addToRequestQueue(mCustomStringRequest);
        } else if (mCustomStringRequestWithBody != null) {
            mCustomStringRequestWithBody.setTag(TAG);
            mCustomStringRequestWithBody.setRetryPolicy(new DefaultRetryPolicy(mInitialTimeoutMs, mMaxNumRetries, mBackoffMultiplier));
            mNetworkManager.addToRequestQueue(mCustomStringRequestWithBody);
        } else {
            throw new RuntimeException("Call GET/POST/PUT/DELETE functions before calling execute");
        }
    }

    /**
     * cancels all call for given TAG
     *
     * @param Tag
     */
    public void cancelAll(String Tag) {
        mNetworkManager.getRequestQueue().cancelAll(Tag);
    }

    /**
     * handles success response, returns response in given Type
     *
     * @param response
     */
    private void handleSuccess(String response, String url) {
        if (TAG != null && response != null) {
            Log.d(TAG, "                <-----------Success--------->\n" + response);
        }

        if (mResponseCallbacks == null) {
            throw new RuntimeException("Response callback not implemented");
        } else if ((mResponseClass != null && mResponseClass == String.class) || (mType != null && mType == new TypeToken<String>() {
        }.getType())) {
            mResponseCallbacks.onSuccessfulResponse(response);
        } else if ((mResponseClass != null && mResponseClass == JSONObject.class) || (mType != null && mType == new TypeToken<JSONObject>() {
        }.getType())) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                mResponseCallbacks.onSuccessfulResponse(jsonObject);
            } catch (JSONException e) {
                Log.i("url: %s", url);
                Log.i("response: %s", response);
                Log.e("EXCEPTION", e.getMessage());
                mResponseCallbacks.onSuccessfulResponse(null);
            }
        } else if ((mResponseClass != null && mResponseClass == JSONArray.class) || (mType != null && mType == new TypeToken<JSONArray>() {
        }.getType())) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                mResponseCallbacks.onSuccessfulResponse(jsonArray);
            } catch (JSONException e) {
                Log.i("url: %s", url);
                Log.i("response: %s", response);
                Log.e("EXCEPTION", e.getMessage());
                mResponseCallbacks.onSuccessfulResponse(null);
            }
        } else if (mResponseClass != null) {
            Object responseObject = Mapprr.getInstance().getGsonInstance().fromJson(response, mResponseClass);
            mResponseCallbacks.onSuccessfulResponse(responseObject);
        } else {
            Object responseObject = Mapprr.getInstance().getGsonInstance().fromJson(response, mType);
            mResponseCallbacks.onSuccessfulResponse(responseObject);
        }
    }

    private void handleError(VolleyError error, String url) {
        if (mResponseCallbacks == null) {
            throw new RuntimeException("Response callback not implemented");
        }
        Log.d(TAG, "                <-----------Failure--------->\nStatusCode : " + mStatusCode + "\nMessage : " + error.getMessage());
        mResponseCallbacks.onErrorResponse(mStatusCode, error.getMessage());
    }

    //endregion

    //region Response Callback Interfaces
    public interface ResponseCallbacks<T> {

        void onSuccessfulResponse(T response);

        void onErrorResponse(int statusCode, String errorMessage);
    }
    //endregion
}

