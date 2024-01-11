package com.ruru.plastic.user.controller;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Property;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.PropertyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    public PropertyService propertyService;

    @PostMapping("/info")
    public DataResponse<Property> getProperty(@RequestBody Property property){
        if(property==null || property.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        Property propertyById = propertyService.getPropertyById(property.getId());
        if(propertyById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(propertyById);
    }

    @PostMapping("/query")
    public DataResponse<List<Property>> queryProperty(@RequestBody Property property){
        return DataResponse.success(propertyService.queryProperty(property));
    }
    
    @PostMapping("/new")
    public DataResponse<Property> createProperty(@RequestBody Property property){
        Msg<Property> msg = propertyService.createProperty(property);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/update")
    public DataResponse<Property> updateProperty(@RequestBody Property property){
        Msg<Property> msg = propertyService.updateProperty(property);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/delete")
    public DataResponse<Property> deleteProperty(@RequestBody Property property){
        Msg<Property> msg = propertyService.deleteProperty(property);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

}
