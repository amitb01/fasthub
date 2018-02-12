package com.mapprr.fasthub.network;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    public static String appendUrlParams(String url, Map<String, String> params)
            throws UnsupportedEncodingException {
        if (params == null) {
            return url;
        }

        StringBuilder result = new StringBuilder(url);
        boolean first = false;
        if (!url.contains("?")) {
            result.append("?");
            first = true;
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (first) { first = false; } else { result.append("&"); }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public static Map<String, String> getUrlParams(String url) {
        String query;
        try {
            URI uri = new URI(url);
            query = uri.getQuery();
        } catch (URISyntaxException e) {
            Log.e("URISyntaxException", url);
            Log.e("URISyntaxException", e.getMessage());
            return null;
        }

        if (query == null) {
            query = "";
        }
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            if (param.equalsIgnoreCase("")) { continue; }
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}

