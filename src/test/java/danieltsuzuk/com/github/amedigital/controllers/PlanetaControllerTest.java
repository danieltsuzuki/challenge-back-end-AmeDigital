package danieltsuzuk.com.github.amedigital.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import danieltsuzuk.com.github.amedigital.dto.ErroResponse;
import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.dto.PlanetaResponse;
import danieltsuzuk.com.github.amedigital.entities.Planeta;
import danieltsuzuk.com.github.amedigital.exceptions.BancoDeDadosException;
import danieltsuzuk.com.github.amedigital.exceptions.PlanetaNaoEncontradoException;
import danieltsuzuk.com.github.amedigital.services.PlanetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para o controlador de Planetas.
 * <p>
 * Esta classe testa os endpoints da classe PlanetaController utilizando o MockMvc para simular requisições HTTP.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PlanetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetaService service;

    private PlanetaRequest request200;
    private PlanetaRequest request400;
    private Page<PlanetaResponse> page;
    private Planeta planeta;

    /**
     * Configuração inicial para os testes.
     * <p>
     * Inicializa os objetos PlanetaRequest, Planeta e PageImpl para os testes.
     * </p>
     */
    @BeforeEach
    public void configuracao() {
        request200 = new PlanetaRequest("Tatooine", "seco", "deserto");
        request400 = new PlanetaRequest("", "", "");
        planeta = new Planeta(1L, "Tatooine", "seco", "deserto", 5);
        page = new PageImpl<>(List.of(new PlanetaResponse(planeta)));
    }

    /**
     * Testa a criação de um planeta com sucesso.
     * <p>
     * Verifica se o código de status HTTP 201 (Created) é retornado ao criar um planeta com dados válidos.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
public void deveRetornarCodigo201QuandoCriarPlanetaComSucesso() throws Exception {
        when(service.criar(any())).thenReturn(new PlanetaResponse(planeta));

        MvcResult response = mockMvc.perform(post("/planetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        Planeta planeta = new ObjectMapper().readValue(responseBody, Planeta.class);

        assertNotNull(planeta.getId());
        assertEquals(planeta.getNome(), "Tatooine");
        assertEquals(planeta.getTerreno(), "deserto");
        assertEquals(planeta.getClima(), "seco");
        assertEquals(planeta.getAparicoes(), 5);
    }

    /**
     * Testa a criação de um planeta com argumentos inválidos.
     * <p>
     * Verifica se o código de status HTTP 400 (Bad Request) é retornado ao tentar criar um planeta com dados inválidos.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo400QuandoEnviarArgumentosInvalidos() throws Exception {
        when(service.criar(any())).thenThrow(new RuntimeException("asd"));

        MvcResult response = mockMvc.perform(post("/planetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request400.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        ErroResponse responseErro = new ObjectMapper().readValue(responseBody, ErroResponse.class);

        assertNotNull(responseErro);
        assertFalse(responseErro.getErrors().isEmpty());
    }

    /**
     * Testa a criação de um planeta já existente.
     * <p>
     * Verifica se o código de status HTTP 400 (Bad Request) é retornado ao tentar criar um planeta com nome já existente.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo400QuandoPlanetaJaExistir() throws Exception {
        when(service.criar(any())).thenThrow( new BancoDeDadosException("Planeta ja cadastrado"));

        MvcResult response = mockMvc.perform(post("/planetas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        ErroResponse responseErro = new ObjectMapper().readValue(responseBody, ErroResponse.class);

        assertNotNull(responseErro);
        assertFalse(responseErro.getErrors().isEmpty());
        assertEquals(responseErro.getErrors().get(0), "Planeta ja cadastrado");
    }

    /**
     * Testa a busca de um planeta por ID com sucesso.
     * <p>
     * Verifica se o código de status HTTP 200 (OK) é retornado ao buscar um planeta existente pelo seu ID.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo200QuandoPlanetaForEncontradoPorId() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(new PlanetaResponse(planeta));

        MvcResult response = mockMvc.perform(get("/planetas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        Planeta planeta = new ObjectMapper().readValue(responseBody, Planeta.class);

        assertNotNull(planeta.getId());
        assertEquals(planeta.getNome(), "Tatooine");
        assertEquals(planeta.getTerreno(), "deserto");
        assertEquals(planeta.getClima(), "seco");
        assertEquals(planeta.getAparicoes(), 5);
    }

    /**
     * Testa a busca de um planeta por ID não encontrado.
     * <p>
     * Verifica se o código de status HTTP 404 (Not Found) é retornado ao buscar um planeta que não existe pelo seu ID.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo404QuandoPlanetaNaoForEncontradoPorId() throws Exception {
        when(service.buscarPorId(1L)).thenThrow( new PlanetaNaoEncontradoException("Planeta nao encontrado"));

        mockMvc.perform(get("/planetas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * Testa a busca de um planeta por nome com sucesso.
     * <p>
     * Verifica se o código de status HTTP 200 (OK) é retornado ao buscar um planeta existente pelo seu nome.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo200QuandoPlanetaForEncontradoPorNome() throws Exception {
        when(service.buscarPorNome(request200.getNome())).thenReturn(new PlanetaResponse(planeta));

        MvcResult response = mockMvc.perform(get("/planetas/{nome}", request200.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        PlanetaResponse planeta = new ObjectMapper().readValue(responseBody, PlanetaResponse.class);

        assertNotNull(planeta.getId());
        assertEquals(planeta.getNome(), "Tatooine");
        assertEquals(planeta.getTerreno(), "deserto");
        assertEquals(planeta.getClima(), "seco");
        assertEquals(planeta.getAparicoes(), 5);
    }

    /**
     * Testa a busca de um planeta por nome não encontrado.
     * <p>
     * Verifica se o código de status HTTP 404 (Not Found) é retornado ao buscar um planeta que não existe pelo seu nome.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante o teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo404QuandoPlanetaNaoForEncontradoPorNome() throws Exception {
        when(service.buscarPorNome(request200.getNome())).thenThrow(new PlanetaNaoEncontradoException("Planeta nao encontrado"));

        mockMvc.perform(get("/planetas/{nome}", request200.getNome())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * Testa o endpoint de busca de planetas.
     * <p>
     * Este teste verifica se a chamada ao endpoint de listagem de planetas retorna um código de status HTTP 200 (OK)
     * juntamente com a lista de planetas. O método utiliza um mock do serviço para simular o comportamento esperado.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante a execução do teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo200ComAListaDePlanetas() throws Exception {
        when(service.buscarTodos(any(Specification.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/planetas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * Testa o endpoint de exclusão de planeta.
     * <p>
     * Este teste verifica se a chamada ao endpoint de exclusão de um planeta pelo seu ID retorna um código de status HTTP 204 (No Content).
     * O método utiliza um mock do serviço para simular a exclusão bem-sucedida do planeta.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante a execução do teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo204AoDeletarPlaneta() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/planetas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Testa o endpoint de exclusão de planeta quando o planeta não é encontrado.
     * <p>
     * Este teste verifica se a chamada ao endpoint de exclusão de um planeta pelo seu ID retorna um código de status HTTP 404 (Not Found)
     * quando o planeta não é encontrado. O método utiliza um mock do serviço para simular a situação de não encontrar o planeta.
     * </p>
     *
     * @throws Exception se ocorrer algum erro durante a execução do teste
     */
    @Test
    @Transactional
    public void deveRetornarCodigo404AoTentarDeletarPlaneta() throws Exception {
        doThrow(new PlanetaNaoEncontradoException("Planeta nao encontrado")).when(service).deletar(1L);

        mockMvc.perform(delete("/planetas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}