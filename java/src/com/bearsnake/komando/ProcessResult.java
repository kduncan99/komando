// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import java.util.List;
import java.util.Map;

public class ProcessResult {

    public final boolean _isSuccessful;
    public final ProcessMessage[] _messages;
    public final Map<Switch, Object> _switchSpecifications;
    public final Object[] _positionalArgumentSpecifications;

    public ProcessResult(
        final boolean isSuccessful,
        final List<ProcessMessage> messages,
        final Map<Switch, Object> switchSpecifications,
        final List<Object> positionalArgumentSpecifications
    ) {
        _isSuccessful = isSuccessful;
        _messages = messages.toArray(new ProcessMessage[0]);
        _switchSpecifications = switchSpecifications;
        _positionalArgumentSpecifications = positionalArgumentSpecifications.toArray(new Object[0]);
    }
}
