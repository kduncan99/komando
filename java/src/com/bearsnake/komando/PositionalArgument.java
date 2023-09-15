// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.FieldNotSpecifiedException;
import com.bearsnake.komando.exceptions.KommandoException;

import java.util.LinkedList;
import java.util.List;

/**
 * A PositionalArgument is a value which is presented on the command line.
 * This value modifies or defines the behavior of the program.
 * The position of the argument is significant.
 */
public class PositionalArgument {

    public final String[] _description;
    public final boolean _isRequired;
    public final String _valueName;
    public final ValueType _valueType;

    public PositionalArgument(
        final String[] description,
        final boolean isRequired,
        final String valueName,
        final ValueType valueType
    ) {
        _description = description;
        _isRequired = isRequired;
        _valueName = valueName;
        _valueType = valueType;
    }

    public static class Builder {
        private final List<String> _description = new LinkedList<>();
        private boolean _isRequired = false;
        private String _valueName = null;
        private ValueType _valueType = null;

        public Builder addDescription(String value) { _description.add(value); return this; }
        public Builder setIsRequired(boolean value) { _isRequired = value; return this; }
        public Builder setValueName(String value) { _valueName = value; return this; }
        public Builder setValueType(ValueType value) { _valueType = value; return this; }

        public PositionalArgument build() throws KommandoException {
            if (_description.isEmpty()) {
                throw new FieldNotSpecifiedException("description");
            }

            if (_valueName == null) {
                throw new FieldNotSpecifiedException("valueName");
            }

            if (_valueType == null) {
                throw new FieldNotSpecifiedException("valueType");
            }

            return new PositionalArgument(
                _description.toArray(new String[0]),
                _isRequired,
                _valueName,
                _valueType
            );
        }
    }
}
