package danieltsuzuk.com.github.amedigital.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "planetas")
public class Planeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String clima;

    @Column(length = 50)
    private String terreno;

}
