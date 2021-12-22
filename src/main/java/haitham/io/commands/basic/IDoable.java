package haitham.io.commands.basic;

import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.List;

public interface IDoable {
    int doIt(Shell shell, List<String> arguments, Environment environment);
}
