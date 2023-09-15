// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

public abstract class KommandoException extends Exception {

    public KommandoException(
        final String message
    ) {
        super(message);
    }
}
