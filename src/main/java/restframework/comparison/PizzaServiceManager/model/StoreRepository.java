package restframework.comparison.PizzaServiceManager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;

public interface StoreRepository extends CrudRepository<Store, Integer> {
    

    default Store store(Store store, List<Resource> items) throws Exception {
        if (store == null) {
            throw new Exception("fatal: store does not exist");
        }
        synchronized(store) {
            items.forEach((r) -> store.addItem(r));
            return save(store);
        }
    } 

    default List<Resource> list(Store store) throws Exception {
        if (store == null) {
            throw new Exception("fatal: store does not exist");
        }

        Optional<Store> opts = findById(store.getId());
        if (!opts.isPresent()) {
            throw new Exception("store with id " + store.getId() + " does not exist");
        } 

        return opts.get().getItems();
    }

    /**
     * Remove items from the store. If there is not enough of one ingredient
     * available, the method throws an exception
     * 
     * @param store store the items are removed from
     * @param items resources to remove from store
     * @return updated version of store
     * @throws Exception if the store is not valid, or there is not enough of one ingredient available
     */
    default Store removeItems(Store store, List<Resource> items) throws Exception {
        if (store == null) {
            throw new Exception("fatal: store does not exist");
        }

        List<Resource> changed = new ArrayList<>();

        synchronized(store) {
            Optional<Store> ost = findById(store.getId());
            if (!ost.isPresent()) {
                throw new Exception("fatal: store not available");
            }

            Store s = ost.get();

            for (Resource r : items) {
                List<Resource> available = s.getItems().stream().filter((res) -> r.getName().equals(res.getName())).collect(Collectors.toList());
                double amount = r.getAmount();
                int i = 0;
                while (i < available.size() && amount > 0) {
                    double min = Double.min(available.get(i).getAmount(), amount);
                    available.get(i).setAmount(available.get(i).getAmount() - min);
                    amount -= min;
                    changed.add(available.get(i));
                    i++;
                }
                if (amount > 0) {
                    throw new Exception("not enough " + r.getName() + " available");
                }
            }
            for (int i = 0; i < changed.size(); i++) {
                if (changed.get(i).getAmount() <= 0) {
                    s.removeItem(changed.get(i));
                }
            }
            return save(s);
        }
    }

}