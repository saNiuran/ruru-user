package com.ruru.plastic.user.service;

import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Property;

import java.util.List;

public interface PropertyService {
    Property getPropertyById(Long id);

    List<Property> queryProperty(Property property);

    Msg<Property> createProperty(Property property);

    Msg<Property> updateProperty(Property property);

    Msg<Property> deleteProperty(Property property);
}
