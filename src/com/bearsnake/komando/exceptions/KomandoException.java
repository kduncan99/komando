// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

public abstract class KomandoException extends Exception {

    public KomandoException(
        final String message
    ) {
        super(message);
    }
}
