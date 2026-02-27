package technical.test.renderer.clients;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.properties.TechnicalApiProperties;
import technical.test.renderer.viewmodels.FlightViewModel;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
@Slf4j
public class TechnicalApiClient {

    private final TechnicalApiProperties technicalApiProperties;
    private final WebClient webClient;

    public TechnicalApiClient(TechnicalApiProperties technicalApiProperties, final WebClient.Builder webClientBuilder) {
        this.technicalApiProperties = technicalApiProperties;
        this.webClient = webClientBuilder.baseUrl(technicalApiProperties.getUrl()).build();
    }

    public Flux<FlightViewModel> getFlights() {
        return webClient
                .get()
                .uri(technicalApiProperties.getFlightPath())
                .retrieve()
                .bodyToFlux(FlightViewModel.class)
                .onErrorResume(err -> {
                    log.warn("Impossible de récupérer les vols depuis l'API technique: {}", err.toString());
                    return Flux.empty();
                });
    }

    public Flux<FlightViewModel> getFlights(String location, String origin, String destination,  String sort, String order, Integer page, Integer size) {

        Map<String, Object> params = getStringObjectMap(location, origin, destination, sort, order, page, size);

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(technicalApiProperties.getFlightPath());
                    params.forEach(uriBuilder::queryParam);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(FlightViewModel.class)
                .onErrorResume(err -> {
                    log.warn("Impossible de récupérer les vols (filtres) depuis l'API technique: {}", err.toString());
                    return Flux.empty();
                });
    }

    private static @NonNull Map<String, Object> getStringObjectMap(String location, String origin, String destination, String sort, String order, Integer page, Integer size) {
        Map<String, Object> params = new LinkedHashMap<>();
        if (location != null && !location.isBlank()) params.put("location", location);
        if (origin != null && !origin.isBlank())     params.put("origin", origin);
        if (destination != null && !destination.isBlank()) params.put("destination", destination);
        if (sort != null && !sort.isBlank())         params.put("sort", sort);
        if (order != null && !order.isBlank())       params.put("order", order);
        if (page != null)                            params.put("page", page);
        if (size != null)                            params.put("size", size);
        return params;
    }

    /**
     * Envoie une requête POST à l'API technique pour créer un nouveau vol avec les données fournies dans le FlightViewModel.
     * @param flightViewModel
     * @return Mono<FlightViewModel>
     */
    public Mono<FlightViewModel> createFlight(final FlightViewModel flightViewModel) {
        return webClient
                .post()
                .uri(technicalApiProperties.getFlightPath())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(flightViewModel)
                .retrieve()
                .bodyToMono(FlightViewModel.class);
    }
}
