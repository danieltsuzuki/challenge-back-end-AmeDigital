package danieltsuzuk.com.github.amedigital.dto;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class PlanetaRequest {

    @NotBlank
    @Length(max = 50, min = 3)
    private String nome;
    @NotBlank
    @Length(max = 50, min = 3)
    private String clima;
    @NotBlank
    @Length(max = 50, min = 3)
    private String terreno;

    public Planeta criarPlanetaComAparicoes(int aparicoes) {
        return new Planeta(null, nome, clima, terreno, aparicoes);
    }

    @Override
    public String toString() {
        return "{\"nome\":\"" + nome + "\",\"clima\":\"" + clima + "\",\"terreno\":\"" + terreno + "\"}";
    }
}
