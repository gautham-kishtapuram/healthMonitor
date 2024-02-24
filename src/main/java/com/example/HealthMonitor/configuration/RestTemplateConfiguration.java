package com.example.HealthMonitor.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfiguration {
	@Bean
	public RestTemplate restTemplate(ResponseErrorHandler responseErrorHandler) {
		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getMessageConverters().add(customJackson2HttpMessageConverter());
		restTemplate.setErrorHandler(responseErrorHandler);
		return restTemplate;
	}

	@Bean
	public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter() {
			public boolean canRead(java.lang.Class<?> clazz, org.springframework.http.MediaType mediaType) {
				return true;
			}
			public boolean canRead(java.lang.reflect.Type type, java.lang.Class<?> contextClass,
					org.springframework.http.MediaType mediaType) {
				return true;
			}
			protected boolean canRead(org.springframework.http.MediaType mediaType) {
				return true;
			}
		};
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));


		ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module())
				.registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		converter.setObjectMapper(objectMapper);
		return converter;
	}
}
