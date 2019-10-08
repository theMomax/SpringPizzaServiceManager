package restframework.comparison.PizzaServiceManager.priceCalculators;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("fixed")
public class FixedPriceCalculator implements PriceCalculator {

    public Double priceOf(Integer recipeID) {
        return 7.0;
    }
}