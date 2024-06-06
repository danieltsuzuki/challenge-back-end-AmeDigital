package danieltsuzuk.com.github.amedigital.dto;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class PlanetaRequest {

    @NotBlank
    @Length(max = 50, min = 3)
    private String nome;
    @Length(max = 50)
    private String clima;
    @Length(max = 50)
    private String terreno;

    public Planeta criarPlanetaComAparicoes(int aparicoes) {
        return new Planeta(null, nome, clima, terreno, aparicoes);
    }

}
