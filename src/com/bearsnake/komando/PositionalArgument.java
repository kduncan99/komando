// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.FieldNotSpecifiedException;
import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.restrictions.Restriction;
import com.bearsnake.komando.values.Value;
import com.bearsnake.komando.values.ValueType;

import java.util.LinkedList;
import java.util.List;

/**
 * A PositionalArgument is a value which is presented on the command line.
 * This value modifies or defines the behavior of the program.
 * The position of the argument is significant.
 */
public class PositionalArgument {

    private final String[] _description;
    private final boolean _isRequired;
    private final Restriction _restriction;
    private final String _valueName;
    private final ValueType _valueType;

    public PositionalArgument(
        final String[] description,
        final boolean isRequired,
        final String valueName,
        final ValueType valueType,
        final Restriction restriction
    ) {
        _description = description;
        _isRequired = isRequired;
        _restriction = restriction;
        _valueName = valueName;
        _valueType = valueType;
    }

    public String[] getDescription() { return _description; }
    public Restriction getRestriction() { return _restriction; }
    public String getValueName() { return _valueName; }
    public ValueType getValueType() { return _valueType; }
    public boolean isRequired() { return _isRequired; }
    public boolean hasRestriction() { return _restriction != null; }

    public void checkRestriction(
        final Value value
    ) throws KomandoException {
        if (_restriction != null) {
            _restriction.check(value);
        }
    }

    public static class Builder {
        private final List<String> _description = new LinkedList<>();
        private boolean _isRequired = false;
        private Restriction _restriction = null;
        private String _valueName = null;
        private ValueType _valueType = null;

        public Builder addDescription(String value) { _description.add(value); return this; }
        public Builder setIsRequired(boolean value) { _isRequired = value; return this; }
        public Builder setRestriction(Restriction value) { _restriction = value; return this; }
        public Builder setValueName(String value) { _valueName = value; return this; }
        public Builder setValueType(ValueType value) { _valueType = value; return this; }

        public PositionalArgument build() throws KomandoException {
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
                _valueType,
                _restriction
            );
        }
    }
}
