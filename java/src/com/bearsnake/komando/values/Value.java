// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ParseException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Value implements Comparable<Value> {

    public static List<Object> parseList(
        final Collection<String> array,
        final ValueType valueType
    ) throws ParseException {
        var result = new LinkedList<>();
        for (var input : array) {
            result.add(parseText(input, valueType));
        }
        return result;
    }

    public static Value parseText(
        final String input,
        final ValueType valueType
    ) throws ParseException {
        return switch (valueType) {
            case BOOLEAN -> BooleanValue.parse(input);
            case FIXED_POINT -> FixedPointValue.parse(input);
            case FLOATING_POINT -> FloatingPointValue.parse(input);
            case STRING -> StringValue.parse(input);
        };
    }

    @Override
    public abstract String toString();
}
