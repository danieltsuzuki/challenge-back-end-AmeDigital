package danieltsuzuk.com.github.amedigital.repositories;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface para o repositório de Planeta.
 * <p>
 * Esta interface estende JpaRepository e JpaSpecificationExecutor para fornecer operações de CRUD
 * e funcionalidades de especificação para a entidade Planeta.
 * </p>
 * <p>
 * A anotação @Repository indica que esta interface é um repositório Spring Data JPA.
 * </p>
 */
@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Long>, JpaSpecificationExecutor<Planeta> {

    /**
     * Verifica se um planeta com o nome especificado existe.
     *
     * @param nome o nome do planeta a ser verificado
     * @return true se um planeta com o nome especificado existir, caso contrário, false
     */
    boolean existsByNome(String nome);

    /**
     * Busca um planeta pelo nome.
     *
     * @param nome o nome do planeta a ser buscado
     * @return um Optional contendo o planeta encontrado, ou um Optional vazio se nenhum planeta for encontrado
     */
    Optional<Planeta> findByNome(String nome);
}
