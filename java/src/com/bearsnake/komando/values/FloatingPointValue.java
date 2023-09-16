// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ParseException;

public class FloatingPointValue extends Value {

    private final Double _value;

    public FloatingPointValue(
        final Double value
    ) {
        _value = value;
    }

    public final Double getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value.toString();
    }

    public static FloatingPointValue parse(
        final String input
    ) throws ParseException {
        try {
            return new FloatingPointValue(Double.parseDouble(input));
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("'%s' is not a valid floating-point value", input));
        }
    }
}
