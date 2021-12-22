package haitham.io.commands.shell;

import haitham.io.commands.basic.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class History {
    private final List<String> history;

    public History() {
        history = new ArrayList<>();
    }

    public void addCommand(AbstractCommand command, List<String> argsList) {
        history.add(command.getCommandName() + (argsList.size() > 0 ? " " + String.join(" " , argsList) : ""));
    }

    public int redoCommand(int number, Shell shell, Environment environment) {
        if (number > 0 && number < history.size()) {
            final String commandExecutionLine = history.get(number - 1);
            return shell.executeCommand(commandExecutionLine, false);
        }

        return 2;
    }

    public void clear() {
        history.clear();
    }

    public String getHistoryListing() {
        final int indexLength = ("" + history.size()).length();
        return IntStream.range(0, history.size())
                .mapToObj(i -> String.format("%" + indexLength + "d %s\n", i + 1, history.get(i)))
                .collect(Collectors.joining());
    }
}
