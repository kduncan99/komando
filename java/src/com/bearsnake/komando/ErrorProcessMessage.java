// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

public class ErrorProcessMessage extends ProcessMessage {

    public ErrorProcessMessage(
        final int argumentIndex,
        final String text
    ) {
        super(argumentIndex, text);
    }

    @Override
    public ProcessMessageType getProcessMessageType() {
        return ProcessMessageType.Error;
    }
}
