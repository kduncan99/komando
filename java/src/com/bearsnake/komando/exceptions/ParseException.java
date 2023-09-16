// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

public class ParseException extends KommandoException {

    public ParseException(
        final String message
    ) {
        super("Cannot parse value:" + message);
    }
}
