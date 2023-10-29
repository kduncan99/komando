// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.restrictions;

import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.exceptions.RestrictionException;
import com.bearsnake.komando.values.Value;

public class RangeRestriction extends Restriction {

    private final Value _lowerLimit;
    private final Value _upperLimit;

    public RangeRestriction(
        final Value lowerLimit,
        final Value upperLimit
    ) {
        _lowerLimit = lowerLimit;
        _upperLimit = upperLimit;
    }

    @Override
    public void check(Value value) throws KomandoException {
        if ((value.compareTo(_lowerLimit) < 0) || (value.compareTo(_upperLimit) > 0)) {
            throw new RestrictionException(value);
        }
    }
}
