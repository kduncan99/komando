// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.messages.Message;
import com.bearsnake.komando.messages.MessageType;
import com.bearsnake.komando.values.CommandValue;
import com.bearsnake.komando.values.Value;

import java.util.List;
import java.util.Map;

public class Result {

    public final List<Message> _messages;
    public final CommandValue _commandValue;
    public final Map<Switch, List<Value>> _switchSpecifications;
    public final List<Value> _positionalArgumentSpecifications;

    public Result(
        final List<Message> messages,
        final CommandValue commandValue,
        final Map<Switch, List<Value>> switchSpecifications,
        final List<Value> positionalArgumentSpecifications
    ) {
        _messages = messages;
        _commandValue = commandValue;
        _switchSpecifications = switchSpecifications;
        _positionalArgumentSpecifications = positionalArgumentSpecifications;
    }

    public boolean hasErrors() {
        for (var m : _messages) {
            var msgType = m.getProcessMessageType();
            if (msgType == MessageType.FATAL || msgType == MessageType.ERROR) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWarnings() {
        for (var m : _messages) {
            if (m.getProcessMessageType() == MessageType.WARNING) {
                return true;
            }
        }
        return false;
    }

    public boolean isHelpRequested() {
        return _switchSpecifications.containsKey(CommandLineHandler.HELP_SWITCH);
    }

    public boolean isVersionRequested() {
        return _switchSpecifications.containsKey(CommandLineHandler.VERSION_SWITCH);
    }
}
