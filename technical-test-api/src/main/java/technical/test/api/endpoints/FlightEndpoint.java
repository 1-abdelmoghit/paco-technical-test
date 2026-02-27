package technical.test.api.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.representation.FlightRepresentation;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightEndpoint {
    private final FlightFacade flightFacade;
    /**
     *
     * Récupération de la liste des vols
     * @RequestParam : on peut trier les résultats par prix ou durée, dans l’ordre croissant ou décroissant, et paginer les résultats.
     * @return Flux<FlightRepresentation>
     */
    @GetMapping
    public Flux<FlightRepresentation> getFlights(
            @RequestParam(value = "origin", required = false) final String origin,
            @RequestParam(value = "destination", required = false) final String destination,
            @RequestParam(value = "sort", required = false, defaultValue = "price") final String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") final String order,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "6") final Integer size
    ) {
        return flightFacade.getFlights(origin, destination, sort, order, page, size);
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
