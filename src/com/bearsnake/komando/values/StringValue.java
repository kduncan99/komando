// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ValuesNotComparableException;
import com.bearsnake.komando.exceptions.ParseException;

public class StringValue extends Value {

    private final String _value;

    public StringValue(
        final String value
    ) {
        _value = value;
    }

    public final String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return _value;
    }

    public static StringValue parse(
        final String input
    ) throws ParseException {
        var sb = new StringBuilder();
        var delimited = false;
        var inDelimiter = false;
        char delimiter = 0;
        for (int ix = 0; ix < input.length(); ix++) {
            var ch = input.charAt(ix);
            if ((ix == 0) && ((ch == '\'') || (ch == '"'))) {
                delimited = true;
                inDelimiter = true;
                delimiter = ch;
            } else if (inDelimiter && (ch == delimiter)) {
                inDelimiter = false;
            } else if (!inDelimiter && delimited) {
                throw new ParseException("Improperly-delimited string value");
            } else {
                sb.append(ch);
            }
        }

        if (inDelimiter) {
            throw new ParseException("Missing closing delimiter for string value");
        }

        return new StringValue(sb.toString());
    }

    @Override
    public boolean equals(
        final Object obj
    ) {
        return (obj instanceof StringValue sv) && (_value.equals(sv._value));
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

    @Override
    public int compareTo(
        final Value o
    ) {
        if (o instanceof StringValue stv) {
            return _value.compareTo(stv._value);
        } else {
            throw new ValuesNotComparableException(this, o);
        }
    }
}
