package dev.darshit.just.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.darshit.just.utils.StringUtils;
import dev.darshit.just.utils.Validator;

import java.util.Objects;

@JsonDeserialize(builder = ShortenOptions.Builder.class)
public class ShortenOptions {

    private String customPath;

    private int pathSize;

    private int ttlInDays;

    private boolean liberalHash;

    private String domain;

    public String getCustomPath() {
        return customPath;
    }

    public int getPathSize() {
        return pathSize;
    }

    public int getTtlInDays() {
        return ttlInDays;
    }

    public boolean isLiberalHash() {
        return liberalHash;
    }

    public String getDomain() {
        return domain;
    }

    public static final class Builder {

        @JsonProperty("customPath")
        private String customPath;

        @JsonProperty("urlSize")
        private int pathSize;

        @JsonProperty("ttl")
        private int ttlInDays;

        @JsonProperty("liberalHash")
        private boolean liberalHash;

        @JsonProperty("domain")
        private String domain;

        public Builder() {
            this.pathSize = 8;
            this.ttlInDays = 7;
            this.liberalHash = false;
        }

        public Builder withCustomPath(String customPath) throws IllegalArgumentException {
            if (StringUtils.isEmpty(customPath)) {
                throw new IllegalArgumentException("CustomPath cannot be empty");
            } else if (!Validator.validateCustomPath(customPath)) {
                throw new IllegalArgumentException("CustomPath cannot contain special characters except - and _");
            }
            this.customPath = customPath;
            return this;
        }


        public Builder withPathSize(int pathSize) throws IllegalArgumentException {
            if (pathSize < 5 || pathSize > 18) {
                throw new IllegalArgumentException("Size must be between 5 and 18 characters");
            }
            this.pathSize = pathSize;
            return this;
        }

        public Builder withTtlInDays(int ttl) throws IllegalArgumentException {
            if (ttl < 1 || ttl > 30) {
                throw new IllegalArgumentException("TTL must be between 1 and 30 days");
            }
            this.ttlInDays = ttl;
            return this;
        }

        public Builder withLiberalHash(boolean liberalHash) {
            this.liberalHash = liberalHash;
            return this;
        }

        public Builder withDomain(String domain) {
            if (!Validator.validateUrl(domain)) {
                throw new IllegalArgumentException("Please pass a valid domain starting with http/https");
            }
            this.domain = domain;
            return this;
        }

        public ShortenOptions build() {
            ShortenOptions shortenOptions = new ShortenOptions();
            shortenOptions.customPath = getCustomPath();
            shortenOptions.pathSize = getPathSize();
            shortenOptions.ttlInDays = getTtlInDays();
            shortenOptions.liberalHash = isLiberalHash();
            shortenOptions.domain = getDomain();
            return shortenOptions;
        }

        public String getCustomPath() {
            return customPath;
        }

        public int getPathSize() {
            return pathSize;
        }

        public int getTtlInDays() {
            return ttlInDays;
        }

        public boolean isLiberalHash() {
            return liberalHash;
        }

        public String getDomain() {
            return domain;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortenOptions)) return false;
        ShortenOptions that = (ShortenOptions) o;
        return pathSize == that.pathSize && ttlInDays == that.ttlInDays && liberalHash == that.liberalHash && Objects.equals(customPath, that.customPath) && Objects.equals(domain, that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customPath, pathSize, ttlInDays, liberalHash, domain);
    }

    @Override
    public String toString() {
        return "ShortenOptions{" +
                "customPath='" + customPath + '\'' +
                ", pathSize=" + pathSize +
                ", ttlInDays=" + ttlInDays +
                ", liberalHash=" + liberalHash +
                ", domain='" + domain + '\'' +
                '}';
    }
}
