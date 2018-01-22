package com.montreal.wtm.model;

public class Talk {

    public Type type;
    public Session session;
    public String time;
    public String room;

    public Talk(Session session, String time, String room) {
        this.type = type;
        this.session = session;
        this.time = time;
        this.room = room;
        
        //TODO CHANGE 
        if (session.getSpeakersId() != null) {
            type = Type.Talk;
        } else {
            type = Type.Break;
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
