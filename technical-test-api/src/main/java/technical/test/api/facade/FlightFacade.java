package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

@Component
@RequiredArgsConstructor
public class FlightFacade {
    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;
    /**
     * Récupération de la liste des vols : on récupère les records, puis on les convertit en représentation.
     * La conversion doit inclure la résolution des aéroports d’origine et de destination pour enrichir la représentation.
     */
    public Flux<FlightRepresentation> getFlights(final String sortBy, final String order, final int page, final int size) {
        return flightService.getFlights( sortBy, order, page, size)
                .flatMap(this::toRepresentation);
    }

    /**
     * Création d’un vol : la génération de l’ID doit être gérée par le service ou la couche repo.
     * Ici on se contente de convertir la représentation en record.
     */
    public Mono<FlightRepresentation> createFlight(final FlightRepresentation representation) {
        return Mono.fromSupplier(() -> flightMapper.convert(representation))
                .flatMap(flightService::saveFlight)
                .flatMap(this::toRepresentation);
    }

    private Mono<FlightRepresentation> toRepresentation(final FlightRecord flightRecord) {
        return Mono.zip(
                        airportService.findByIataCode(flightRecord.getOrigin()),
                        airportService.findByIataCode(flightRecord.getDestination())
                )
                .map(tuple -> {
                    final var rep = flightMapper.convert(flightRecord);
                    rep.setOrigin(airportMapper.convert(tuple.getT1()));
                    rep.setDestination(airportMapper.convert(tuple.getT2()));
                    return rep;
                });
    }
}
