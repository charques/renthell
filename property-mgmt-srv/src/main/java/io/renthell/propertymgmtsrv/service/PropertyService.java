package io.renthell.propertymgmtsrv.service;

import io.renthell.propertymgmtsrv.web.dto.PropertyDto;

import java.text.ParseException;
import java.util.List;

public interface PropertyService {

    public PropertyDto save(PropertyDto property);

    public List<PropertyDto> findAll();

    public PropertyDto findOne(String uuid);
}
