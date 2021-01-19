package dev.darshit.just.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ShortenResponse {

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("error")
    private String error;

    @JsonProperty("ttl")
    private Integer ttlInDays;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getTtlInDays() {
        return ttlInDays;
    }

    public ShortenResponse() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortenResponse)) return false;
        ShortenResponse that = (ShortenResponse) o;
        return Objects.equals(shortUrl, that.shortUrl) &&
                Objects.equals(error, that.error) &&
                Objects.equals(ttlInDays, that.ttlInDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortUrl, error, ttlInDays);
    }

    @Override
    public String toString() {
        return "ShortenResponse{" +
                "shortUrl='" + shortUrl + '\'' +
                ", error='" + error + '\'' +
                ", ttlInDays=" + ttlInDays +
                '}';
    }
}
