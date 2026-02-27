package technical.test.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    /**
     * Enregistre un enregistrement de vol dans la base de données.
     *
     * @param flightRecord The flight record to save.
     * @return A Mono emitting the saved flight record.
     */
    public Mono<FlightRecord> saveFlight(final FlightRecord flightRecord) {

        flightRecord.setId(UUID.randomUUID());

        return flightRepository.save(flightRecord);
    }

    /**
     * Récupère une liste paginée d’enregistrements de vols depuis la base de données, triée selon le champ et l’ordre spécifiés.
     *
     * @param origin
     * @param destination
     * @param sortBy
     * @param order
     * @param page
     * @param size
     * @return Flux<FlightRecord>
     */
    public Flux<FlightRecord> getFlights(final String origin, final String destination, final String sortBy, final String order, final int page, final int size) {
        final String normalizedSort = (sortBy == null) ? "price" : sortBy.trim().toLowerCase();
        final String normalizedOrder = (order == null) ? "asc" : order.trim().toLowerCase();
        final int safePage = Math.max(0, page);
        final int safeSize = Math.max(1, size);

        String sortProperty = "price";
        if ("origin".equals(normalizedSort)) {
            sortProperty = "origin";
        } else if ("destination".equals(normalizedSort)) {
            sortProperty = "destination";
        }

        Sort.Direction direction = "desc".equals(normalizedOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;
        final Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(direction, sortProperty));

        return flightRepository.getFlights(origin, destination, pageable);
    }
}
