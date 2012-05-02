package com.dumbster.pop;

import com.dumbster.pop.action.APOP;
import com.dumbster.pop.action.Action;
import com.dumbster.pop.action.CapabilityList;
import com.dumbster.pop.action.Connect;
import com.dumbster.pop.action.DeleteMessage;
import com.dumbster.pop.action.Invalid;
import com.dumbster.pop.action.List;
import com.dumbster.pop.action.NoOp;
import com.dumbster.pop.action.Password;
import com.dumbster.pop.action.Quit;
import com.dumbster.pop.action.Reset;
import com.dumbster.pop.action.Retrieve;
import com.dumbster.pop.action.Status;
import com.dumbster.pop.action.Top;
import com.dumbster.pop.action.UIDList;
import com.dumbster.pop.action.User;
import com.dumbster.smtp.MailStore;

public class Request {
    private final Action _action;
    private final POPState _state;

    
    public static Request initialRequest() {
        return new Request(new Connect(), POPState.AUTHORIZATION);
    }
    
    public static Request createRequest(POPState currentState, String inputLine) {
        // parse the input line to figure out which Action we're constructing
        Action action = parseInput(currentState, inputLine);
        return new Request(action, currentState);
    }

    /**
     * Constructor for the request
     * 
     * @param action the action specified by the client
     * @param state the current state of the session
     */
    Request(Action action, POPState state) {
        _action = action;
        _state = state;
    }

    public Response execute(MailStore ms) {
        return _action.response(_state, ms);
    }
    
    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    private static Action parseInput(POPState currentState, String inputLine) {
        String ucLine = inputLine.toUpperCase();
        switch (currentState) {
            case AUTHORIZATION: // USER, PASS, APOP, QUIT, CAPA
                if (ucLine.startsWith("USER")) {
                    return new User();
                } else if (ucLine.startsWith("PASS")) {
                    return new Password();
                } else if (ucLine.startsWith("APOP")) {
                    return new APOP();
                } else if (ucLine.startsWith("QUIT")) {
                    return new Quit();
                } else if (ucLine.startsWith("CAPA")) {
                    return new CapabilityList();
                } else {
                    // invalid command for this state. If we actually cared, there'd be some logic here
                    return new Invalid("invalid command for state "+currentState);
                }
            case TRANSACTION: // CAPA, STAT, LIST, RETR, DELE, NOOP, RSET, TOP, UIDL
                if (ucLine.startsWith("CAPA")) {
                    return new CapabilityList();
                } else if (ucLine.startsWith("STAT")) {
                    return new Status();
                } else if (ucLine.startsWith("LIST")) {
                    return new List(inputLine.substring(4));
                } else if (ucLine.startsWith("RETR")) {
                    return new Retrieve(inputLine.substring(4));
                } else if (ucLine.startsWith("DELE")) {
                    return new DeleteMessage(inputLine.substring(4));
                } else if (ucLine.startsWith("NOOP")) {
                    return new NoOp();
                } else if (ucLine.startsWith("QUIT")) {
                    return new Quit();
                } else if (ucLine.startsWith("RSET")) {
                    return new Reset();
                } else if (ucLine.startsWith("TOP")) {
                    return new Top();
                } else if (ucLine.startsWith("UIDL")) {
                    return new UIDList(inputLine);
                } else {
                    // invalid command for this state. If we actually cared, there'd be some logic here
                    return new Invalid("invalid command for state "+currentState);
                }
        }
        
        // we really can't get here, but if we do we should probably just quit
        return new Quit();
    }
}
