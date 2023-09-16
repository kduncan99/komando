// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.messages;

import com.bearsnake.komando.Switch;

public class SwitchMessage extends Message {

    private final Switch _switch;

    public SwitchMessage(
        final MessageType messageType,
        final Switch swch,
        final String message
    ) {
        super(messageType, message);
        _switch = swch;
    }

    @Override
    public String toString() {
        return String.format("%s:Switch %s:%s", getProcessMessageType(), _switch.toString(), getMessage());
    }
}
