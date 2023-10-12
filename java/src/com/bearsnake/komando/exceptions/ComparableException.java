// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.exceptions;

import com.bearsnake.komando.values.Value;

public class ComparableException extends RuntimeException {

    public ComparableException(
        final Value value1,
        final Value value2
    ) {
        super(String.format("Values '%s' and '%s' are not comparable", value1.toString(), value2.toString()));
    }
}
