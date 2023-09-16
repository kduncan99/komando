// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.FieldNotSpecifiedException;
import com.bearsnake.komando.exceptions.KommandoException;
import com.bearsnake.komando.values.ValueType;

import java.util.LinkedList;
import java.util.List;

/**
 * An ArgumentSwitch is a command line entity preceded by one or two hyphens which indicates
 * that a certain behavior should be instigated, and expects one (or optionally multiple) corresponding
 * values which further determine said behavior.
 */
public class ArgumentSwitch extends Switch {

    private final boolean _isMultiple;
    private final boolean _isRequired;
    private final String _valueName;
    private final ValueType _valueType;

    public ArgumentSwitch(
        final String shortName,
        final String longName,
        final String[] description,
        final boolean isRequired,
        final boolean isMultiple,
        final String valueName,
        final ValueType valueType
    ) {
        super(shortName, longName, description);
        _isMultiple = isMultiple;
        _isRequired = isRequired;
        _valueName = valueName;
        _valueType = valueType;
    }

    public boolean isMultiple() { return _isMultiple; }
    public String getValueName() { return _valueName; }
    public ValueType getValueType() { return _valueType; }

    @Override
    public boolean isRequired() { return _isRequired; }

    public static class Builder {
        private final List<String> _description = new LinkedList<>();
        private boolean _isMultiple = false;
        private boolean _isRequired = false;
        private String _longName = null;
        private String _shortName = null;
        private String _valueName = null;
        private ValueType _valueType = null;

        public Builder addDescription(String value) { _description.add(value); return this; }
        public Builder setIsMultiple(boolean value) { _isMultiple = value; return this; }
        public Builder setIsRequired(boolean value) { _isRequired = value; return this; }
        public Builder setLongName(String value) { _longName = value; return this; }
        public Builder setShortName(String value) { _shortName = value; return this; }
        public Builder setValueName(String value) { _valueName = value; return this; }
        public Builder setValueType(ValueType value) { _valueType = value; return this; }

        public ArgumentSwitch build() throws KommandoException {
            if (_shortName == null) {
                throw new FieldNotSpecifiedException("shortName");
            }

            if (_description.isEmpty()) {
                throw new FieldNotSpecifiedException("description");
            }

            if (_valueName == null) {
                throw new FieldNotSpecifiedException("valueName");
            }

            if (_valueType == null) {
                throw new FieldNotSpecifiedException("valueType");
            }

            return new ArgumentSwitch(
                _shortName,
                _longName,
                _description.toArray(new String[0]),
                _isRequired,
                _isMultiple,
                _valueName,
                _valueType
            );
        }
    }
}
