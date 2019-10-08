package restframework.comparison.PizzaServiceManager.priceCalculators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import restframework.comparison.PizzaServiceManager.model.Recipe;
import restframework.comparison.PizzaServiceManager.model.RecipeRepository;
import restframework.comparison.PizzaServiceManager.model.Resource;

@Component
@Profile("normal")  
public class NormalPriceCalculator implements PriceCalculator {

    @Autowired
    private RecipeRepository repo;

    public Double priceOf(Integer recipeID) throws Exception {
        Recipe recipe = repo.findById(recipeID).orElseThrow();
        Double price = 0.0;

        for (Resource r : recipe.getResources()) {
            price += r.getAmount();
        }

        return price;
    }
}