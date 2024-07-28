package danieltsuzuk.com.github.amedigital.controllers;

import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.services.PlanetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{variavel}")
    public ResponseEntity<PlanetaResponse> buscarPorIdOuNome(@PathVariable(required = false) String variavel) {
        Long id = null;
        String nome = null;
        try {
            id = Long.parseLong(variavel);
        } catch (RuntimeException e) {
            nome = variavel;
        }

        if (id != null) {
            PlanetaResponse response = service.buscarPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else  {
            PlanetaResponse response = service.buscarPorNome(nome);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }


}
