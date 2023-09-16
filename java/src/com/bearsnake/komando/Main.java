// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.KommandoException;

public class Main {

    //  temporary code
    public static void main(
        final String[] args
    ) throws KommandoException {
        var usernameSwitch = new ArgumentSwitch.Builder().setShortName("u")
                                                         .setLongName("username")
                                                         .addDescription("User name credentials for access to the flux capacitor.")
                                                         .setIsRequired(false)
                                                         .setIsMultiple(false)
                                                         .setValueName("user")
                                                         .setValueType(ValueType.STRING)
                                                         .build();

        var posArg1 = new PositionalArgument.Builder().setValueName("inputFile")
                                                      .setValueType(ValueType.STRING)
                                                      .addDescription("Path and filename for the input file.")
                                                      .setIsRequired(true)
                                                      .build();

        var posArg2 = new PositionalArgument.Builder().setValueName("outputFile")
                                                      .setValueType(ValueType.STRING)
                                                      .addDescription("Path and filename for the output file.")
                                                      .addDescription("If not specified, output is written to stdout.")
                                                      .setIsRequired(false)
                                                      .build();

        CommandLineHandler clh = new CommandLineHandler();
        clh.addCanonicalHelpSwitch()
           .addCanonicalVersionSwitch()
           .addSwitch(usernameSwitch)
           .addPositionalArgument(posArg1)
           .addPositionalArgument(posArg2);

        var testArgs = new String[]{
            "Flip", "-v", "-h",
        };

        //clh.displayUsage("Flabber");
    }
}
