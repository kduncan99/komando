// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando.messages;

public enum MessageType {

    ERROR("ERROR"),
    FATAL("FATAL"),
    INFORMATIONAL("INFO"),
    WARNING("WARN");

    public final String _value;

    MessageType(
        final String value
    ) {
        _value = value;
    }
}
