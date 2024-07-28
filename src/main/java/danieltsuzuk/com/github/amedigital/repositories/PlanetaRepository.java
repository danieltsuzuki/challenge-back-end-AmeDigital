package danieltsuzuk.com.github.amedigital.repositories;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Long> {
    boolean existsByNome(String nome);
}
