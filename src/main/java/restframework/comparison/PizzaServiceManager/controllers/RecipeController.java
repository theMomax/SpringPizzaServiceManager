package restframework.comparison.PizzaServiceManager.controllers;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import restframework.comparison.PizzaServiceManager.model.Recipe;
import restframework.comparison.PizzaServiceManager.model.RecipeRepository;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeController {

    

    @Autowired
    private RecipeRepository recipeRepository;

    /**
     * createRecipe -> TODO: api-link
     */
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> createRecipe(@RequestBody Recipe recipe) {
        if (recipe.getId() != null ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "cannot create recipe with specific id");
        }
        
        if ("".equals(recipe.getTitle()) || recipe.getTitle() == null || recipe.getResources() == null || recipe.getResources().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "illegal content for recipe");
        }

        if (recipeRepository.findByTitle(recipe.getTitle()).iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "recipe with title " + recipe.getTitle() + " already exists");
        }

        recipe = recipeRepository.store(recipe);
        
        return ResponseEntity.ok(Map.of("data", recipe.getId()));
    }

    /**
     * fetchAll -> TODO: api-link
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> fetchAll() {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        if (!recipes.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no recipes found");
        }

        Iterator<Recipe> it = recipes.iterator();
        int i = 0;
        for (; it.hasNext(); ++i) it.next();

        return ResponseEntity.ok(Map.of("message", i + " recipes found", "data", recipes));
    }

    /**
     * fetch -> TODO: api-link
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> fetch(@PathVariable(value="id") Integer id) {
        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " not found");
        }

        return ResponseEntity.ok(Map.of("message", "found", "data", ropt.get()));
    }

    /**
     * deleteRecipe -> TODO: api-link
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRecipe(@PathVariable(value="id") Integer id) {
        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " did not exist");
        }

        recipeRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "recipe was deleted succesfully"));
    }

    /**
     * updateRecipe -> TODO: api-link
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRecipe(@PathVariable(value="id") Integer id, @RequestBody Recipe recipe) {
        if ("".equals(recipe.getTitle()) || recipe.getTitle() == null || recipe.getResources() == null || recipe.getResources().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "illegal content for recipe");
        }

        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "there is no recipe with id " + id);
        }

        recipeRepository.delete(ropt.get());

        recipe = recipeRepository.store(recipe);
        return ResponseEntity.ok(Map.of("data", recipe.getId()));
    }

}