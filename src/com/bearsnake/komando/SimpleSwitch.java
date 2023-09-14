// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.kommando;

import com.bearsnake.kommando.exceptions.FieldNotSpecifiedException;
import com.bearsnake.kommando.exceptions.KommandoException;

import java.util.LinkedList;
import java.util.List;

/**
 * A SimpleSwitch is a command line entity preceded by one or two hyphens which indicates
 * that a certain behavior should be instigated. It does not have any attached argument.
 */
public class SimpleSwitch extends Switch {

    public SimpleSwitch(
        final String _shortName,
        final String _longName,
        final String[] _description
    ) {
        super(_shortName, _longName, _description);
    }

    public static class Builder {
        private final List<String> _description = new LinkedList<>();
        private String _longName = null;
        private String _shortName = null;

        public Builder addDescription(String value) { _description.add(value); return this; }
        public Builder setLongName(String value) { _longName = value; return this; }
        public Builder setShortName(String value) { _shortName = value; return this; }

        public Switch build() throws KommandoException {
            if (_shortName == null) {
                throw new FieldNotSpecifiedException("shortName");
            }

            if (_description.isEmpty()) {
                throw new FieldNotSpecifiedException("description");
            }

            return new SimpleSwitch(
                _shortName,
                _longName,
                _description.toArray(new String[0])
            );
        }
    }
}
