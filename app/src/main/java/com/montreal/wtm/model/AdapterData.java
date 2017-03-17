package com.montreal.wtm.model;


public class AdapterData {

    private int type;

    private Object data;

    public AdapterData(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
