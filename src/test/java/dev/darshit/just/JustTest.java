package dev.darshit.just;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JustTest {

    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void test_invalid_longUrl() {
        String[] args = "abc".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("'abc' is not a valid URL"));
    }

    @Test
    public void test_incorrect_strategy() {
        String[] args = "https://google.com --strategy=abc".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value 'abc' for strategy '--strategy': Valid strategies: word, hash, custom, wordHashCombo"));
    }

    @Test
    public void test_invalid_domain() {
        String[] args = "https://google.com --domain=abc".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value 'abc' for domain '--domain' "));
    }

    @Test
    public void test_invalid_custom_path() {
        String[] args = "https://google.com --strategy=custom --customPath=".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("custom strategy should have a -c or --customPath option"));
    }

    @Test
    public void test_invalid_urlSize_with_hash_strategy_greater__than_18() {
        String[] args = "https://google.com --strategy=hash --urlSize=31".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value '31' for urlSize '--urlSize', it should be between 5 and 18 "));
    }

    @Test
    public void test_invalid_urlSize_with_hash_strategy_less_than_5() {
        String[] args = "https://google.com --strategy=hash --urlSize=4".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value '4' for urlSize '--urlSize', it should be between 5 and 18 "));
    }

    @Test
    public void test_invalid_ttl_greater_than_30() {
        String[] args = "https://google.com --ttl=33".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value '33' for ttl '--ttl', it should be between 1 and 30 days "));
    }

    @Test
    public void test_invalid_ttl_less_than_1() {
        String[] args = "https://google.com --ttl=-1".split(" ");
        new CommandLine(new Just()).execute(args);
        assertTrue(err.toString().contains("Invalid value '-1' for ttl '--ttl', it should be between 1 and 30 days "));
    }


}