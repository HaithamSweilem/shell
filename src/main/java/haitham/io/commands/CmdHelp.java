package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;
import haitham.io.util.StringAlignUtil;

import java.util.List;

public class CmdHelp extends AbstractCommand {

    public CmdHelp() {
        super("?");
    }

    @Override
    public int validateArgs() {
        return 0;
    }

    @Override
    public int doIt(Shell shell, List<String> commandLineArguments, Environment environment) {

        // if a command name is given as the first argument to the 'help' command,
        // then, print the usage of that command
        if (commandLineArguments.size() > 0) {
            final String commandName = commandLineArguments.get(0);
            final AbstractCommand command = environment.getInstalledCommandByName(commandName);
            if (command == null) {
                System.err.println("Command not found '" + commandName + "'");
                return -1;
            }

            command.printUsage();
            return 0;
        }

        // otherwise, print the list of available commands
        System.out.println("The following commands are available, separated by spaces:");
        System.out.println(new StringAlignUtil().alignCenter(String.join(" ", environment.getInstalledCommands()), 15));

        return 0;
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdHelp.class.getSimpleName();
    }
}
