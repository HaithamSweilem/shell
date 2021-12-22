package haitham.io.commands.shell;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Shell {

    public static final int EXEC_RESULT_INVALID_COMMAND = -1;

    protected final String version = "Shell v1.0";
    protected final Scanner cin = new Scanner(System.in);
    protected final Environment environment;
    protected final History history = new History();
    protected final String shellName = "shell";
    protected final LocalDateTime tomorrow = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

    public Shell(Environment environment) {
        this.environment = environment;

        history.clear();
        environment.setLastExecutionVar("0");
        final int cwdReturnValue;
        if ((cwdReturnValue = environment.setCwd())  != 0 ) {
            throw new RuntimeException("Could not run Shell in the current directory. Error code: " + cwdReturnValue);
        }
        environment.setShellVar(shellName);
    }

    public void run() {
        System.out.println(version);
        System.out.println("Welcome! Type ? and hit enter to see available commands.");

        while (true) {

            // if it is after midnight, prevent running shell :P
            final LocalDateTime midnight = LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 0, 0);
            if (LocalDateTime.now().isAfter(midnight)) {
                System.out.println("It's after midnight! Get some sleep 😴");
                return;
            }

            System.out.print(getShellHeader());

            // read line
            String inputLine = cin.nextLine();

            // parse command from input line, execute it, and save it to history
            final int result = executeCommand(inputLine);

            // save command result in environment
            environment.setLastExecutionVar(result + "");

            if (result == -1) {
                System.out.println("Invalid command.");
            }
        }
    }

    public String getShellHeader() {
        return getLoggedInUserName() + getMachineHost()
                + " <" + getDateTime() + ">"
                + " " + getCwd()
                + " " + (isLoggedInUserASuperUser() ? "# " : "$ ");
    }

    public String getLoggedInUserName() {
        return System.getProperty("user.name");
    }

    public String getMachineHost() {
        try {
            return "@" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss"));
    }

    public String getCwd() {
//        return System.getProperty("user.dir");
        return environment.getCwd();
    }

    public boolean isLoggedInUserASuperUser() {
        return Arrays.stream(File.listRoots()).noneMatch(root -> root.isDirectory() && !root.canWrite());
    }

    public String getHistory() {
        return history.getHistoryListing();
    }

    public int redoCommand(int numberInHistory) {
        return history.redoCommand(numberInHistory, this, environment);
    }

    public int executeCommand(String line) {
        return executeCommand(line, true);
    }

    public int executeCommand(String line, boolean addToHistory) {

        if (line == null || line.trim().isEmpty()) {
            return 0;
        }

        // parse command
        final String[] args = line.split(" ");

        if (args.length > 0) {
            String commandName = args[0];
            List<String> argsList = Arrays.stream(args).skip(1).toList();

            // get installed command
            haitham.io.commands.basic.AbstractCommand command = environment.getInstalledCommandByName(commandName);

            if (command != null) {

                // if we are redoing a command, we execute the "CommandRedo" with the target command as an argument
                // so, we don't add the "CommandRedo" to the history, instead, only the target command is added
                // (via the CommandRedo.doIt() method which calls the Shell.redoCommand() method
                if (addToHistory) {
                    // save command in history with arguments
                    history.addCommand(command, argsList);
                }

                // execute command and return result
                return command.doIt(this, argsList, environment);
            }
        }

        // return -1 to indicate an error
        return EXEC_RESULT_INVALID_COMMAND;
    }
}
