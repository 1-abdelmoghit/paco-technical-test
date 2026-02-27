package technical.test.renderer.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.clients.TechnicalApiClient;
import technical.test.renderer.viewmodels.FlightViewModel;

@Service
public class FlightService {
    private final TechnicalApiClient technicalApiClient;

    public FlightService(TechnicalApiClient technicalApiClient) {
        this.technicalApiClient = technicalApiClient;
    }

    public Flux<FlightViewModel> getFlights(final String sort, final String order, final Integer page, final Integer size) {
        return this.technicalApiClient.getFlights(sort,order,page, size);
    }

    public Mono<FlightViewModel> getFlightById(final java.util.UUID id) {
        return this.technicalApiClient.getFlightById(id);
    }

    public reactor.core.publisher.Mono<FlightViewModel> createFlight(FlightViewModel flight) {
        return this.technicalApiClient.createFlight(flight);
    }
}
