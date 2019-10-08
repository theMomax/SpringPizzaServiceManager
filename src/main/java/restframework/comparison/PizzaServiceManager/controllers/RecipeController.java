package restframework.comparison.PizzaServiceManager.controllers;

import java.util.Iterator;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Response<Integer> createRecipe(@RequestBody Recipe recipe, HttpServletResponse response) {
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
        response.setStatus(HttpServletResponse.SC_CREATED);
        return new Response<>(recipe.getId());
    }

    /**
     * fetchAll -> TODO: api-link
     */
    @GetMapping("")
    public Response<Iterable<Recipe>> fetchAll() {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        if (!recipes.iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no recipes found");
        }

        Iterator<Recipe> it = recipes.iterator();
        int i = 0;
        for (; it.hasNext(); ++i) it.next();

        return new Response<>(i + " recipes found", recipes);
    }

    /**
     * fetch -> TODO: api-link
     */
    @GetMapping("/{id}")
    public Response<Recipe> fetch(@PathVariable(value="id") Integer id) {
        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " not found");
        }

        return new Response<>("found", ropt.get());
    }

    /**
     * deleteRecipe -> TODO: api-link
     */
    @DeleteMapping("/{id}")
    public Response<Object> deleteRecipe(@PathVariable(value="id") Integer id) {
        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " did not exist");
        }

        recipeRepository.deleteById(id);
        return new Response<>("recipe was deleted succesfully");
    }

    /**
     * updateRecipe -> TODO: api-link
     */
    @PutMapping("/{id}")
    public Response<Integer> updateRecipe(@PathVariable(value="id") Integer id, @RequestBody Recipe recipe) {
        if ("".equals(recipe.getTitle()) || recipe.getTitle() == null || recipe.getResources() == null || recipe.getResources().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "illegal content for recipe");
        }

        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "there is no recipe with id " + id);
        }

        recipeRepository.delete(ropt.get());

        recipe = recipeRepository.store(recipe);
        return new Response<>(recipe.getId());
    }

}