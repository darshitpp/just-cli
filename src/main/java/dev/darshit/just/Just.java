package dev.darshit.just;

import dev.darshit.just.request.ShortenOptions;
import dev.darshit.just.request.ShortenRequest;
import dev.darshit.just.request.ShortenResponse;
import dev.darshit.just.utils.JsonUtils;
import dev.darshit.just.utils.StringUtils;
import dev.darshit.just.utils.Validator;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import static picocli.CommandLine.Help;
import static picocli.CommandLine.Model;
import static picocli.CommandLine.ParameterException;
import static picocli.CommandLine.ParseResult;
import static picocli.CommandLine.Spec;

@Command(name = "just", mixinStandardHelpOptions = true,
        version = "0.0.1",
        description = "Shortens a URL")
class Just implements Callable<String> {


    private final String shortenerHost = System.getenv("URL_SHORTENER_HOST");

    @Parameters(index = "0", arity = "1",
            description = "URL to shorten")
    private String url;

    @Option(names = {"-s", "--strategy"}, arity = "1",
            description = "Strategy to use for shortening, e.g. hash, word, custom, wordHash", defaultValue = "word")
    private String strategy;

    @Option(names = {"-d", "--domain"}, arity = "1",
            description = "Domain name for the URL, e.g. https://domain.com", defaultValue = "https://just.darshit.dev")
    private String domain;

    @Option(names = {"-p", "--customPath"}, arity = "1",
            description = "Custom Path of URL when using custom strategy")
    private String customPath;

    @Option(names = {"-us", "--urlSize"}, arity = "1",
            description = "Size of the identifier when using hash strategy", defaultValue = "8")
    private int urlSize;

    @Option(names = {"-ttl", "--ttl"}, arity = "1",
            description = "Expiry time of the Short URL", defaultValue = "7")
    private int ttlInDays;

    @Option(names = {"-l", "--liberalHash"}, arity = "1",
            description = "Use Liberal Hash characters", defaultValue = "false")
    private boolean liberalHash;

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    @Spec
    Model.CommandSpec spec;

    public static void main(String... args) {

        Help.Ansi ansi = Help.Ansi.AUTO;
        int exitCode = new CommandLine(new Just())
                .setColorScheme(Help.defaultColorScheme(ansi))
                .execute(args);

        System.exit(exitCode);
    }

    private void validateArgs() {
        String url = spec.positionalParameters().get(0).getValue();
        if (!Validator.validateUrl(url)) {
            throw new ParameterException(spec.commandLine(), String.format("'%s' is not a valid URL", url));
        }

        ParseResult parseResult = spec.commandLine().getParseResult();

        List<String> strategies = List.of("word", "hash", "custom", "wordHashCombo");
        if (!strategies.contains(strategy)) {
            throw new ParameterException(spec.commandLine(), String.format("Invalid value '%s' for strategy '--strategy': " +
                    "Valid strategies: word, hash, custom, wordHashCombo", strategy));
        }

        if ("custom".equals(strategy)) {
            String customPath = spec.findOption("customPath").getValue();
            if (StringUtils.isEmpty(customPath)) {
                throw new ParameterException(spec.commandLine(), String.format("%s strategy should have a -c or --customPath option", strategy));
            }
        } else if ("hash".equals(strategy)) {
            if (parseResult.hasMatchedOption(spec.findOption("urlSize"))) {
                Integer urlSize = parseResult.matchedOptionValue("urlSize", 8);
                if (urlSize < 5 || urlSize > 18) {
                    throw new ParameterException(spec.commandLine(), String.format("Invalid value '%s' for urlSize '--urlSize', it should be between 5 and 18 ", urlSize));
                }
            }
        }

        String domain = spec.findOption("domain").getValue();
        if (!Validator.validateUrl(domain)) {
            throw new ParameterException(spec.commandLine(), String.format("Invalid value '%s' for domain '--domain' ", domain));
        }

        if (parseResult.hasMatchedOption(spec.findOption("ttl"))) {
            Integer ttl = parseResult.matchedOptionValue("ttl", 7);
            if (ttl < 1 || ttl > 30) {
                throw new ParameterException(spec.commandLine(), String.format("Invalid value '%s' for ttl '--ttl', it should be between 1 and 30 days ", ttl));
            }
        }

    }



    @Override
    public String call() throws Exception {
        validateArgs();
        ShortenRequest shortenRequest = new ShortenRequest(url, strategy, buildShortenOptions());

        String host = !StringUtils.isEmpty(shortenerHost) ? shortenerHost : "https://just.darshit.dev";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(host + "/shorten"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtils.json(shortenRequest), StandardCharsets.UTF_8))
                .build();

        CompletableFuture<ShortenResponse> responseFuture = httpClient
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(res -> JsonUtils.value(res, ShortenResponse.class));

        ShortenResponse shortenResponse = responseFuture.get();
        String response = generateCliResponse(shortenResponse);
        System.out.println(response);
        return response;
    }

    private String generateCliResponse(ShortenResponse shortenResponse) {
        String response = null;
        if (shortenResponse != null) {
            String shortUrl = shortenResponse.getShortUrl();
            int ttl = shortenResponse.getTtlInDays();
            String error = shortenResponse.getError();

            if (!StringUtils.isEmpty(shortUrl)) {
                response = "Short \uD83D\uDD17: " + shortUrl + " will expire in " + ttl + " days.";
            } else if (!StringUtils.isEmpty(error)) {
                response = error;
            } else {
                response = "Some error occurred. Please try after some time. \n\nContact me @darshitpp on Twitter if the issue is persistent";
            }
        }

        response = response + "\n\n" + "Check out \uD83D\uDD0E https://github.com/darshitpp/url-shortener for more info";
        return response;
    }

    private ShortenOptions buildShortenOptions() {
        ShortenOptions.Builder options = new ShortenOptions.Builder();
        if ("hash".equals(strategy)) {
            if (!StringUtils.isEmpty(customPath)) {
                options.withCustomPath(customPath);
            }
            if (liberalHash) {
                options.withLiberalHash(true);
            }
            options.withPathSize(urlSize);
        } else if ("custom".equals(strategy)) {
            options.withCustomPath(customPath);
        }
        options.withDomain(domain);
        options.withTtlInDays(ttlInDays);
        return options.build();
    }
}