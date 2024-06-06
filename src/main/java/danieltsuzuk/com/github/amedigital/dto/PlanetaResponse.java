package danieltsuzuk.com.github.amedigital.dto;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import lombok.Getter;

@Getter
public class PlanetaResponse {

    private Long id;
    private String nome;
    private String clima;
    private String terreno;
    private int aparicoes;

    public PlanetaResponse(Planeta planeta){
        this.id = planeta.getId();
        this.nome = planeta.getNome();
        this.clima = planeta.getClima();
        this.terreno = planeta.getTerreno();
        this.aparicoes = planeta.getAparicoes();
    }

}
