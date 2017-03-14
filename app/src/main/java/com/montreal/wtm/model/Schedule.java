package com.montreal.wtm.model;

public class Schedule {

    private int type;

    private Object data;

    public Schedule(int type, Object data) {
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
