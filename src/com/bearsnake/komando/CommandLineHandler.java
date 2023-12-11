// Komando project
// Copyright © 2023 by Kurt Duncan, BearSnake LLC
// All Rights Reserved

package com.bearsnake.komando;

import com.bearsnake.komando.exceptions.CommandArgumentException;
import com.bearsnake.komando.exceptions.KomandoException;
import com.bearsnake.komando.exceptions.ParseException;
import com.bearsnake.komando.messages.CommandArgumentMessage;
import com.bearsnake.komando.messages.Message;
import com.bearsnake.komando.messages.MessageType;
import com.bearsnake.komando.messages.PositionalArgumentMessage;
import com.bearsnake.komando.messages.SwitchMessage;
import com.bearsnake.komando.values.CommandValue;
import com.bearsnake.komando.values.EmptyValue;
import com.bearsnake.komando.values.StringValue;
import com.bearsnake.komando.values.Value;
import com.bearsnake.komando.values.ValueType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private CommandArgument _commandArgument = null;
    private final List<PositionalArgument> _positionalArguments = new LinkedList<>();
    private final List<Switch> _switches = new LinkedList<>();
    private final List<SwitchDependency> _dependencies = new LinkedList<>();
    private final List<SwitchPair> _exclusions = new LinkedList<>();

    private String[] _arguments;
    private int _argIndex;
    private final List<Message> _messages = new LinkedList<>();
    private final Map<Switch, List<Value>> _switchSpecifications = new HashMap<>();
    private final List<Value> _positionalSpecifications = new LinkedList<>();

    // chosenCommand will be the reference to one of the CommandValue objects passed to the CommandArgument
    // object if it was specified. It will be null if we were not given a CommandArgument.
    private CommandValue _chosenCommand = null;

    static final Switch HELP_SWITCH;
    static final Switch VERSION_SWITCH;

    static {
        try {
            HELP_SWITCH = new SimpleSwitch.Builder().setShortName("h")
                                                       .setLongName("help")
                                                       .addDescription("Displays usage for this program.")
                                                       .build();
            VERSION_SWITCH = new SimpleSwitch.Builder().setShortName("v")
                                                           .setLongName("version")
                                                           .addDescription("Displays program version.")
                                                           .build();
        } catch (KomandoException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO doc this
    public CommandLineHandler addCanonicalHelpSwitch() throws KomandoException {
        _switches.add(HELP_SWITCH);
        return this;
    }

    // TODO doc this
    public CommandLineHandler addCanonicalVersionSwitch() throws KomandoException {
        _switches.add(VERSION_SWITCH);
        return this;
    }

    // TODO doc this
    public CommandLineHandler addCommandArgument(
        final CommandArgument value
    ) {
        if (_commandArgument != null) {
            throw new CommandArgumentException();
        }

        _commandArgument = value;
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

        if (_commandArgument != null) {
            sb.append(" command");
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

                if (!sw._affinity.isEmpty()) {
                    sb.setLength(0);
                    sb.append("    Only applies to command(s): ");
                    var commandStrings = sw._affinity.stream()
                                                     .map(StringValue::getValue)
                                                     .collect(Collectors.toCollection(LinkedList::new));
                    sb.append(String.join(", ", commandStrings));
                    System.err.println(sb);
                }

                for (var desc : sw._description) {
                    System.err.printf("    %s\n", desc);
                }
            }
        }

        if (_commandArgument != null) {
            System.err.println();
            System.err.printf("command %s", _commandArgument.getChoicesString());

            for (var desc : _commandArgument.getDescription()) {
                System.err.printf("  %s\n", desc);
            }
        }

        for (var posArg : _positionalArguments) {
            System.err.println();
            System.err.printf("%s (%s)%s\n",
                              posArg.getValueName(),
                              posArg.getValueType().name().toLowerCase(),
                              posArg.hasRestriction() ? posArg.getRestriction().toString() : "");

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
            } else if ((_commandArgument != null) && (_chosenCommand == null)) {
                processCommandArgument(arg);
            } else {
                processPositionalArgument(arg);
            }
        }

        // Special checking for canonical help/version switches
        var help = _switchSpecifications.containsKey(HELP_SWITCH);
        var version = _switchSpecifications.containsKey(VERSION_SWITCH);

        // Do all the verification checking for the various switch and argument combinations,
        // but *only* if we are not doing help or version - in those cases, we ignore all the rest.
        if (!help && !version) {
            // Check the command - if we have a command argument
            if ((_commandArgument != null) && (_chosenCommand == null)) {
                _messages.add(new CommandArgumentMessage(MessageType.ERROR, "Required but not specified"));
            }

            // Ensure that all required switches were specified. This does not apply if there was a command argument,
            // and the otherwise-required switch does not have affinity with the given command.
            for (var sw : _switches) {
                if (sw.isRequired() && switchIsApplicable(sw) && !_switchSpecifications.containsKey(sw)) {
                    _messages.add(new SwitchMessage(MessageType.ERROR, sw, "Required but not specified"));
                }
            }

            if (_chosenCommand != null) {
                for (var sw : _switchSpecifications.keySet()) {
                    var msg = "Switch does not apply to '" + _chosenCommand.getValue() + "' command";
                    if (!switchIsApplicable(sw)) {
                        _messages.add(new SwitchMessage(MessageType.WARNING, sw, msg));
                    }
                }
            }

            // Check for switch dependencies.
            for (var dep : _dependencies) {
                if (switchIsApplicable(dep._subject) && switchIsApplicable(dep._dependency)) {
                    if (_switchSpecifications.containsKey(dep._subject) && !_switchSpecifications.containsKey(dep._dependency)) {
                        var msg = String.format("Requires unspecified switch %s", dep._dependency);
                        _messages.add(new SwitchMessage(MessageType.ERROR, dep._subject, msg));
                    }
                }
            }

            // Check for switch exclusions
            for (var ex : _exclusions) {
                if (switchIsApplicable(ex._switch1) && switchIsApplicable(ex._switch2)) {
                    if (_switchSpecifications.containsKey(ex._switch1) && _switchSpecifications.containsKey(ex._switch2)) {
                        var msg = String.format("May not be specified with switch %s", ex._switch2.toString());
                        _messages.add(new SwitchMessage(MessageType.ERROR, ex._switch1, msg));
                    }
                }
            }

            // Check for required positional arguments
            int px = 0;
            for (var posArg : _positionalArguments) {
                if (posArg.isRequired() && (px >= _positionalSpecifications.size())) {
                    _messages.add(new PositionalArgumentMessage(MessageType.ERROR, posArg, "Required but not specified"));
                }
                px++;
            }
        }

        return new Result(_messages, _chosenCommand, _switchSpecifications, _positionalSpecifications);
    }

    private void processArgumentSwitch(
        final String[] argTokens,
        final ArgumentSwitch argSwitch
    ) {
        String rawValues;
        if (argTokens.length == 2) {
            // the value(s) is/are given within the argument token
            rawValues = argTokens[1];
        } else {
            // the value(s) is/are given in the following argument token
            if (_argIndex == _arguments.length) {
                _messages.add(new SwitchMessage(MessageType.ERROR, argSwitch, "No value specified for switch"));
                return;
            }
            rawValues = _arguments[_argIndex++];
        }

        var subTokens = split(rawValues, ',');
        if (!argSwitch.isMultiple()) {
            if (_switchSpecifications.containsKey(argSwitch) || (subTokens.length > 1)) {
                _messages.add(new SwitchMessage(MessageType.ERROR, argSwitch, "Multiple values specified for singly-valued switch"));
                return;
            }
        }

        List<Value> values = new LinkedList<>();
        for (var token : subTokens) {
            try {
                values.add(Value.parseText(token, argSwitch.getValueType()));
            } catch (ParseException ex) {
                _messages.add(new SwitchMessage(MessageType.ERROR, argSwitch, ex.getMessage()));
                return;
            }
        }

        if (!_switchSpecifications.containsKey(argSwitch)) {
            _switchSpecifications.put(argSwitch, values);
        } else {
            _switchSpecifications.get(argSwitch).addAll(values);
        }
    }

    private void processCommandArgument(
        final String argText
    ) {
        try {
            var value = (StringValue) Value.parseText(argText, ValueType.STRING);
            _chosenCommand = _commandArgument.findCommandValueForString(value);
            if (_chosenCommand == null) {
                var msg = "'" + value + "' is not a valid command";
                _messages.add(new CommandArgumentMessage(MessageType.ERROR, msg));
            }
        } catch (KomandoException ex) {
            _messages.add(new CommandArgumentMessage(MessageType.ERROR, ex.getMessage()));
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
            arg.checkRestriction(value);
            _positionalSpecifications.add(value);
        } catch (KomandoException ex) {
            _messages.add(new PositionalArgumentMessage(MessageType.ERROR, arg, ex.getMessage()));
            _positionalSpecifications.add(new EmptyValue());
        }
    }

    private void processSwitch(
        final String arg
    ) {
        var tokens = split(arg, '=');
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

    private static String[] split(
        final String input,
        final Character delimiter
    ) {
        // This special version of String.split() will not check for delimiters inside double-quoted string segments.
        var temp = new LinkedList<String>();
        char quoteChar = 0;
        var sb = new StringBuilder();
        for (var ix = 0; ix < input.length(); ++ix) {
            var ch = input.charAt(ix);
            if (quoteChar != 0) {
                // we are inside a quoted section
                if (ch == quoteChar) {
                    // end of quoted section
                    quoteChar = 0;
                } else {
                    sb.append(ch);
                }
            } else {
                // we are *not* inside a quoted section
                if ((ch == '\'') || (ch == '"')) {
                    quoteChar = ch;
                } else {
                    if (ch == delimiter) {
                        temp.add(sb.toString());
                        sb.setLength(0);
                    } else {
                        sb.append(ch);
                    }
                }
            }
        }

        // TODO someday we should complain if we are here and still in a quoted section
        temp.add(sb.toString());
        return temp.toArray(new String[0]);
    }

    private boolean switchIsApplicable(
        final Switch sw
    ) {
        // This switch does *not* apply if there is a command argument,
        // and the switch has affinity with any commands, but not the chosen command.
        return !((_commandArgument != null) && sw.hasAffinity() && !sw.hasAffinityWith(_chosenCommand));
    }
}
