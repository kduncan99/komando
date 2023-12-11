// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.restrictions;

import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.values.Value;

public abstract class Restriction {

    public abstract void check(final Value value) throws KomandoException;
    public abstract String toString(); // force implementation by subclasses
}
