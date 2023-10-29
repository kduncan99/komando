// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

import com.bearsnake.komando.values.Value;

public class RestrictionException extends KomandoException {

    public RestrictionException(
        final Value value
    ) {
        super(String.format("Restriction Violated:'%s' is not an acceptable value", value.toString()));
    }
}
