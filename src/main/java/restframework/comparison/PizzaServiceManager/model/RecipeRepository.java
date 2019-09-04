package restframework.comparison.PizzaServiceManager.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import restframework.comparison.PizzaServiceManager.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    /**
     * Properly saves any Recipe. This includes connecting the contained
     * resources to their parent. This function is to be used instead of 
     * save.
     * 
     * @param entity any Recipe
     * @return the updated version of entity, after it was saved to the database
     */
    default <S extends Recipe> S store(S entity) {
        for (Resource r : entity.getResources()) {
            r.setRecipe(entity);
        }
        return save(entity);
    }

    @Query("SELECT r FROM Recipe r WHERE r.title = :title")
    Iterable<Recipe> findByTitle(@Param("title") String title);

} 