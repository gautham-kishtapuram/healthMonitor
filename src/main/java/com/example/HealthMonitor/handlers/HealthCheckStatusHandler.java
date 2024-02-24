package com.example.HealthMonitor.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HealthCheckStatusHandler implements CommandLineRunner {

	private final @NonNull RestTemplate restTemplate;

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				log.info("Please enter a URL (or type 'exit' to quit): ");
				String baseUrl = scanner.nextLine();
				if ("exit".equalsIgnoreCase(baseUrl.trim())) {
					break;
				}
//				I have commented the below post method, because it may need to be changed for some websites.
				/*log.info("Please enter the HTTP method (GET or POST): ");
				String httpMethod = scanner.nextLine().toUpperCase();*/

				URI validURL = validateURL(baseUrl);
				if (validURL != null) {
//					HttpMethod method = HttpMethod.valueOf(httpMethod);
					checkHealth(validURL, HttpMethod.GET);
				}
			} catch (Exception e) {
				log.error("ERROR while processing the request - {}", e.getMessage());
			}

		}
		scanner.close();
	}

	void checkHealth(URI uri, HttpMethod method) {
		try {
			if (method == HttpMethod.GET) {
				ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
				handleResponse(uri, responseEntity);
			} else if (method == HttpMethod.POST) {
				/*Object response = WebClient.builder()
						.baseUrl(uri.toString())
						.exchangeStrategies(ExchangeStrategies.builder()
								.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
								.build()) // Adjust as needed
						.build()
						.post()
						.accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
						.retrieve()
						.onStatus(HttpStatusCode::is4xxClientError,
								error -> error.bodyToMono(String.class).flatMap(errorBody -> {
									System.out.println("4xx Client Error: " + errorBody);
									return Mono.error(new RuntimeException("4xx Client Error"));
								}))

						.bodyToFlux(Object.class)
						.blockLast();*/
				ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri, null, Object.class);
				log.info(responseEntity);
				handleResponse(uri, responseEntity);
			} else {
				log.error("Unsupported HTTP method.");
			}
		} catch (Exception e) {
			log.error("Error while checking health: {}", e.getMessage());
		}
	}
	/*private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
		clientCodecConfigurer.customCodecs().encoder(new Jackson2JsonEncoder(new ObjectMapper(), MediaType.TEXT_HTML));
		clientCodecConfigurer.customCodecs().decoder(new Jackson2JsonDecoder(new ObjectMapper(), MediaType.TEXT_HTML));
	}*/

	void handleResponse(URI uri, ResponseEntity<?> responseEntity) {
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode.is2xxSuccessful()) {
			log.info("SUCCESS : the server {} is up and running", uri.getHost());
		} else if (statusCode.is4xxClientError()) {
			log.warn("CLIENT ERROR: The server {} returned a client error (4xx)", uri.getHost());
		} else if (statusCode.is5xxServerError()) {
			log.warn("SERVER ERROR: The server {} returned a server error (5xx)", uri.getHost());
		}
	}

	private URI validateURL(String baseUrl) {
		String baseURL = baseUrl.trim();
		URI uri = null;
		try {
			uri = new URI(addHttpsIfNeeded(baseURL));
		} catch (URISyntaxException e) {
			log.error("FAILURE : URL is not in the correct format, Reason {}", e.getMessage());
		}
		return uri;
	}

	private String addHttpsIfNeeded(String baseUrl) {
		if (!baseUrl.startsWith("http")) {
			baseUrl = "https://" + baseUrl;
		}
		return baseUrl;
	}
}



