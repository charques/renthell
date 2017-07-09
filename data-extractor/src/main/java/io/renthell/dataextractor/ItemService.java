package io.renthell.dataextractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cfhernandez on 9/7/17.
 */
@Service
public class ItemService {
    @Autowired
    private FotocasaRepository repository;

    public List<FotocasaItem> findAll() {
        List<FotocasaItem> items = repository.findAll();
        return items;
    }
}
