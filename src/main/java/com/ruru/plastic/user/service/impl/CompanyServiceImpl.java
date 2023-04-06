package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.CompanyMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.Company;
import com.ruru.plastic.user.model.CompanyExample;
import com.ruru.plastic.user.request.CompanyRequest;
import com.ruru.plastic.user.service.CompanyService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Company getCompanyById(Long id) {
        return companyMapper.selectByPrimaryKey(id);
    }

    @Override
    public Company getCompanyBySocialCode(String socialCode){
        CompanyExample example = new CompanyExample();
        example.createCriteria().andSocialCodeEqualTo(socialCode);
        return companyMapper.selectOneByExample(example);
    }

    @Override
    public List<Company> queryCompany(Company company) {
        CompanyRequest request  =new CompanyRequest();
        BeanUtils.copyProperties(company,request);

        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria criteria = example.createCriteria();

        queryCompany(request, criteria);
        example.setOrderByClause("update_time desc");

        return companyMapper.selectByExample(example);
    }

    @Override
    public Msg<Company> createCompany(Company company) {
        if (company == null || StringUtils.isEmpty(company.getName()) || StringUtils.isEmpty(company.getSocialCode())) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        Company companyBySocialCode = getCompanyBySocialCode(company.getSocialCode());
        if (companyBySocialCode != null) {
            company.setId(companyBySocialCode.getId());
            return updateCompany(company);
        }

        if (company.getStatus() == null) {
            company.setStatus(StatusEnum.可用.getValue());
        }
        if(company.getCreateTime()==null){
            company.setCreateTime(new Date());
        }
        if(company.getUpdateTime()==null){
            company.setUpdateTime(new Date());
        }
        companyMapper.insertSelective(company);
        return Msg.success(getCompanyById(company.getId()));
    }

    @Override
    public Msg<Company> updateCompany(Company company) {
        if (company == null || company.getId() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        Company companyById = getCompanyById(company.getId());
        if(companyById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(company,companyById,NullUtil.getNullPropertyNames(company));
        CompanyExample example = new CompanyExample();
        example.createCriteria().andIdNotEqualTo(company.getId()).andSocialCodeEqualTo(companyById.getSocialCode());
        if(companyMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        company.setUpdateTime(new Date());
        companyMapper.updateByPrimaryKeySelective(company);
        return Msg.success(getCompanyById(company.getId()));
    }

    @Override
    public PageInfo<Company> filterCompany(CompanyRequest request) {
        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria criteria = example.createCriteria();

        queryCompany(request, criteria);

        if(StringUtils.isNotEmpty(request.getOrderClause())){
            example.setOrderByClause(request.getOrderClause());
        }else{
            example.setOrderByClause("update_time desc");
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        return new PageInfo<>(companyMapper.selectByExample(example));
    }

    private static void queryCompany(CompanyRequest request, CompanyExample.Criteria criteria) {
        if (request.getId() != null) {
            criteria.andIdEqualTo(request.getId());
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            criteria.andNameEqualTo( request.getName());
        }
        if (StringUtils.isNotEmpty(request.getAddress())) {
            criteria.andAddressEqualTo(request.getAddress());
        }
        if(StringUtils.isNotEmpty(request.getProvince())){
            criteria.andProvinceEqualTo(request.getProvince());
        }
        if(StringUtils.isNotEmpty(request.getCity())){
            criteria.andCityEqualTo(request.getCity());
        }
        if(StringUtils.isNotEmpty(request.getCounty())){
            criteria.andCountyEqualTo(request.getCounty());
        }
        if(StringUtils.isNotEmpty(request.getDistrictCode())){
            criteria.andDistrictCodeEqualTo(request.getDistrictCode());
        }
        if (StringUtils.isNotEmpty(request.getSocialCode())) {
            criteria.andSocialCodeEqualTo(request.getSocialCode());
        }
        if(StringUtils.isNotEmpty(request.getIndustry())){
            criteria.andIndustryEqualTo(request.getIndustry());
        }
        if(StringUtils.isNotEmpty(request.getLegalPersonName())){
            criteria.andLegalPersonNameEqualTo(request.getLegalPersonName());
        }
        if(StringUtils.isNotEmpty(request.getContactName())){
            criteria.andContactNameEqualTo(request.getContactName());
        }
        if(StringUtils.isNotEmpty(request.getContactPhone())){
            criteria.andContactPhoneEqualTo(request.getContactPhone());
        }
        if(StringUtils.isNotEmpty(request.getMainBiz())){
            criteria.andMainBizEqualTo(request.getMainBiz());
        }
        if(request.getAnnualProcurement()!=null){
            criteria.andAnnualProcurementEqualTo(request.getAnnualProcurement());
        }
        if (request.getStatus() != null) {
            criteria.andStatusEqualTo(request.getStatus());
        }
        if (request.getStartTime() != null) {
            criteria.andUpdateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andUpdateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }

    @Override
    public Msg<Company> deleteCompany(Company company) {
        company.setStatus(StatusEnum.不可用.getValue());
        return updateCompany(company);
    }


}
