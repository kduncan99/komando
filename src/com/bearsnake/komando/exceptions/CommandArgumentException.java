// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

public class CommandArgumentException extends RuntimeException {

    public CommandArgumentException() {
        super("Only one CommandArgument may be specified");
    }
}
