package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.List;

public class CmdSet extends AbstractCommand {

    public CmdSet() {
        super("set");
    }

    @Override
    public int validateArgs() {
        return 0;
    }

    @Override
    public int doIt(Shell shell, List<String> commandLineArguments, Environment environment) {

        args.clear();
        if (commandLineArguments != null) {
            args.addAll(commandLineArguments);
        }

        // expected two arguments
        if (args.size() < 2) {
            System.err.println("Invalid number of arguments. Usage syntax is: set NAME VALUE");
            return 1;
        }

        if (!args.get(0).matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
            System.err.println("Invalid characters in variable name, only letters, number and underscore are allowed.");
            return 2;
        }

        environment.setEnvVar(args.get(0), args.get(1));

        System.out.println(args.get(0) + " = " + environment.getEnvVar(args.get(0)));

        return 0;
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdSet.class.getSimpleName();
    }
}
