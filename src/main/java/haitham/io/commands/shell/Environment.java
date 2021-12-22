package haitham.io.commands.shell;

import haitham.io.commands.basic.AbstractCommand;
import haitham.io.commands.basic.InstalledCommands;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    private final Map<String, String> envVars = new HashMap<>();
    /** The list of installed commands in this environment. */
    protected final InstalledCommands installedCommands = new InstalledCommands();

    public void setEnvVar(String key, String value) {
        envVars.put(key, value);
    }

    public String getEnvVar(String key) {
        return Optional.ofNullable(
                envVars.get(key)
        ).orElse(Optional.ofNullable(
                System.getProperty(key)
        ).orElse(""));
    }

    public AbstractCommand getInstalledCommandByName(String commandName) {
        return installedCommands.get(commandName);
    }

    public List<String> getInstalledCommands() {
        return installedCommands.getCommandNames();
    }

    public String getLastExecutionVar() {
        return getEnvVar("?");
    }

    public String getEnvVarsListing() {
        StringBuilder sb = new StringBuilder();

        envVars.forEach((k, v) -> sb.append(k).append(" = ").append(v).append("\n"));

        return sb.toString();
    }

    public void setLastExecutionVar(String value) {
        setEnvVar("?", value);
    }

    public void setShellVar(String value) {
        setEnvVar("SHELL", value);
    }

    /**
     * <p>Takes input in the form   : "User ${user.name}, version ${version}"
     *
     * <p>and produces a result like: "User user123, version 1.0.0"
     *
     * <p>This is achieved by filling the variable values from environment or Java system properties.
     *
     */
    public String fillVariablesInText(String input) {
        String regex = "\\$\\{([^}]+)}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);

        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            var varName = m.group(1);
            var varValue = getEnvVar(varName).replace("\\", "\\\\");
            m.appendReplacement(sb, varValue);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public int setCwd(String dir) {
        File f = new File(dir == null ? "." : dir);

        if (f.exists() && f.isDirectory()) {
            setEnvVar("CWD", dir);
            return 0;
        }

        // file does not exist
        return -1;
    }

    public int setCwd() {
        return setCwd(getEnvVar("user.dir"));
    }

    public String getCwd() {
        return getEnvVar("CWD");
    }
}
