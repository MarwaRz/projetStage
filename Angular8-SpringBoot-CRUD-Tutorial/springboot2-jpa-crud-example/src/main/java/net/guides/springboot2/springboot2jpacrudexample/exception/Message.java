package net.guides.springboot2.springboot2jpacrudexample.exception;

public class Message {
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message() {
        super();

    }

    public Message(String message) {
        super();
        this.message = message;
    }


}
