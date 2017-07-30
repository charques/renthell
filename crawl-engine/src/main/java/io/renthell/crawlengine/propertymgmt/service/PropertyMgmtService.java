package io.renthell.crawlengine.propertymgmt.service;

import io.renthell.crawlengine.fotocasa.model.FotocasaItem;

public interface PropertyMgmtService {

    public void addPropertyTransaction(FotocasaItem item, Integer transactionIndex);
}
