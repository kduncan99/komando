// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.values.ValueType;

public class Main {

    //  temporary code
    public static void main(
        final String[] args
    ) throws KomandoException {
        var usernameSwitch = new ArgumentSwitch.Builder().setShortName("u")
                                                         .setLongName("username")
                                                         .addDescription("User name credentials for access to the flux capacitor.")
                                                         .setIsRequired(false)
                                                         .setIsMultiple(false)
                                                         .setValueName("user")
                                                         .setValueType(ValueType.STRING)
                                                         .build();

        var passwordSwitch = new ArgumentSwitch.Builder().setShortName("p")
                                                         .setLongName("password")
                                                         .addDescription("Password credentials for access to the flux capacitor.")
                                                         .setIsRequired(false)
                                                         .setIsMultiple(false)
                                                         .setValueName("pwd")
                                                         .setValueType(ValueType.STRING)
                                                         .build();

        var boolSwitch = new ArgumentSwitch.Builder().setShortName("b")
                                                     .setLongName("bool")
                                                     .addDescription("An array of bools")
                                                     .setIsMultiple(true)
                                                     .setValueName("boolValue")
                                                     .setValueType(ValueType.BOOLEAN)
                                                     .build();

        var intSwitch = new ArgumentSwitch.Builder().setShortName("i")
                                                    .addDescription("integer value")
                                                    .setIsMultiple(true)
                                                    .setValueName("intValue")
                                                    .setValueType(ValueType.FIXED_POINT)
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
           .addSwitch(passwordSwitch)
           .addSwitch(boolSwitch)
           .addSwitch(intSwitch)
           .addPositionalArgument(posArg1)
           .addPositionalArgument(posArg2)
           .addDependency(usernameSwitch, passwordSwitch)
           .addMutualExclusion(boolSwitch, intSwitch);

        var testArgs = new String[] {
            "-u=kduncan", "-p=foo", "-i=42,-55,0X1000", "inpFile", "outpFile", "overFile"
        };

        var result = clh.processCommandLine(testArgs);
        if (result.hasErrors()) {
            System.out.println("Found errors.");
        }

        if (result.hasWarnings()) {
            System.out.println("Found warnings.");
        }

        for (var msg : result._messages) {
            System.out.println(msg);
        }

        System.out.println("Switches:");
        for (var swEntry : result._switchSpecifications.entrySet()) {
            var sw = swEntry.getKey();
            var values = swEntry.getValue();
            System.out.println("  " + sw.toString() + ":");
            if (values != null) {
                for (var value : values) {
                    System.out.printf("    %s\n", value.toString());
                }
            }
        }

        System.out.println("PosArgs:");
        for (int px = 0; px < result._positionalArgumentSpecifications.size(); ++px) {
            System.out.printf("  %d:%s\n", px, result._positionalArgumentSpecifications.get(px).toString());
        }

        //clh.displayUsage("flip");
    }
}
