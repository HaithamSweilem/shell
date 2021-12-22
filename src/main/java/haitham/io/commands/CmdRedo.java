package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.List;
import java.util.Optional;

public class CmdRedo extends AbstractCommand {

    public CmdRedo() {
        super("!");
    }

    @Override
    public int validateArgs() {
        return 0;
    }

    @Override
    public int doIt(Shell shell, List<String> commandLineArguments, Environment environment) {
        if (commandLineArguments.size() != 1) {
            System.err.println("Invalid arguments.\nUsage: ! <command-number-from-history>");
            return 1;
        }

        int commandNumberInHistory = Optional.ofNullable(
                Integer.getInteger(commandLineArguments.get(0))
            ).orElse(-1);

        return shell.redoCommand(commandNumberInHistory);
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdRedo.class.getSimpleName();
    }
}
