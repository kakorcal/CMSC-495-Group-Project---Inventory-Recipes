package database;

/*
 * Returns user friendly error messages
 * */
public class ErrorHandler {
    private String message;

    public Boolean hasError() {
        if(message.isEmpty()) {
            return false;
        }else {
            return true;
        }
    }

    public void reset() {
        message = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
