package com.bearsnake.kommando;

import com.bearsnake.kommando.exceptions.KommandoException;

import static com.bearsnake.kommando.ValueType.STRING;

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
                                                         .setValueType(STRING)
                                                         .build();

        var posArg1 = new PositionalArgument.Builder().setValueName("inputFile")
                                                      .setValueType(STRING)
                                                      .addDescription("Path and filename for the input file.")
                                                      .setIsRequired(true)
                                                      .build();

        var posArg2 = new PositionalArgument.Builder().setValueName("outputFile")
                                                      .setValueType(STRING)
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

        var args = new String[]{
            "Flip", "-v", "-h",
        };

        //clh.displayUsage("Flabber");
    }
}
