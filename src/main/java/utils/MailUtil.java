package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;


public class MailUtil {
	public static void sendGmail(String to, String from, String subject, String body, boolean isBodyHtml) throws MessagingException {
		// 1 - Lấy API key từ biến môi trường (không hard-code vào code)
		String apiKey = System.getenv("RESEND_API_KEY");
		if (apiKey == null || apiKey.isBlank()) {
			throw new MessagingException("Thiếu biến môi trường RESEND_API_KEY");
		}

		// 2 - Tạo nội dung JSON (Resend dùng "html" hoặc "text" tùy loại body)
		String bodyField = isBodyHtml ? "html" : "text";
		String json = "{" + "\"from\":\"" + escapeJson(from) + "\"," + "\"to\":[\"" + escapeJson(to) + "\"]," + "\"subject\":\"" + escapeJson(subject)
				+ "\"," + "\"" + bodyField + "\":\"" + escapeJson(body) + "\"" + "}";

		// 3 - Tạo HTTP request gửi tới Resend
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.resend.com/emails")).header("Authorization", "Bearer " + apiKey)
				.header("Content-Type", "application/json").header("User-Agent", "web-programming-app/1.0").POST(HttpRequest.BodyPublishers.ofString(
						json)).build();

		// 4 - Gửi request, lỗi thì ném MessagingException như cũ
		try {
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			if (status < 200 || status >= 300) {
				throw new MessagingException("Resend API lỗi " + status + ": " + response.body());
			}
		} catch (IOException e) {
			throw new MessagingException("Không kết nối được tới Resend API", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new MessagingException("Gửi mail bị gián đoạn", e);
		}
	}

	// Escape ký tự đặc biệt để chuỗi hợp lệ trong JSON (đặc biệt là \n trong body)
	private static String escapeJson(String text) {
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			switch (c) {
				case '\\':
					result.append("\\\\");
					break;
				case '"':
					result.append("\\\"");
					break;
				case '\n':
					result.append("\\n");
					break;
				case '\r':
					result.append("\\r");
					break;
				case '\t':
					result.append("\\t");
					break;
				default:
					if (c < 0x20)
						result.append(String.format("\\u%04x", (int)c));
					else result.append(c);
			}
		}
		return result.toString();
	}
}
