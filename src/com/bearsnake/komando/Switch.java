// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.values.CommandValue;

import java.util.Arrays;
import java.util.Collection;

public abstract class Switch {

    public final Collection<CommandValue> _affinity;
    public final String _shortName;
    public final String _longName;
    public final String[] _description;

    public Switch(
        final String shortName,
        final String longName,
        final String[] description
    ) {
        this(shortName, longName, description, new CommandValue[0]);
    }

    public Switch(
        final String shortName,
        final String longName,
        final String[] description,
        final CommandValue[] affinity
    ) {
        _shortName = shortName;
        _longName = longName;
        _description = description;
        _affinity = Arrays.stream(affinity).toList();
    }

    public boolean hasAffinity() {
        return !_affinity.isEmpty();
    }

    public boolean hasAffinityWith(
        final CommandValue cmdValue
    ) {
        return _affinity.contains(cmdValue);
    }

    public abstract boolean isRequired();

    @Override
    public final String toString() {
        if (_longName == null) {
            return String.format("-%s", _shortName);
        } else {
            return String.format("-%s/--%s", _shortName, _longName);
        }
    }
}
