package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Company;
import com.ruru.plastic.user.request.CompanyRequest;

import java.util.List;

public interface CompanyService {

    Company getCompanyById(Long id);

    Company getCompanyBySocialCode(String socialCode);

    List<Company> queryCompany(Company company);

    Msg<Company> createCompany(Company company);

    Msg<Company> updateCompany(Company company);

    PageInfo<Company> filterCompany(CompanyRequest request);

    Msg<Company> deleteCompany(Company company);
}
