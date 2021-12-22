package haitham.io.commands;

import haitham.io.commands.shell.Environment;
import haitham.io.commands.shell.Shell;
import junit.framework.TestCase;

import java.util.Collections;
import java.util.List;

public class CmdSetTest extends TestCase {

    public void testCmdSetWithNoArgsShouldReturn1() {
        CmdSet cmd = new CmdSet();
        Environment env = new Environment();
        Shell shell = new Shell(env);

        final int expected = 1;
        final int actualNull = cmd.doIt(shell, null, env);
        final int actualEmpty = cmd.doIt(shell, Collections.emptyList(), env);

        assertEquals(expected, actualEmpty);
        assertEquals(expected, actualNull);
    }

    public void testCmdSetWithNotEnoughArgsShouldReturn1() {
        CmdSet cmd = new CmdSet();
        Environment env = new Environment();
        Shell shell = new Shell(env);
        List<String> args = List.of("key");

        final int expected = 1;
        final int actualNotEnoughArgs = cmd.doIt(shell, args, env);

        assertEquals(expected, actualNotEnoughArgs);
    }

    public void testCmdSetWithArgNameIsNotAlphaNumericShouldReturn2() {
        CmdSet cmd = new CmdSet();
        Environment env = new Environment();
        Shell shell = new Shell(env);

        assertEquals(2, cmd.doIt(shell, List.of("3453", "value"), env));
    }

    public void testCmdSetNormalFunctionalityShouldReturn0() {
        CmdSet cmd = new CmdSet();
        Environment env = new Environment();
        Shell shell = new Shell(env);
        final String key = "key_1";
        final String val1 = "val1";
        final String val2 = "val2";

        assertEquals(0, cmd.doIt(shell, List.of(key, val1), env));
        assertEquals(val1, env.getEnvVar(key));

        assertEquals(0, cmd.doIt(shell, List.of(key, val2), env));
        assertEquals(val2, env.getEnvVar(key));
    }

}
