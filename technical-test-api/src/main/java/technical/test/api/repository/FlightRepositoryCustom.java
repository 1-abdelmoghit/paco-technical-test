package technical.test.api.repository;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import technical.test.api.record.FlightRecord;

public interface FlightRepositoryCustom {
    Flux<FlightRecord> getFlights(final Pageable pageable);
}
