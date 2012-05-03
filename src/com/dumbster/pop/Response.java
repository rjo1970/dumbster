package com.dumbster.pop;

/**
 * POP3 response container. For details about the responses and the rest of
 * the protocol, see RFC 1939. https://tools.ietf.org/html/rfc1939
 */
public class Response {
    // These are the response codes POP3 uses.
    public static final String OK = "+OK";
    public static final String ERROR = "-ERR";

    private final String _code;
    
    private final String _message;
    
    private final POPState _nextState;
    
    public Response(String code, String message, POPState nextState) {
        _code = code;
        _message = message;
        _nextState = nextState;
    }

    public String getCode() {
        return _code;
    }

    public String getMessage() {
        return _message;
    }

    public POPState getNextState() {
        return _nextState;
    }
}
