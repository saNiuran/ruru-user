package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.BlueCertMapper;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.model.BlueCert;
import com.ruru.plastic.user.model.BlueCertExample;
import com.ruru.plastic.user.request.BlueCertRequest;
import com.ruru.plastic.user.service.BlueCertService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlueCertServiceImpl implements BlueCertService {

    @Autowired
    private BlueCertMapper blueCertMapper;

    @Override
    public BlueCert getBlueCertById(Long id){
        return blueCertMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BlueCert> queryBlueCert(BlueCertRequest request){
        BlueCertExample example = new BlueCertExample();
        BlueCertExample.Criteria criteria = example.createCriteria();

        queryBlueCert(request,criteria);
        if (StringUtils.isNotEmpty(request.getOrderClause())) {
            example.setOrderByClause(request.getOrderClause());
        } else {
            example.setOrderByClause("update_time desc");
        }
        return blueCertMapper.selectByExample(example);
    }

    @Override
    public Msg<BlueCert> createBlueCert(BlueCert blueCert){
        if(blueCert==null || StringUtils.isEmpty(blueCert.getSocialCode())
                || StringUtils.isEmpty(blueCert.getLicenseImg()) || StringUtils.isEmpty(blueCert.getContactName())
                || StringUtils.isEmpty(blueCert.getContactIdNo())){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        List<BlueCert> list = queryBlueCert(new BlueCertRequest() {{
            setSocialCode(blueCert.getSocialCode());
            setContactIdNo(blueCert.getContactIdNo());
        }});

        if(list.size()>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        blueCert.setId(null);
        if(blueCert.getStatus()==null){
            blueCert.setStatus(CertStatusEnum.待付款.getValue());
        }
        if(blueCert.getCreateTime()==null){
            blueCert.setCreateTime(new Date());
        }
        if(blueCert.getUpdateTime()==null){
            blueCert.setUpdateTime(new Date());
        }

        blueCertMapper.insertSelective(blueCert);
        return Msg.success(getBlueCertById(blueCert.getId()));
    }

    @Override
    public Msg<BlueCert> updateBlueCert(BlueCert blueCert){
        if(blueCert==null || blueCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        BlueCert blueCertById = getBlueCertById(blueCert.getId());
        if(blueCertById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(blueCert,blueCertById, NullUtil.getNullPropertyNames(blueCert));

        List<BlueCert> list = queryBlueCert(new BlueCertRequest() {{
            setSocialCode(blueCertById.getSocialCode());
            setContactIdNo(blueCertById.getContactIdNo());
        }});

        if(list.stream().anyMatch(v->!v.getId().equals(blueCertById.getId()))){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        blueCertById.setUpdateTime(new Date());
        blueCertMapper.updateByPrimaryKeySelective(blueCertById);
        return Msg.success(getBlueCertById(blueCert.getId()));
    }

    @Override
    public Msg<BlueCert> deleteBlueCert(BlueCert blueCert){
        if(blueCert==null || blueCert.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        blueCert.setStatus(CertStatusEnum.失效.getValue());
        return updateBlueCert(blueCert);
    }

    @Override
    public PageInfo<BlueCert> filterBlueCert(BlueCertRequest request){
        BlueCertExample example = new BlueCertExample();
        BlueCertExample.Criteria criteria = example.createCriteria();

        queryBlueCert(request, criteria);

        if (StringUtils.isNotEmpty(request.getOrderClause())) {
            example.setOrderByClause(request.getOrderClause());
        } else {
            example.setOrderByClause("update_time desc");
        }
        PageHelper.startPage(request.getPage(), request.getSize());

        return new PageInfo<>(blueCertMapper.selectByExample(example));
    }

    private static void queryBlueCert(BlueCertRequest request, BlueCertExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(StringUtils.isNotEmpty(request.getName())){
            criteria.andNameEqualTo(request.getName());
        }
        if(StringUtils.isNotEmpty(request.getSocialCode())){
            criteria.andSocialCodeEqualTo(request.getSocialCode());
        }
        if(StringUtils.isNotEmpty(request.getLicenceAddr())){
            criteria.andLicenceAddrEqualTo(request.getLicenceAddr());
        }
        if(StringUtils.isNotEmpty(request.getLicenseImg())){
            criteria.andLicenseImgEqualTo(request.getLicenseImg());
        }
        if(request.getStatus()!=null){
            criteria.andStatusEqualTo(request.getStatus());
        }
        if(StringUtils.isNotEmpty(request.getLegalPerson())){
            criteria.andLegalPersonEqualTo(request.getLegalPerson());
        }
        if(StringUtils.isNotEmpty(request.getContactName())){
            criteria.andContactNameEqualTo(request.getContactName());
        }
        if(StringUtils.isNotEmpty(request.getContactMobile())){
            criteria.andContactMobileEqualTo(request.getContactMobile());
        }
        if(StringUtils.isNotEmpty(request.getContactIdNo())){
            criteria.andContactIdNoEqualTo(request.getContactIdNo());
        }

        if (request.getStartTime() != null) {
            criteria.andUpdateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andUpdateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }

}
