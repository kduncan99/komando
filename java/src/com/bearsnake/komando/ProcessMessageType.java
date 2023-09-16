// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

public enum ProcessMessageType {

    Informational("INFO"),
    Warning("WARN"),
    Error("ERROR"),
    Fatal("FATAL");

    public final String _value;

    ProcessMessageType(
        final String value
    ) {
        _value = value;
    }
}
