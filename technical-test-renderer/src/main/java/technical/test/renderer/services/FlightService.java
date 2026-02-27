package technical.test.renderer.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import technical.test.renderer.clients.TechnicalApiClient;
import technical.test.renderer.viewmodels.FlightViewModel;

@Service
public class FlightService {
    private final TechnicalApiClient technicalApiClient;

    public FlightService(TechnicalApiClient technicalApiClient) {
        this.technicalApiClient = technicalApiClient;
    }

    public Flux<FlightViewModel> getFlights(final String location, final String origin, final String destination, final String sort, final String order, final Integer page, final Integer size) {
        return this.technicalApiClient.getFlights(location, origin, destination, sort, order, page, size);
    }

    public reactor.core.publisher.Mono<FlightViewModel> createFlight(FlightViewModel flight) {
        return this.technicalApiClient.createFlight(flight);
    }
}
