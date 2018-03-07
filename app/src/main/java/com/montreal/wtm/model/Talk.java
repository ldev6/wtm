package com.montreal.wtm.model;

public class Talk {

    public Type type;
    public Session session;
    public String time;
    public String room;
    public boolean saved;

    public Talk(Session session, String time, String room, boolean saved) {
        this.session = session;
        this.time = time;
        this.room = room;
        this.saved = saved;

        //TODO CHANGE 
        if (session.getSpeakersId() != null) {
            type = Type.Talk;
        } else if (session.getType().equals("break")) {
            type = Type.Break;
        } else if (session.getType().equals("food")) {
            type = Type.Food;
        } else {
            type = Type.General;
        }
    }

    public enum Type {
        Talk,
        General,
        Food,
        Break;
    }

    public Type getType() {
        return type;
    }

    public Session getSession() {
        return session;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }
    
}
