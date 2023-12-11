// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.restrictions;

import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.exceptions.RestrictionException;
import com.bearsnake.komando.values.StringValue;
import com.bearsnake.komando.values.Value;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class EnumerationRestriction extends Restriction {

    private final Collection<Value> _acceptedValues = new LinkedList<>();

    public EnumerationRestriction(
        Collection<Value> acceptedValues
    ) {
        _acceptedValues.addAll(acceptedValues);
    }

    public EnumerationRestriction(
        String[] acceptedValues
    ) {
        _acceptedValues.addAll(Arrays.stream(acceptedValues)
                                     .map(StringValue::new)
                                     .collect(Collectors.toCollection(LinkedList::new)));
    }

    public Collection<Value> getAcceptedValues() { return new LinkedList<>(_acceptedValues); }

    @Override
    public void check(Value value) throws KomandoException {
        for (var av : _acceptedValues) {
            if (av.equals(value)) {
                return;
            }
        }

        throw new RestrictionException(value);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("[ ");
        var first = true;
        for (var av : _acceptedValues) {
            if (!first) {
                sb.append(" | ");
            }
            sb.append(av.toString());
            first = false;
        }
        sb.append(" ]");
        return sb.toString();
    }
}
