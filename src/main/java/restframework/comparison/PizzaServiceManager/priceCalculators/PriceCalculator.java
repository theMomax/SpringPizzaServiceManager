package restframework.comparison.PizzaServiceManager.priceCalculators;

public interface PriceCalculator {

    Double priceOf(Integer recipeID) throws Exception;

}