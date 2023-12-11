// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ValuesNotComparableException;
import com.bearsnake.komando.exceptions.ParseException;

public class FixedPointValue extends Value {

    private final Long _value;

    public FixedPointValue(
        final Long value
    ) {
        _value = value;
    }

    public final Long getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value.toString();
    }

    public static FixedPointValue parse(
        final String input
    ) throws ParseException {
        boolean hexFlag = input.toUpperCase().startsWith("0X");
        var chop = hexFlag ? input.substring(2) : input;
        try {
            return new FixedPointValue(Long.parseLong(chop, hexFlag ? 16 : 10));
        } catch (NumberFormatException ex) {
            throw new ParseException(String.format("'%s' is not a valid integer", input));
        }
    }

    @Override
    public boolean equals(
        final Object obj
    ) {
        return (obj instanceof FixedPointValue sv) && (_value.equals(sv._value));
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

    @Override
    public int compareTo(
        final Value o
    ) {
        if (o instanceof FixedPointValue fpv) {
            return _value.compareTo(fpv._value);
        } else if (o instanceof FloatingPointValue fpv) {
            double flt = fpv.getValue();
            long fixed = (long) flt;
            return _value.compareTo(fixed);
        } else {
            throw new ValuesNotComparableException(this, o);
        }
    }
}
