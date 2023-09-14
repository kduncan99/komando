// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.kommando.exceptions;

public class FieldNotSpecifiedException extends KommandoException {

    public FieldNotSpecifiedException(
        final String fieldName
    ) {
        super("Field '" + fieldName + "' was not specified");
    }
}
