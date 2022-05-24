package com.trouph.bot.configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_FLOAT_AS_INT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_INVALID_SUBTYPE;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SorareBotConfiguration {

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        // Update the standard Jackson converter with the provided ObjectMapper.
        // This leaves the rest of the standard converters in-place.
        restTemplate.getMessageConverters().forEach(c -> {
            if (c instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) c).setObjectMapper(objectMapper);
            }
        });

        return restTemplate;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder
                // We want a JSON mapper here
                .json()
                // Find all the standard Jackson modules included on the classpath. Note that this
                // should take care of things like Guava etc
                .findModulesViaServiceLoader(true)
                // Set features that we do and don't want enabled
                .featuresToEnable(
                        // Enums must be unmarshalled by name
                        FAIL_ON_NUMBERS_FOR_ENUMS,
                        // No duplicate property names in the JSON - indicates a bug
                        FAIL_ON_READING_DUP_TREE_KEY,
                        // Accept case insensitive enums - rainbow serializes enums to lower case
                        ACCEPT_CASE_INSENSITIVE_ENUMS)
                .featuresToDisable(
                        // Don't round floating point values that should be integers
                        ACCEPT_FLOAT_AS_INT,
                        // Otherwise time stamps are written as integer arrays of components
                        WRITE_DATES_AS_TIMESTAMPS,
                        // Otherwise we won't be backward compatible when reading JSON created by newer code
                        FAIL_ON_UNKNOWN_PROPERTIES,
                        // Similar to FAIL_ON_UNKNOWN_PROPERTIES, if we're disabling that, then we should
                        // disable this in order to read JSON with classes we're unaware of.
                        FAIL_ON_INVALID_SUBTYPE)
                .serializationInclusion(
                        // If values are missing (either null, or Optional.empty()), then don't include them
                        // in the output. This helps with backward/forward compatibility (whereby new optional
                        // response params that are null will not be included in the response anyway).
                        JsonInclude.Include.NON_ABSENT)
                .build();
    }



}
