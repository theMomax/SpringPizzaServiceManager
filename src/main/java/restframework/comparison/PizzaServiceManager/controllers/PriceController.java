package restframework.comparison.PizzaServiceManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import restframework.comparison.PizzaServiceManager.priceCalculators.PriceCalculator;


@RestController
@RequestMapping("/api/v1/price")
public class PriceController {

    @Autowired
    private PriceCalculator calculator;

    /**
     * createRecipe -> TODO: api-link
     */
    @GetMapping("/{id}")
    public Response<Double> createRecipe(@PathVariable(value="id") Integer recipeID) {
        Double price;
        try {
            price = calculator.priceOf(recipeID);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id " + recipeID);
        }
        return new Response<>(price);
    }
}