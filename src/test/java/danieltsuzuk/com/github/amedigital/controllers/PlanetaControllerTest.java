package danieltsuzuk.com.github.amedigital.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import danieltsuzuk.com.github.amedigital.dto.ErroResponse;
import danieltsuzuk.com.github.amedigital.dto.PlanetaRequest;
import danieltsuzuk.com.github.amedigital.entities.Planeta;
import danieltsuzuk.com.github.amedigital.repositories.PlanetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PlanetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanetaRepository repository;

    private PlanetaRequest request200;
    private PlanetaRequest request400;

    @BeforeEach
    public void configuracao() {
        request200 = new PlanetaRequest("Tatooine", "seco", "deserto");
        request400 = new PlanetaRequest("", "", "");
    }

    @Test
    @Transactional
public void deveRetornarCodigo201QuandoCriarPlanetaComSucesso() throws Exception {
        when(repository.existsByNome(request200.getNome())).thenReturn(false);
        when(repository.save(any())).thenReturn(new Planeta(1L, request200.getNome(), request200.getClima(), request200.getTerreno(), 5));

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

    @Test
    @Transactional
    public void deveRetornarCodigo400QuandoEnviarArgumentosInvalidos() throws Exception {
        when(repository.existsByNome(request200.getNome())).thenReturn(false);

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

    @Test
    @Transactional
    public void deveRetornarCodigo400QuandoPlanetaJaExistir() throws Exception {
        when(repository.existsByNome(request200.getNome())).thenReturn(true);

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

    @Test
    @Transactional
    public void deveRetornarCodigo200QuandoPlanetaForEncontradoPorId() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(new Planeta(1L, request200.getNome(), request200.getClima(), request200.getTerreno(), 5)));

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

    @Test
    @Transactional
    public void deveRetornarCodigo404QuandoPlanetaNaoForEncontradoPorId() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/planetas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Transactional
    public void deveRetornarCodigo200QuandoPlanetaForEncontradoPorNome() throws Exception {
        when(repository.findByNome(request200.getNome())).thenReturn(Optional.of(new Planeta(1L, request200.getNome(), request200.getClima(), request200.getTerreno(), 5)));

        MvcResult response = mockMvc.perform(get("/planetas/{nome}", request200.getNome())
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

    @Test
    @Transactional
    public void deveRetornarCodigo404QuandoPlanetaNaoForEncontradoPorNome() throws Exception {
        when(repository.findByNome(request200.getNome())).thenReturn(Optional.empty());

        mockMvc.perform(get("/planetas/{nome}", request200.getNome())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request200.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}