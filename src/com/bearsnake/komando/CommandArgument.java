// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.FieldNotSpecifiedException;
import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.values.CommandValue;
import com.bearsnake.komando.values.StringValue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A CommandArgument is a PositionalArgument which indicates a particular command to be executed.
 * The potential values for the CommandArgument are automatically restricted.
 * There may be at most one CommandArgument, and it must be the first of the PositionalArguments.
 * The CommandArgument is always required, and it always has an EnumerationRestriction.
 */
public class CommandArgument {

    private final String[] _description;
    private final CommandValue[] _commands;

    public CommandArgument(
        final String[] description,
        final CommandValue[] commands
    ) {
        _description = description;
        _commands = commands;
    }

    /**
     * Given a StringValue we look for the original CommandValue which corresponds to that string value.
     * @param value for which we look
     * @return matching CommandValue if found, else null
     */
    CommandValue findCommandValueForString(
        final StringValue value
    ) {
        for (var cv : _commands) {
            if (cv.equals(value)) {
                return cv;
            }
        }

        return null;
    }

    public String[] getDescription() { return _description; }

    public static class Builder {
        private final List<String> _description = new LinkedList<>();
        private final List<CommandValue> _commandValues = new LinkedList<>();

        public Builder addDescription(String value) { _description.add(value); return this; }
        public Builder addCommandValue(CommandValue value) { _commandValues.add(value); return this; }

        public CommandArgument build() throws KomandoException {
            if (_description.isEmpty()) {
                throw new FieldNotSpecifiedException("description");
            }

            if (_commandValues.isEmpty()) {
                throw new FieldNotSpecifiedException("commandValue");
            }

            return new CommandArgument(
                _description.toArray(new String[0]),
                _commandValues.toArray(new CommandValue[0])
            );
        }
    }

    public String getChoicesString() {
        var temp = Arrays.stream(_commands).map(StringValue::getValue).collect(Collectors.toCollection(LinkedList::new));
        return "[ " + String.join(" | ", temp) + " ]";
    }
}
