package com.montreal.wtm.utils.model;


public class DataRowDebugItem {

    public Object data;
    public int type;

    public DataRowDebugItem(int type) {
        this(type, null);
    }

    public DataRowDebugItem(int type, Object data) {
        this.type = type;
        this.data = data;
    }

}
