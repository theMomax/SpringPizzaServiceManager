package restframework.comparison.PizzaServiceManager.priceCalculators;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class MockPriceCalculator implements PriceCalculator {

    public Double priceOf(Integer recipeID)  {
        return 0.0;
    }
}