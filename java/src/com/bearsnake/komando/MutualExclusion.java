// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import java.util.LinkedList;
import java.util.List;

public class MutualExclusion {

    private final List<Switch> _switches = new LinkedList<>();

    public MutualExclusion addSwitch(
        final Switch value
    ) {
        _switches.add(value);
        return this;
    }

    public boolean isDisallowed(
        final Switch switch1,
        final Switch switch2
    ) {
        return _switches.contains(switch1) && _switches.contains(switch2);
    }
}
