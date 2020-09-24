package com.example.yoyo;

public class Responses {
    String message;
    boolean person_or_bot;

    public Responses(String message, boolean person_or_bot) {
        this.message = message;
        this.person_or_bot = person_or_bot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPerson_or_bot() {
        return person_or_bot;
    }

    public void setPerson_or_bot(boolean person_or_bot) {
        this.person_or_bot = person_or_bot;
    }
}
