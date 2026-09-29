package utils;

import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBUtil {
	private static final EntityManagerFactory emf = createEntityManagerFactory();

	private static EntityManagerFactory createEntityManagerFactory() {
		Map<String, String> dbOverrides = new HashMap<>();

		String dbUrl = System.getenv("DB_URL");
		if (dbUrl != null && !dbUrl.isBlank()) {
			dbOverrides.put("jakarta.persistence.jdbc.url", dbUrl);
			dbOverrides.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
			dbOverrides.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
		}

		return Persistence.createEntityManagerFactory("MyPostgresPU", dbOverrides);
	}

	public static EntityManagerFactory getEmFactory() {
		return emf;
	}
}
