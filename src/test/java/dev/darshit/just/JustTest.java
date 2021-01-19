package dev.darshit.just;

import dev.darshit.just.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class JustTest {

    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @BeforeEach // JUnit 5
    public void setUpStreams() {
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach // JUnit 5
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test_incorrect_strategy() {
        String[] args = "https://google.com --strategy=abc".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_domain() {
        String[] args = "https://google.com --domain=abc".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_custom_path() {
        String[] args = "https://google.com --strategy=custom --customPath=".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_urlSize_with_hash_strategy_greater__than_18() {
        String[] args = "https://google.com --strategy=hash --urlSize=31".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_urlSize_with_hash_strategy_less_than_5() {
        String[] args = "https://google.com --strategy=hash --urlSize=4".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_ttl_greater_than_30() {
        String[] args = "https://google.com --ttl=33".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }

    @Test
    public void test_invalid_ttl_less_than_1() {
        String[] args = "https://google.com --ttl=-1".split(" ");
        new CommandLine(new Just()).execute(args);
        assertFalse(StringUtils.isEmpty(err.toString()));
    }


}