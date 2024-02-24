package com.example.HealthMonitor.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class HealthErrorResponseHandler implements ResponseErrorHandler {
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		log.error("FAILED : while processing the request - {} ", httpResponse.getStatusText());
	}
}