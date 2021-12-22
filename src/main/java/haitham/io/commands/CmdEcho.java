package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.List;

public class CmdEcho extends AbstractCommand {

    public CmdEcho() {
        super("echo");
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
        String output = (args.size() == 0) ? "" : String.join(" ", args);

        System.out.println(environment.fillVariablesInText(output));

        return 0;
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdEcho.class.getSimpleName();
    }
}
