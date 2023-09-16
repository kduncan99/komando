// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

public abstract class Switch {

    public final String _shortName;
    public final String _longName;
    public final String[] _description;

    public Switch(
        final String shortName,
        final String longName,
        final String[] description
    ) {
        _shortName = shortName;
        _longName = longName;
        _description = description;
    }

    public abstract boolean isRequired();

    @Override
    public final String toString() {
        return String.format("-%s/--%s", _shortName, _longName);
    }
}
