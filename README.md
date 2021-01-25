# Java URL Shortener CLI (JUST-CLI)

## A simple CLI wrapper for shortening URLs through [JUST](https://github.com/darshitpp/url-shortener)

Made with picocli - https://picocli.info/


## Usage

Supports all the parameters supported by [JUST](https://github.com/darshitpp/url-shortener) application

### Native Image
```bash
> ./just -hV

just [-hV] [-d=<domain>] [-l=<liberalHash>] [-p=<customPath>]
            [-s=<strategy>] [-ttl=<ttlInDays>] [-us=<urlSize>] <url>
Shortens a URL
      <url>               URL to shorten
  -d, --domain=<domain>   Domain name for the URL, e.g. https://domain.com
  -h, --help              Show this help message and exit.
  -l, --liberalHash=<liberalHash>
                          Use Liberal Hash characters
  -p, --customPath=<customPath>
                          Custom Path of URL when using custom strategy
  -s, --strategy=<strategy>
                          Strategy to use for shortening, e.g. hash, word,
                            custom, wordHash
  -ttl, --ttl=<ttlInDays>
                          Expiry time of the Short URL
  -us, --urlSize=<urlSize>
                          Size of the identifier when using hash strategy
  
  -V, --version           Print version information and exit.
```

Recommend usage is using the native-image mentioned in the releases, though for smaller footprint for the application, Java 11 is required.

### JAR

Requirements: Java 11
```bash
> java -jar just-<version>.jar -hV
```

## Building from Source

Requirements:
1. Java 11 for JAR
2. (Optional) GraalVM 21.0.0 - Java 11 with `native-image` installed to generate an independent program. (Check https://picocli.info/#_graalvm_native_image for more info)

Use [SDKMAN!](https://sdkman.io/) for installing a JDK easily

Use: `mvn clean install` to build the application. However, in case you use a Non-GraalVM vendor for JDK, the `native-image-maven-plugin` will fail (but the JAR will still be created)