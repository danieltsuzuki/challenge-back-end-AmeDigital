package danieltsuzuk.com.github.amedigital.controllers;

import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.entities.Planeta;
import danieltsuzuk.com.github.amedigital.specifications.PlanetaSpecification;
import danieltsuzuk.com.github.amedigital.services.PlanetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para a entidade Planeta.
 * <p>
 * Este controlador fornece endpoints para criar, buscar e listar planetas.
 * </p>
 */
@RestController
@RequestMapping("/planetas")
public class PlanetaController {

    @Autowired
    private PlanetaService service;

    /**
     * Endpoint para criar um novo planeta.
     * <p>
     * Recebe um objeto PlanetaRequest no corpo da requisição e retorna um objeto PlanetaResponse.
     * </p>
     *
     * @param request o DTO com os dados do planeta a ser criado
     * @return uma resposta com o planeta criado e o status HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<PlanetaResponse> criar(@Valid @RequestBody PlanetaRequest request) {
        PlanetaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para buscar um planeta pelo seu ID ou nome.
     * <p>
     * O valor da variável de caminho pode ser um ID ou um nome. O método tenta converter a variável para Long.
     * Se a conversão falhar, considera-se que a variável é um nome.
     * </p>
     *
     * @param variavel o ID ou nome do planeta a ser buscado
     * @return uma resposta com o planeta encontrado e o status HTTP 200 (OK)
     */
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

    /**
     * Endpoint para buscar todos os planetas com paginação e filtragem.
     * <p>
     * Permite a filtragem por ID e/ou nome do planeta, e suporta paginação.
     * </p>
     *
     * @param pageable os parâmetros de paginação
     * @param id o ID do planeta a ser filtrado (opcional)
     * @param nome o nome do planeta a ser filtrado (opcional)
     * @return uma resposta com uma página de planetas encontrados e o status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<Page<PlanetaResponse>> buscarTodos(Pageable pageable, @RequestParam(required = false) Long id, @RequestParam(required = false) String nome) {
        Specification<Planeta> spec = Specification.where(PlanetaSpecification.likeNome(nome))
                .and(PlanetaSpecification.hasId(id));
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarTodos(spec, pageable));
    }


}
