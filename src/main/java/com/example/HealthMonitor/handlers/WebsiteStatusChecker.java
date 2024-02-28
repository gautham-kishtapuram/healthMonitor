package com.example.HealthMonitor.handlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
@Component
public class WebsiteStatusChecker implements CommandLineRunner {

	private void execute() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Enter the URL of the website (e.g., example.com): ");
			String websiteUrl = scanner.nextLine();

			try {
				if (!websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")) {
					websiteUrl = "https://" + websiteUrl;
				}

				URL url = new URL(websiteUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				int responseCode = connection.getResponseCode();
				System.out.println("Response Code: " + responseCode);

				if (responseCode == HttpURLConnection.HTTP_OK) {
					System.out.println("Server is up and running. No error on the server.");
				} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					System.out.println("404 Error: Page not found");
					handleErrorResponse(connection);
				} else if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST
						&& responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
					System.out.println("Client error (400 series): The request is malformed or invalid.");
					handleErrorResponse(connection);
				} else if (responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR
						&& responseCode < HttpURLConnection.HTTP_VERSION) {
					System.out.println("Server error (500 series): There is an issue on the server side.");
					handleErrorResponse(connection);
				} else {
					System.out.println("Unexpected response code: " + responseCode);
				}

				connection.disconnect();
			} catch (IOException e) {
				System.out.println("Error: " + e.getMessage());
			}

			System.out.println("Do you want to check another website? (yes/no): ");
			String input = scanner.nextLine().toLowerCase();
			if (!input.equals("yes")) {
				break;
			}
		}

		scanner.close();
	}

	static void handleErrorResponse(HttpURLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		String line;
		StringBuilder responseContent = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			responseContent.append(line);
		}
		reader.close();

		boolean hasErrorHandling = hasStylingOrScripts(responseContent.toString());
		if (hasErrorHandling) {
			System.out.println("The server is handling custom error pages.");
		} else {
			System.out.println("The server is not handling custom error pages.");
		}
	}

	static boolean hasStylingOrScripts(String responseBody) {
		Document doc = Jsoup.parse(responseBody);
		Element head = doc.head();

		if (head != null && head.select("style").first() != null) {
			return true;
		}

		// Check for <script> elements
		Elements scriptElements = doc.getElementsByTag("script");
		if (!scriptElements.isEmpty()) {
			Elements cssLinks = doc.select("link[href$=.css]");
			Elements jsScripts = doc.select("script[src$=.js]");

			if (!cssLinks.isEmpty() || !jsScripts.isEmpty()) {
				return true;
			}
			return true;
		}

		// Check for <link> elements with rel="stylesheet"
		Elements linkElements = doc.select("link[rel=stylesheet]");
		return !linkElements.isEmpty();
	}

	@Override
	public void run(String... args) throws Exception {
		execute();

	}
}
