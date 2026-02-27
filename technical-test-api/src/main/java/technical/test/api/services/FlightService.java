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
     * @param sortBy
     * @param order
     * @param page
     * @param size
     * @return Flux<FlightRecord>
     */
    public Flux<FlightRecord> getFlights(
            String sortBy,
            String order,
            int page,
            int size
    ) {
        String sortField = switch (sortBy == null ? "" : sortBy.trim().toLowerCase()) {
            case "origin" -> "origin";
            case "destination" -> "destination";
            default -> "price";
        };

        Sort.Direction direction =
                "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.max(1, size),
                Sort.by(direction, sortField)
        );

        return flightRepository.getFlights(pageable);
    }

    /**
     * Recherche un vol par identifiant.
     *
     * @param id UUID du vol
     * @return Mono émettant le record si trouvé
     */
    public Mono<FlightRecord> findById(final UUID id) {
        return flightRepository.findById(id);
    }
}
