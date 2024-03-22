package org.naukma.zlagoda;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class ZlagodaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZlagodaApplication.class, args);
	}

	@Bean
	public Connection getConnection(@Value("${spring.datasource.driver-class-name}") String driverClassName,
									@Value("${spring.datasource.url}") String url,
									@Value("${spring.datasource.username}") String user,
									@Value("${spring.datasource.password}") String password) {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Can't find SQL Driver", e);
		}
		Connection connection;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		Flyway flyway = Flyway.configure()
				.dataSource(url, user, password)
				.load();
		flyway.migrate();
		return connection;
	}
}
