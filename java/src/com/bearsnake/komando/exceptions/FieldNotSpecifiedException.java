// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

public class FieldNotSpecifiedException extends KomandoException {

    public FieldNotSpecifiedException(
        final String fieldName
    ) {
        super("Field '" + fieldName + "' was not specified");
    }
}
