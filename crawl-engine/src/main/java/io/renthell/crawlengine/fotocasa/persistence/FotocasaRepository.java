package io.renthell.crawlengine.fotocasa.persistence;

import io.renthell.crawlengine.fotocasa.model.FotocasaItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by cfhernandez on 5/7/17.
 */
public interface FotocasaRepository extends MongoRepository<FotocasaItem, String> {

    public List<FotocasaItem> findByPostalCode(String postalCode);
}
