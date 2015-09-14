package com.unfc.choicecustomercare.models;

import java.io.Serializable;

/**
 * Created by bhupinder on 14/9/15.
 */
public class UpdateResponseModel implements Serializable {

    private boolean result;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
