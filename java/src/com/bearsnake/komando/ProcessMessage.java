// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

public abstract class ProcessMessage {

    private final String _text;
    private final int _argumentIndex;

    protected ProcessMessage(
        final int argumentIndex,
        final String text
    ) {
        _argumentIndex = argumentIndex;
        _text = text;
    }

    public String toString() {
        return String.format("%s:%s", getProcessMessageType()._value, _text);
    }

    public abstract ProcessMessageType getProcessMessageType();

}
