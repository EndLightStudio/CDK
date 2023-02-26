package cdk;

public class CodeException extends Exception {
    private final int code;
    private final String text;

    public CodeException(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getMessage() {
        return "code: " + this.code + " " + this.text;
    }
}
