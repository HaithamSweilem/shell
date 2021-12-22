package haitham.io.commands.basic;

import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;

import java.util.*;

public abstract class AbstractCommand implements IDoable, IUndoable {

    /** The actual command line arguments passed to this command upon execution. */
    protected final List<String> args = new ArrayList<>();
    /** The list of arguments which can be used with this command. These are useful for printing the usage and for validation. */
    protected final List<String> options = new ArrayList<>();
    /** The command name used to execute this command in a shell. */
    protected String commandName;
    /** A map of error codes and their descriptions. */
    protected final Map<Integer, String> errorsCodes = new HashMap<>();

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getUsage() {
        StringBuilder usage = new StringBuilder("Usage " + commandName);

        for (String option : options) {
            usage.append(option).append(" ");
        }

        return usage.toString();
    }

    public void printUsage() {
        System.out.println(getUsage());
    }

    /**
     * <p>
     * Validate if required arguments are specified correctly.
     * </p>
     * <p>
     * The return value is 0 when validation is successful.
     * </p>
     * <p>
     * If validation fails, an error code is returned, for which the error description
     * can be obtained by calling @{@code getErrorDescription()}
     * </p>
     *
     * @return 0 if valid, otherwise a non-zero value indicating the error code.
     */
    public int validateArgs() {
        return 0;
    }

    public void setErrorCodes(Map<Integer, String> codes) {
        this.errorsCodes.putAll(codes);
    }

    /**
     * Executes the command with the given list of arguments.
     * <p>
     *     The arguments list could be null.
     * </p>
     * <p>
     *     Return 0 to indicate success, otherwise return an agreed-upon-non-zero number.
     * </p>
     *
     * @param arguments arguments for the command
     * @return execution status of this command, 0 for success.
     */
    @Override
    public abstract int doIt(Shell shell, List<String> arguments, Environment environment);

    public int doIt(Shell shell, Environment environment) {
        return doIt(shell, null, environment);
    }

    /**
     * Undoes the command if possible, returning 0 for successful undoing.
     *
     * @return execution status of this command, 0 for success.
     */
    @Override
    public abstract int undoIt();

    public String getErrorDescription(int code) {
        return errorsCodes.get(code);
    }

    /**
     * Gets the name of the subclass which extends this abstract class.
     * Useful for the toString() method.
     *
     */
    public abstract String getClassName();

    @Override
    public String toString() {
        final String className = Optional.of(getClassName()).orElse(AbstractCommand.class.getSimpleName());
        return className + " {" +
                "args = " + args +
                ", options = " + options +
                ", commandName = '" + commandName + '\'' +
                ", errorsCodes = " + errorsCodes +
                '}';
    }
}
