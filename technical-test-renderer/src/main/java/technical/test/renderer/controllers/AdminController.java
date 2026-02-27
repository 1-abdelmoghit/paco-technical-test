package technical.test.renderer.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import technical.test.renderer.facades.FlightFacade;
import technical.test.renderer.viewmodels.AirportViewModel;
import technical.test.renderer.viewmodels.FlightViewModel;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FlightFacade flightFacade;

    @ModelAttribute("flight")
    public FlightViewModel flightModel() {
        FlightViewModel flight = new FlightViewModel();
        flight.setOrigin(new AirportViewModel());
        flight.setDestination(new AirportViewModel());
        return flight;
    }

    /**
     * Affiche la page de création de vol (route /admin/createFlight).
     *
     * @param model modèle MVC utilisé par Thymeleaf
     * @return Mono de nom de vue
     */
    @GetMapping
    public Mono<String> getCreateFlightPage(final Model model) {
        model.addAttribute("flight", new FlightViewModel());
        return Mono.just("pages/admin");
    }

    /**
     * Création de vol via route /admin/createFlight - version renvoyant un Mono.
     * L'attribut 'flight' est mis à jour dans le modèle avec le Mono retourné par la façade (si nécessaire).
     *
     * @param model modèle MVC
     * @param flightViewModel données du vol
     * @return Mono émettant une redirection
     */
    @PostMapping
    public Mono<String> createFlightMono(
            final Model model,
            @ModelAttribute FlightViewModel flightViewModel
    ) {
        Mono<FlightViewModel> createFlight = flightFacade.createFlight(flightViewModel);
        model.addAttribute("flight", createFlight);
        return Mono.just("redirect:/");
    }
}
