package restframework.comparison.PizzaServiceManager.controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import restframework.comparison.PizzaServiceManager.model.Recipe;
import restframework.comparison.PizzaServiceManager.model.RecipeRepository;
import restframework.comparison.PizzaServiceManager.model.Resource;
import restframework.comparison.PizzaServiceManager.model.Store;
import restframework.comparison.PizzaServiceManager.model.StoreRepository;

@RestController
@RequestMapping("/api/v1")
public class ResourcesController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private Store store;

    @PostConstruct
    private void init() {
        // load or create store
        Iterator<Store> it = storeRepository.findAll().iterator();

        if (it.hasNext()) {
            store = it.next();
        } else {
            store = new Store();
            storeRepository.save(store);
        }
    }

    /**
     * addResource -> TODO: api-link
     */
    @PostMapping("/refill")
    public Response<Object> addResource(@RequestBody List<Resource> resources) throws Exception {
        store = storeRepository.store(store, resources);

        return new Response<>(resources.size() + " resources added to store");
    }

    /**
     * fetchAvailable -> TODO: api-link
     */
    @GetMapping("/store")
    public Response<List<Resource>> fetchAvailable() throws Exception{
        List<Resource> items = storeRepository.list(store);

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "no items found");
        }

        return new Response<>(items.size() + " resources found", items);
    }

    /**
     * order -> TODO: api-link
     */
    @PostMapping("/order/{id}")
    public Response<Object> order(@PathVariable(value="id") Integer id) {
        Optional<Recipe> ropt = recipeRepository.findById(id);
        if (!ropt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe with id " + id + " not found");
        }
        
        try {
            store = storeRepository.removeItems(store, ropt.get().getResources());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.IM_USED, e.getMessage());
        }

        return new Response<>("ordered " + ropt.get().getTitle());
    }

}

/*
// Order -> TODO: api-link
func Order(c *gin.Context) {
	var recipe model.Recipe
	id, err := strconv.ParseUint(c.Param("id"), 10, 64)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"message": "invalid id-format: " + c.Param("id")})
		return
	}
	recipe.ID = uint(id)
	err = recipe.Read()
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"message": err.Error()})
		return
	}
	log.Println(recipe)

	err = model.S.Remove(recipe.Resources...)
	if err != nil {
		c.JSON(http.StatusRequestEntityTooLarge, gin.H{"message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "ordered " + recipe.Title})
}*/