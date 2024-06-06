package danieltsuzuk.com.github.amedigital.controllers;

import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.services.PlanetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/planetas")
public class PlanetaController {

    @Autowired
    private PlanetaService service;

    @PostMapping
    public ResponseEntity<PlanetaResponse> criar(@Valid @RequestBody PlanetaRequest request) {
        PlanetaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
