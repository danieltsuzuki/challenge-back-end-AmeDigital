package danieltsuzuk.com.github.amedigital.services;

import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.dto.StarWarsPlanetaResponse;
import danieltsuzuk.com.github.amedigital.dto.StarWarsPlanetaResultados;
import danieltsuzuk.com.github.amedigital.entities.Planeta;
import danieltsuzuk.com.github.amedigital.exceptions.BancoDeDadosException;
import danieltsuzuk.com.github.amedigital.exceptions.PlanetaNaoEncontradoException;
import danieltsuzuk.com.github.amedigital.repositories.PlanetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class PlanetaService {

    @Autowired
    private PlanetaRepository repository;

    @Autowired
    private WebClient apiStarWars;


    @Transactional
    public PlanetaResponse criar(PlanetaRequest dto) {
        if (repository.existsByNome(dto.getNome())) {
            throw new BancoDeDadosException("Planeta ja cadastrado");
        }

        int aparicoes = 0;
        Mono<StarWarsPlanetaResponse> monoAparicoes = apiStarWars.get()
                .uri("/planets/?search={nomePlaneta}", dto.getNome())
                .retrieve()
                .bodyToMono(StarWarsPlanetaResponse.class);

        StarWarsPlanetaResponse starWarsPlanetaResponse = monoAparicoes.block();

        if (starWarsPlanetaResponse != null && !starWarsPlanetaResponse.getResults().isEmpty())
            for (StarWarsPlanetaResultados planeta : starWarsPlanetaResponse.results) {
                if (planeta.name.equalsIgnoreCase(dto.getNome()))
                    aparicoes = planeta.getFilms().size();
            }

        Planeta planeta = repository.save(dto.criarPlanetaComAparicoes(aparicoes));
        return new PlanetaResponse(planeta);
    }

    public PlanetaResponse buscarPorId(Long id) {
        Optional<Planeta> planeta = Optional.ofNullable(repository.findById(id).orElseThrow(
                () -> new PlanetaNaoEncontradoException("Planeta nao enconntrado")
        ));
        return new PlanetaResponse(planeta.get());
    }

    public PlanetaResponse buscarPorNome(String nome) {
        Optional<Planeta> planeta = Optional.ofNullable(repository.findByNome(nome).orElseThrow(
                () -> new PlanetaNaoEncontradoException("Planeta nao enconntrado")
        ));
        return new PlanetaResponse(planeta.get());
    }

}
