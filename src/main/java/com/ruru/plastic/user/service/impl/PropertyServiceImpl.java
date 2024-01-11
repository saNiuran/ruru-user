package com.ruru.plastic.user.service.impl;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.PropertyMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.Property;
import com.ruru.plastic.user.model.PropertyExample;
import com.ruru.plastic.user.service.PropertyService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public Property getPropertyById(Long id){
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Property> queryProperty(Property property){
        PropertyExample example = new PropertyExample();
        PropertyExample.Criteria criteria = example.createCriteria();

        if(property.getId()!=null){
            criteria.andIdEqualTo(property.getId());
        }
        if(StringUtils.isNotEmpty(property.getName())){
            criteria.andNameEqualTo(property.getName());
        }
        if(StringUtils.isNotEmpty(property.getSign())){
            criteria.andSignEqualTo(property.getSign());
        }
        if(property.getType()!=null){
            criteria.andTypeEqualTo(property.getType());
        }
        if(property.getStatus()!=null){
            criteria.andStatusEqualTo(property.getStatus());
        }

        return propertyMapper.selectByExample(example);
    }

    @Override
    public Msg<Property> createProperty(Property property){
        if(property==null || StringUtils.isEmpty(property.getName()) || StringUtils.isEmpty(property.getSign())){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        List<Property> list = queryProperty(new Property() {{
            setSign(property.getSign());
        }});

        if(list.size()>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        property.setId(null);
        if(property.getStatus()==null){
            property.setStatus(StatusEnum.可用.getValue());
        }
        propertyMapper.insertSelective(property);

        return Msg.success(getPropertyById(property.getId()));
    }

    @Override
    public Msg<Property> updateProperty(Property property){
        if(property==null || property.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        Property propertyById = getPropertyById(property.getId());
        if(propertyById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(property,propertyById, NullUtil.getNullPropertyNames(property));
        List<Property> list = queryProperty(new Property() {{
            setSign(propertyById.getSign());
        }});

        if(list.stream().anyMatch(v->!v.getId().equals(property.getId()))){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        propertyMapper.updateByPrimaryKeySelective(propertyById);
        return Msg.success(getPropertyById(property.getId()));
    }

    @Override
    public Msg<Property> deleteProperty(Property property){
        if(property==null || property.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        property.setStatus(StatusEnum.不可用.getValue());
        return updateProperty(property);
    }
}
