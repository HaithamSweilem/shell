package haitham.io;

import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

public class Main {
    public static void main(String[] args) {
        Environment environment = new Environment();
        Shell bash = new Shell(environment);
        bash.run();
    }
}
