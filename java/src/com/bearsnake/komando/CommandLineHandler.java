// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.KommandoException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineHandler {

    private final List<PositionalArgument> _positionalArguments = new LinkedList<>();
    private final List<Switch> _switches = new LinkedList<>();
    private final List<MutualExclusion> _mutualExclusions = new LinkedList<>();

    private int _argIndex;
    private boolean _errorFlag;
    private boolean _fatalFlag;
    private final List<ProcessMessage> _messages = new LinkedList<>();
    private final Map<Switch, Object> _switchSpecifications = new HashMap<>();
    private final List<Object> _positionalSpecifications = new LinkedList<>();

    public CommandLineHandler addCanonicalHelpSwitch() throws KommandoException {
        var sw = new SimpleSwitch.Builder().setShortName("h")
                                           .setLongName("help")
                                           .addDescription("Displays usage for this program.")
                                           .build();
        _switches.add(sw);
        return this;
    }

    public CommandLineHandler addCanonicalVersionSwitch() throws KommandoException {
        var sw = new SimpleSwitch.Builder().setShortName("v")
                                           .setLongName("version")
                                           .addDescription("Displays program version.")
                                           .build();
        _switches.add(sw);
        return this;
    }

    public CommandLineHandler addMutualExclusion(
        final MutualExclusion value
    ) {
        _mutualExclusions.add(value);
        return this;
    }

    public CommandLineHandler addPositionalArgument(
        final PositionalArgument value
    ) {
        _positionalArguments.add(value);
        return this;
    }

    public CommandLineHandler addSwitch(
        final Switch value
    ) {
        _switches.add(value);
        return this;
    }

    public void displayUsage(
        final String programName
    ) {
        System.err.println("Usage:");

        var sb = new StringBuilder();
        sb.append("  ").append(programName);
        if (!_switches.isEmpty()) {
            sb.append(" {switches}");
        }
        for (var posArg : _positionalArguments) {
            sb.append(" ")
              .append(posArg._isRequired ? "" : "[")
              .append("{")
              .append(posArg._valueName)
              .append("}").append(posArg._isRequired ? "" : "]");
        }
        System.err.println(sb);

        if (!_switches.isEmpty()) {
            System.err.println();
            System.err.println("switches:");
            for (var sw : _switches) {
                sb = new StringBuilder();
                sb.append("  -").append(sw._shortName);
                if (sw._longName != null) {
                    sb.append(", --").append(sw._longName);
                }

                if (sw instanceof ArgumentSwitch as) {
                    sb.append(" {").append(as._valueName).append("}");
                    if (as._isMultiple) {
                        sb.append(",...");
                    }

                    sb.append("  ");
                    sb.append(as._isRequired ? "(required)" : "(optional)");
                }
                System.err.println(sb);

                for (var desc : sw._description) {
                    System.err.printf("    %s\n", desc);
                }
            }
        }

        for (var posArg : _positionalArguments) {
            System.err.println();
            System.err.printf("%s (%s)\n", posArg._valueName, posArg._valueType.name().toLowerCase());
            for (var desc : posArg._description) {
                System.err.printf("  %s\n", desc);
            }
        }
    }

    public void processPositionalArgument(
        final String arg
    ) {

    }

    public void processSwitch(
        final String arg
    ) {

    }


    public ProcessResult processCommandLine(
        final String[] args
    ) {
        _argIndex = 0;
        _errorFlag = false;
        _fatalFlag = false;
        _messages.clear();
        _positionalSpecifications.clear();
        _switchSpecifications.clear();

        var checkSwitch = true;
        while (_argIndex < args.length) {
            var arg = args[_argIndex++];

            if (checkSwitch && arg.startsWith("-")) {
                if (arg.equals("--")) {
                    checkSwitch = false;
                } else {
                    processSwitch(arg);
                }
            } else {
                processPositionalArgument(arg);
            }
        }

        //  TODO check for mutual exclusion violations
        //  TODO ensure all required things are specified

        return new ProcessResult(!(_errorFlag || _fatalFlag), _messages, _switchSpecifications, _positionalSpecifications);
    }
}
