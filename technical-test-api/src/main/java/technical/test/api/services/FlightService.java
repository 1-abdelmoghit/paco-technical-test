package technical.test.api.services;

import lombok.RequiredArgsConstructor;
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
     * @param sortBy
     * @param order
     * @param page
     * @param size
     * @return Flux<FlightRecord>
     */
    public Flux<FlightRecord> getFlights(String sortBy, String order, int page, int size) {
        return flightRepository.getFlights(sortBy, order, page, size);
    }
}
