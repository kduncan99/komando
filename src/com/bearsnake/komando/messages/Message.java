// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.messages;

public class Message {

    private final String _message;
    private final MessageType _messageType;

    public Message(
        final MessageType messageType,
        final String message
    ) {
        _messageType = messageType;
        _message = message;
    }

    public final String getMessage() {
        return _message;
    }

    public final MessageType getProcessMessageType() {
        return _messageType;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", _messageType._value, _message);
    }
}
