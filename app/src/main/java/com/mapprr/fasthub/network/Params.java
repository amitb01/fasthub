package com.mapprr.fasthub.network;

public interface Params {

    interface Header {

        String CONTENT_TYPE = "Content-Type";
    }

    interface Url {

        String QUERY = "q";
        String SORT = "sort";
        String ORDER = "order";
    }

    interface Body {

        String PAYLOAD = "payload";
    }

}