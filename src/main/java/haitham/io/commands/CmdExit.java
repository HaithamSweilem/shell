package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.List;

public class CmdExit extends AbstractCommand {

    public CmdExit() {
        super("exit");
    }

    @Override
    public int validateArgs() {
        return 0;
    }

    @Override
    public int doIt(Shell shell, List<String> commandLineArguments, Environment environment) {
        System.exit(0);
        return 0;
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdExit.class.getSimpleName();
    }
}
