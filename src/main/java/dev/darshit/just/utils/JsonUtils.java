package dev.darshit.just.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class JsonUtils {
	
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    /**
     * Convert a Java object to Json string.
     *
     * @param value The Java object to marshall
     * @return The Json string
     */
    public static String json(final Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (final JsonProcessingException jpe) {
            throw new SystemException("Failed to parse object to json string", jpe);
        }
    }

    /**
     * Convert a Json String to Java object.
     *
     * @param json The Json string to unmarshall
     * @return The unmarshalled java object
     */
    public static <T> T value(final String json, final Class<T> valueType) {
        try {
            return MAPPER.readValue(json, valueType);
        } catch (final IOException ioe) {
            throw new SystemException("Failed to parse json value to object", ioe);
        }
    }

    /**
     * Convert a Json String to Java object.
     *
     * @param json          The Json string
     * @param typeReference The type reference of the java object required to be parsed
     * @return The unmarshalled java object
     */
    public static <T> T value(final String json, final TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (final IOException ioe) {
            throw new SystemException("Failed to parse json value to object", ioe);
        }
    }

    /**
     * Convert Java object to object defined in TypeReference.
     *
     * @param value         The Java Object
     * @param typeReference The type reference of the object to unmarshall to
     * @param <T>           The type of request Object
     * @param <R>           The type of response Object
     * @return converted java object of type R
     */
    public static <T, R> R convertValue(final T value, final TypeReference<R> typeReference) {
        return MAPPER.convertValue(value, typeReference);
    }

    /**
     * Convert Java object to object defined in TypeReference.
     *
     * @param value         The Java Object
     * @param valueType     class type of object to unmarshall to
     * @param <T>           The type of request Object
     * @param <R>           The type of response Object
     * @return converted java object of type R
     */
    public static <T, R> R convertValue(final T value, final Class<R> valueType) {
        return MAPPER.convertValue(value, valueType);
    }

}

class SystemException extends RuntimeException {
    
    private static final long serialVersionUID = -2606893225597468841L;
    
    /**
     *
     * @param message
     * @param cause
     */
    public SystemException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     *
     * @param message
     */
    public SystemException(final String message) {
        super(message);
    }
}