// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.kommando;

import com.bearsnake.kommando.exceptions.KommandoException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandLineHandler {

    public static class Result {
        public final boolean _isSuccessful;
        public final String[] _messages;
        public final Map<String, Object> _dictionary;

        public Result(
            final boolean isSuccessful,
            final List<String> messages,
            final Map<String, Object> dictionary
        ) {
            _isSuccessful = isSuccessful;
            _messages = messages.toArray(new String[0]);
            _dictionary = dictionary;
        }
    }

    private final List<PositionalArgument> _positionalArguments = new LinkedList<>();
    private final List<Switch> _switches = new LinkedList<>();
    private final List<MutualExclusion> _mutualExclusions = new LinkedList<>();

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

    public Result processCommandLine(
        final String[] args
    ) {
        var isSuccessful = true;
        var dictionary = new HashMap<String, Object>();
        var messages = new LinkedList<String>();

        var ax = 0;
        var posArgX = 0;
        while (ax < args.length) {
            if (args[ax].startsWith("--")) {
                // TODO look for switch long name
            } else if (args[ax].startsWith("-")) {
                // TODO look for switch short name
            } else {
                //  TODO this is a positional arg
                if (posArgX == _positionalArguments.size()) {
                    var msg = String.format("Error at '%s': Too many positional arguments", args[ax]);
                    messages.add(msg);
                    ax++;
                } else {
                    //  TODO
                }
            }
        }

        return new Result(isSuccessful, messages, dictionary);
    }
}
