// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ParseException;
import com.bearsnake.komando.exceptions.ValuesNotComparableException;

public class FloatingPointValue extends Value implements Comparable<Value> {

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

    @Override
    public boolean equals(
        final Object obj
    ) {
        return (obj instanceof FloatingPointValue sv) && (_value.equals(sv._value));
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

    @Override
    public int compareTo(
        final Value o
    ) {
        if (o instanceof FloatingPointValue fpv) {
            return _value.compareTo(fpv._value);
        } else if (o instanceof FixedPointValue fpv) {
            var fixed = fpv.getValue();
            double dbl = (double) fixed;
            return _value.compareTo(dbl);
        } else {
            throw new ValuesNotComparableException(this, o);
        }
    }
}
