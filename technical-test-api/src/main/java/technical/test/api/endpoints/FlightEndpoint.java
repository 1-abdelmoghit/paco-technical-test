package technical.test.api.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.representation.FlightRepresentation;

import java.util.UUID;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightEndpoint {
    private final FlightFacade flightFacade;
    /**
     *
     * Récupération de la liste des vols tri et pagination
     * @RequestParam : on peut trier les résultats par prix ou durée, dans l’ordre croissant ou décroissant, et paginer les résultats.
     * @return Flux<FlightRepresentation>
     */
    @GetMapping

    public Flux<FlightRepresentation> getFlights(
            @RequestParam(defaultValue = "price") String sort,
            @RequestParam(defaultValue = "asc")   String order,
            @RequestParam(defaultValue = "0")     int page,
            @RequestParam(defaultValue = "6")     int size
    ) {
        return flightFacade.getFlights(sort, order, page, size);
    }


    /**
     * Récupération du détail d'un vol par son identifiant
     * @param id UUID du vol
     * @return Mono<FlightRepresentation>
     */
    @GetMapping("/{id}")
    public Mono<FlightRepresentation> getFlightById(@PathVariable final UUID id) {
        return flightFacade.getFlightById(id);
    }

    /**
     *
     * Création d’un vol
     * @RequestBody : on attend une représentation du vol à créer, sans ID (géré par le service).
     * @return Mono<FlightRepresentation>
     */
    @PostMapping
    public Mono<FlightRepresentation> createFlight(@RequestBody final FlightRepresentation flightRepresentation) {
        return flightFacade.createFlight(flightRepresentation);
    }
}
