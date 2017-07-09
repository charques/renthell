package io.renthell.dataextractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by cfhernandez on 9/7/17.
 */
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item")
    public List<FotocasaItem> findAllItems() {
        return itemService.findAll();
    }
}