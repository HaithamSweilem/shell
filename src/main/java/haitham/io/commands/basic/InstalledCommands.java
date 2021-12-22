package haitham.io.commands.basic;


import haitham.io.commands.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstalledCommands {

    private final Map<String, AbstractCommand> commands = new HashMap<>();

    public InstalledCommands() {
        commands.put("?", new CmdHelp());
        commands.put("echo", new CmdEcho());
        commands.put("exit", new CmdExit());
        commands.put("history", new CmdHistory());
        commands.put("!", new CmdRedo());
        commands.put("set", new CmdSet());
        commands.put("env", new CmdEnv());
        commands.put("cd", new CmdCd());
        commands.put("pwd", new CmdPwd());
        commands.put("ls", new CmdLs());
    }

    public AbstractCommand get(String commandName) {
        return commands.get(commandName);
    }

    public List<String> getCommandNames() {
        return commands.keySet().stream().sorted().toList();
    }
}
