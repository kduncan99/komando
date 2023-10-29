// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.values;

import com.bearsnake.komando.exceptions.ComparableException;

public class EmptyValue extends Value {

    public EmptyValue() {}

    @Override
    public String toString() {
        return "<empty>";
    }

    @Override
    public boolean equals(
        final Object obj
    ) {
        return (obj instanceof EmptyValue);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(
        final Value o
    ) {
        throw new ComparableException(this, o);
    }
}
