package haitham.io.commands.shell;

import java.util.Arrays;

public class CommandOption {
    protected String name;
    protected String value;
    protected String delimiter = "=";
    protected String prefix = "-";

    public CommandOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getFullOptionValue() {
        return name + delimiter + value;
    }

    public static void main(String[] args) {
        String[] a = {"a", "ab", "abcde"};
        int sum = Arrays.stream(a)
                .filter(value -> value.matches("ab"))
                .mapToInt(String::length)
                .sum();
        System.out.println("sum: " + sum);
    }
}
