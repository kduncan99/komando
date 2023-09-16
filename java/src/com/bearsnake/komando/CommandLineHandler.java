// Kommando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.KommandoException;
import com.bearsnake.komando.exceptions.ParseException;
import com.bearsnake.komando.messages.Message;
import com.bearsnake.komando.messages.MessageType;
import com.bearsnake.komando.messages.PositionalArgumentMessage;
import com.bearsnake.komando.messages.SwitchMessage;
import com.bearsnake.komando.values.EmptyValue;
import com.bearsnake.komando.values.Value;

import java.util.*;

public class CommandLineHandler {

    private static class SwitchDependency {

        public final Switch _subject;
        public final Switch _dependency;

        public SwitchDependency(
            final Switch subject,
            final Switch dependency
        ) {
            _subject = subject;
            _dependency = dependency;
        }
    }

    private static class SwitchPair {

        public final Switch _switch1;
        public final Switch _switch2;

        public SwitchPair(
            final Switch switch1,
            final Switch switch2
        ) {
            _switch1 = switch1;
            _switch2 = switch2;
        }
    }

    private final List<PositionalArgument> _positionalArguments = new LinkedList<>();
    private final List<Switch> _switches = new LinkedList<>();
    private final List<SwitchDependency> _dependencies = new LinkedList<>();
    private final List<SwitchPair> _exclusions = new LinkedList<>();

    private String[] _arguments;
    private int _argIndex;
    private final List<Message> _messages = new LinkedList<>();
    private final Map<Switch, List<Value>> _switchSpecifications = new HashMap<>();
    private final List<Value> _positionalSpecifications = new LinkedList<>();

    // TODO doc this
    public CommandLineHandler addCanonicalHelpSwitch() throws KommandoException {
        var sw = new SimpleSwitch.Builder().setShortName("h")
                                           .setLongName("help")
                                           .addDescription("Displays usage for this program.")
                                           .build();
        _switches.add(sw);
        return this;
    }

    // TODO doc this
    public CommandLineHandler addCanonicalVersionSwitch() throws KommandoException {
        var sw = new SimpleSwitch.Builder().setShortName("v")
                                           .setLongName("version")
                                           .addDescription("Displays program version.")
                                           .build();
        _switches.add(sw);
        return this;
    }

    // TODO doc this
    public CommandLineHandler addDependency(
        final Switch subject,
        final Switch dependency
    ) {
        _dependencies.add(new SwitchDependency(subject, dependency));
        return this;
    }

    // TODO doc this
    public CommandLineHandler addMutualExclusion(
        final Switch switch1,
        final Switch switch2
    ) {
        _exclusions.add(new SwitchPair(switch1, switch2));
        return this;
    }

    // TODO doc this
    public CommandLineHandler addPositionalArgument(
        final PositionalArgument value
    ) {
        _positionalArguments.add(value);
        return this;
    }

    // TODO doc this
    public CommandLineHandler addSwitch(
        final Switch value
    ) {
        _switches.add(value);
        return this;
    }

    /**
     * Displays usage information based on the configured arguments and switches.
     * @param programName name of the program
     */
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
              .append(posArg.isRequired() ? "" : "[")
              .append("{")
              .append(posArg.getValueName())
              .append("}").append(posArg.isRequired() ? "" : "]");
        }
        System.err.println(sb);

        if (!_switches.isEmpty()) {
            System.err.println();
            System.err.println("switches:");
            for (var sw : _switches) {
                sb = new StringBuilder();
                sb.append("  ").append(sw.toString());

                if (sw instanceof ArgumentSwitch as) {
                    sb.append(" {").append(as.getValueName()).append("}");
                    if (as.isMultiple()) {
                        sb.append(",...");
                    }

                    sb.append("  ");
                    sb.append(as.isRequired() ? "(required)" : "(optional)");
                }
                System.err.println(sb);

                for (var desc : sw._description) {
                    System.err.printf("    %s\n", desc);
                }
            }
        }

        for (var posArg : _positionalArguments) {
            System.err.println();
            System.err.printf("%s (%s)\n", posArg.getValueName(), posArg.getValueType().name().toLowerCase());
            for (var desc : posArg.getDescription()) {
                System.err.printf("  %s\n", desc);
            }
        }
    }

    // TODO doc this
    public Result processCommandLine(
        final String[] args
    ) {
        _arguments = args;
        _argIndex = 0;
        _messages.clear();
        _positionalSpecifications.clear();
        _switchSpecifications.clear();

        var checkSwitch = true;
        while (_argIndex < _arguments.length) {
            var arg = _arguments[_argIndex++];

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

        // check for required switches
        for (var swch : _switches) {
            if (swch.isRequired() && !_switchSpecifications.containsKey(swch)) {
                _messages.add(new SwitchMessage(MessageType.ERROR, swch, "Required but not specified"));
            }
        }

        // check for dependencies
        for (var dep : _dependencies) {
            if (_switchSpecifications.containsKey(dep._subject) && !_switchSpecifications.containsKey(dep._dependency)) {
                var msg = String.format("Requires unspecified switch %s", dep._dependency);
                _messages.add(new SwitchMessage(MessageType.ERROR, dep._subject, msg));
            }
        }

        // check for exclusions
        for (var ex : _exclusions) {
            if (_switchSpecifications.containsKey(ex._switch1) && _switchSpecifications.containsKey(ex._switch2)) {
                var msg = String.format("May not be specified with switch %s", ex._switch2.toString());
                _messages.add(new SwitchMessage(MessageType.ERROR, ex._switch1, msg));
            }
        }

        // check for required positional arguments
        int px = 0;
        for (var posArg : _positionalArguments) {
            if (posArg.isRequired() && (px >= _positionalSpecifications.size())) {
                _messages.add(new PositionalArgumentMessage(MessageType.ERROR, posArg, "Required but not specified"));
            }
            px++;
        }

        return new Result(_messages, _switchSpecifications, _positionalSpecifications);
    }

    private void processArgumentSwitch(
        final String[] argTokens,
        final ArgumentSwitch swch
    ) {
        String rawValues;
        if (argTokens.length == 2) {
            // the value(s) is/are given within the argument token
            rawValues = argTokens[1];
        } else {
            // the value(s) is/are given in the following argument token
            if (_argIndex == _arguments.length) {
                _messages.add(new SwitchMessage(MessageType.ERROR, swch, "No value specified for switch"));
                return;
            }
            rawValues = _arguments[_argIndex++];
        }

        // TODO we need a special version of split which honors single- and double-quote delimiters
        var subTokens = rawValues.split(",");
        if (!swch.isMultiple()) {
            if (_switchSpecifications.containsKey(swch) || (subTokens.length > 1)) {
                _messages.add(new SwitchMessage(MessageType.ERROR, swch, "Multiple values specified for singly-valued switch"));
                return;
            }
        }

        List<Value> values = new LinkedList<>();
        for (var token : subTokens) {
            try {
                values.add(Value.parseText(token, swch.getValueType()));
            } catch (ParseException ex) {
                _messages.add(new SwitchMessage(MessageType.ERROR, swch, ex.getMessage()));
                return;
            }
        }

        if (!_switchSpecifications.containsKey(swch)) {
            _switchSpecifications.put(swch, values);
        } else {
            _switchSpecifications.get(swch).addAll(values);
        }
    }

    private void processPositionalArgument(
        final String argText
    ) {
        if (_positionalSpecifications.size() == _positionalArguments.size()) {
            _messages.add(new Message(MessageType.WARNING, "Extraneous arguments are ignored"));
            return;
        }

        var arg = _positionalArguments.get(_positionalSpecifications.size());
        try {
            var value = Value.parseText(argText, arg.getValueType());
            _positionalSpecifications.add(value);
        } catch (ParseException ex) {
            _messages.add(new PositionalArgumentMessage(MessageType.ERROR, arg, ex.getMessage()));
            _positionalSpecifications.add(new EmptyValue());
        }
    }

    private void processSwitch(
        final String arg
    ) {
        // TODO we need a special version of split which honors single- and double-quote delimiters
        var tokens = arg.split("=");

        Switch swch = null;
        if (tokens[0].startsWith("--")) {
            var chop = tokens[0].substring(2);
            for (var sw : _switches) {
                if (chop.equals(sw._longName)) {
                    swch = sw;
                    break;
                }
            }
        } else {
            var chop = tokens[0].substring(1);
            for (var sw : _switches) {
                if (chop.equals(sw._shortName)) {
                    swch = sw;
                    break;
                }
            }
        }

        if (swch == null) {
            _messages.add(new Message(MessageType.WARNING, "Unrecognized Switch:" + arg));
        } else if (swch instanceof ArgumentSwitch asw) {
            processArgumentSwitch(tokens, asw);
        } else {
            if (_switchSpecifications.containsKey(swch)) {
                _messages.add(new SwitchMessage(MessageType.WARNING, swch, "Specified more than once"));
            } else {
                _switchSpecifications.put(swch, null);
            }
        }
    }
}
