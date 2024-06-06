package danieltsuzuk.com.github.amedigital.Utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiExterna {

    @Bean
    public WebClient apiStarWars(WebClient.Builder builder) {
        return builder.baseUrl("https://swapi.dev/api/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
