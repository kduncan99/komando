// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.messages;

import com.bearsnake.komando.PositionalArgument;

public class PositionalArgumentMessage extends Message {

    private final PositionalArgument _arg;

    public PositionalArgumentMessage(
        final MessageType messageType,
        final PositionalArgument arg,
        final String message
    ) {
        super(messageType, message);
        _arg = arg;
    }

    @Override
    public String toString() {
        return String.format("%s:Positional Argument {%s}:%s",
                             getProcessMessageType(),
                             _arg.getValueName(),
                             getMessage());
    }
}
