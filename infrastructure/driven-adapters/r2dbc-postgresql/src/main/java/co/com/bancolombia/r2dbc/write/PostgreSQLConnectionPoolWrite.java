package co.com.bancolombia.r2dbc.write;

import co.com.bancolombia.r2dbc.config.PostgresqlConnectionProperties;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;

@Configuration
@EnableR2dbcRepositories(basePackages = "co.com.bancolombia.r2dbc.write", entityOperationsRef = "postgresR2dbcEntityWriteOperations")
public class PostgreSQLConnectionPoolWrite {
    public static final int MAX_IDLE_TIME = 30;

	@Bean("writePostgresOperations")
    public ConnectionPool getConnectionPool(
            PostgresqlConnectionProperties properties,
            @Value("${adapters.db.schema}") String schema,
            @Value("${adapters.db.initial-size}") int initialSize,
            @Value("${adapters.db.max-size}") int maxSize
    ) {
		PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                .host(properties.host())
                .port(properties.port())
                .schema(schema)
                .database(properties.dbname())
                .username(properties.username())
                .password(properties.password())
                .sslMode(SSLMode.REQUIRE)
                .build();

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                .name("api-postgres-connection-pool")
                .initialSize(initialSize)
                .maxSize(maxSize)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .build();

		return new ConnectionPool(poolConfiguration);
	}


    @Bean
    public R2dbcEntityOperations postgresR2dbcEntityWriteOperations(
            @Qualifier("writePostgresOperations") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }
}