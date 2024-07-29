package danieltsuzuk.com.github.amedigital.specifications;

import danieltsuzuk.com.github.amedigital.entities.Planeta;
import org.springframework.data.jpa.domain.Specification;

/**
 * Classe de especificações para a entidade Planeta.
 * <p>
 * Esta classe contém métodos estáticos que retornam especificações para serem usadas em consultas JPA Criteria.
 * </p>
 */
public class PlanetaSpecification {

    /**
     * Cria uma especificação para buscar planetas cujo nome contenha a string fornecida.
     * <p>
     * Se o nome for nulo, a especificação retornará uma conjunção (sem filtro).
     * </p>
     *
     * @param nome o nome ou parte do nome a ser buscado
     * @return uma especificação para buscar planetas pelo nome
     */
    public static Specification<Planeta> likeNome(String nome) {
        return (root, query, builder) -> nome == null ? builder.conjunction() : builder.like(root.get("nome"), "%" + nome + "%");
    }

    /**
     * Cria uma especificação para buscar planetas com o ID especificado.
     * <p>
     * Se o ID for nulo, a especificação retornará uma conjunção (sem filtro).
     * </p>
     *
     * @param id o ID do planeta a ser buscado
     * @return uma especificação para buscar planetas pelo ID
     */
    public static Specification<Planeta> hasId(Long id) {
        return (root, query, builder) -> id == null ? builder.conjunction() : builder.equal(root.get("id"), id);
    }
}
