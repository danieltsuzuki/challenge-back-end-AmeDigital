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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Serviço para a entidade Planeta.
 * <p>
 * Esta classe fornece métodos para criar, buscar e listar planetas.
 * </p>
 */
@Service
public class PlanetaService {

    @Autowired
    private PlanetaRepository repository;

    @Autowired
    private WebClient apiStarWars;

    /**
     * Cria um novo planeta no banco de dados.
     * <p>
     * Este método verifica se um planeta com o mesmo nome já existe. Se existir, lança uma exceção.
     * Caso contrário, consulta a API do Star Wars para obter o número de aparições do planeta em filmes,
     * salva o novo planeta com o número de aparições e retorna a resposta do planeta criado.
     * </p>
     *
     * @param dto o DTO com os dados do planeta a ser criado
     * @return a resposta do planeta criado
     */
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

    /**
     * Busca um planeta pelo seu ID.
     * <p>
     * Este método lança uma exceção se o planeta não for encontrado.
     * </p>
     *
     * @param id o ID do planeta a ser buscado
     * @return a resposta do planeta encontrado
     */
    public PlanetaResponse buscarPorId(Long id) {
        Optional<Planeta> planeta = Optional.ofNullable(repository.findById(id).orElseThrow(
                () -> new PlanetaNaoEncontradoException("Planeta nao enconntrado")
        ));
        return new PlanetaResponse(planeta.get());
    }

    /**
     * Busca um planeta pelo seu nome.
     * <p>
     * Este método lança uma exceção se o planeta não for encontrado.
     * </p>
     *
     * @param nome o nome do planeta a ser buscado
     * @return a resposta do planeta encontrado
     */
    public PlanetaResponse buscarPorNome(String nome) {
        Optional<Planeta> planeta = Optional.ofNullable(repository.findByNome(nome).orElseThrow(
                () -> new PlanetaNaoEncontradoException("Planeta nao enconntrado")
        ));
        return new PlanetaResponse(planeta.get());
    }

    /**
     * Busca todos os planetas de acordo com a especificação e paginação fornecidas.
     *
     * @param spec     a especificação para filtrar os planetas
     * @param pageable as informações de paginação
     * @return uma página contendo as respostas dos planetas encontrados
     */
    public Page<PlanetaResponse> buscarTodos(Specification spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }
}
