package technical.test.api.repository;

import reactor.core.publisher.Flux;
import technical.test.api.record.FlightRecord;

public interface FlightRepositoryCustom {
    Flux<FlightRecord> getFlights(String sortBy, String order, int page, int size);
}
