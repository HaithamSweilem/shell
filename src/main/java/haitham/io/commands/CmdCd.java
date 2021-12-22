package haitham.io.commands;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.io.File;
import java.util.List;

public class CmdCd extends AbstractCommand {

    public CmdCd() {
        super("cd");
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

        // no args given, or a dot (.) is given, stay in current dir
        if (args.size() == 0 || args.get(0).equals(".")) {
            return 0;
        }

        // TODO: handle upper directory ".."
        //

        String dirName = String.join(" ", args);
        int result = environment.setCwd(dirName);
        if (result != 0) {

            // try again by prefixing dirName with CWD
            dirName = environment.getCwd() + File.separatorChar + dirName;
            result = environment.setCwd(dirName);
            if (result != 0) {
                System.err.println("Directory does not exist: " + dirName);
            }
        }

        return 0;
    }

    @Override
    public int undoIt() {
        return 0;
    }

    @Override
    public String getClassName() {
        return CmdCd.class.getSimpleName();
    }
}
