// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.messages;

public class CommandArgumentMessage extends Message {

    public CommandArgumentMessage(
        final MessageType messageType,
        final String message
    ) {
        super(messageType, message);
    }

    @Override
    public String toString() {
        return String.format("%s:Command:%s",
                             getProcessMessageType(),
                             getMessage());
    }
}
