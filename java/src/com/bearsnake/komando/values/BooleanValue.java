// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ParseException;

public class BooleanValue extends Value {

    private final Boolean _value;

    public BooleanValue(
        final Boolean value
    ) {
        _value = value;
    }

    public final Boolean getValue() {
        return _value;
    }

    public static BooleanValue parse(
        final String input
    ) throws ParseException {
        var inputUpper = input.toUpperCase();
        if (inputUpper.equals("TRUE") || inputUpper.equals("YES") || inputUpper.equals("ON")) {
            return new BooleanValue(true);
        } else if (inputUpper.equals("FALSE") || inputUpper.equals("NO") || inputUpper.equals("OFF")) {
            return new BooleanValue(false);
        } else {
            throw new ParseException(String.format("'%s' cannot be interpreted as a boolean value", input));
        }
    }

}
