/*
 * Copyright (c) Dotted Eyes Ltd 2018.
 * All Rights Reserved.
 *
 * This Software is the confidential information of
 * Dotted Eyes Ltd. 67-71 Northwood St,
 * Birmingham, B3 1TX, United Kingdom.
 *
 * The software may be used only in accordance with the terms
 * of the licence agreement made with Dotted Eyes Ltd.
 *
 */
package lib.logging;

import lib.hlt.Position;

import java.util.ArrayList;
import java.util.List;

public class FLog {

    private static FLog INSTANCE;

    List<String> logLines = new ArrayList<>();

    public static void log(int turn, Position position, String message) {
        if (INSTANCE == null) {
            INSTANCE = new FLog();
        }

        INSTANCE.log(turn, position, message);
    }

    public void doLog(int turn, Position position, String message) {
        String value = "{ \"t\": " + turn + ", \"x\": " + position.x + ", \"y\": " + position.y + ", \"msg\": \"" + message + "\" }";
        logLines.add(value);
    }

    public void write() {
        // TODO: write flog to file
    }
}
