package danieltsuzuk.com.github.amedigital.services;

import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.dto.StarWarsPlanetaResponse;
import danieltsuzuk.com.github.amedigital.entities.Planeta;
import danieltsuzuk.com.github.amedigital.repositories.PlanetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PlanetaService {

    @Autowired
    private PlanetaRepository repository;

    @Autowired
    private WebClient apiStarWars;


    @Transactional
    public PlanetaResponse criar(PlanetaRequest dto) {
        int aparicoes = 0;
        Mono<StarWarsPlanetaResponse> monoAparicoes = apiStarWars.get()
                .uri("/planets/?search={nomePlaneta}", dto.getNome())
                .retrieve()
                .bodyToMono(StarWarsPlanetaResponse.class);

        StarWarsPlanetaResponse starWarsPlanetaResponse = monoAparicoes.block();

        if (starWarsPlanetaResponse != null && !starWarsPlanetaResponse.getResults().isEmpty())
            aparicoes = starWarsPlanetaResponse.getResults().get(0).getFilms().size();

        Planeta planeta = repository.save(dto.criarPlanetaComAparicoes(aparicoes));
        return new PlanetaResponse(planeta);
    }

}
