package com.dumbster.pop;

public enum POPState {

    AUTHORIZATION, TRANSACTION;

    private static final POPState[] __allValues = POPState.values();

    public static POPState fromString(String s) {
        for (POPState e : __allValues) {
            if (e.name().equals(s)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No such POPState: " + s);
    }
}

