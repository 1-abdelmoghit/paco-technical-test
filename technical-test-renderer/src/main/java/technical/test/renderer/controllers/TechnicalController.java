package technical.test.renderer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import technical.test.renderer.facades.FlightFacade;
import technical.test.renderer.viewmodels.FlightViewModel;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TechnicalController {

    private final FlightFacade flightFacade;

    @GetMapping
    public Mono<String> getFlightPage(final Model model,
                                                        @RequestParam(value = "location", required = false) final String location,
                                                        @RequestParam(value = "origin", required = false) final String origin,
                                                        @RequestParam(value = "destination", required = false) final String destination,
                                                        @RequestParam(value = "sort", required = false, defaultValue = "price") final String sort,
                                                        @RequestParam(value = "order", required = false, defaultValue = "asc") final String order,
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "6") final Integer size) {
        model.addAttribute("flights", this.flightFacade.getFlights(location, origin, destination, sort, order, page, size));
        model.addAttribute("location", location);
        model.addAttribute("origin", origin);
        model.addAttribute("destination", destination);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return Mono.just("pages/index");
    }

    @GetMapping("/createFlight")
    public Mono<String> getCreateFlightPage(final Model model) {
        model.addAttribute("flight", new FlightViewModel());
        return Mono.just("pages/createFlight");
    }

    @PostMapping("/createFlight")
    public Mono<String> createFlight(@ModelAttribute FlightViewModel flightViewModel) {
        return flightFacade.createFlight(flightViewModel)
                .thenReturn("redirect:/");
    }
}
