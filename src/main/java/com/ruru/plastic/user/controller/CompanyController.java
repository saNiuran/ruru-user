package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Company;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.CompanyRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.CompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @LoginRequired
    @PostMapping("/info")
    public DataResponse<Company> getCompanyById(@RequestBody Company company){
        if(company==null || company.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        Company companyById = companyService.getCompanyById(company.getId());
        if(companyById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(companyById);
    }

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<Company> createCompany(@RequestBody Company company){
        Msg<Company> msg = companyService.createCompany(company);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<Company> updateCompany(@RequestBody Company company){
        Msg<Company> msg = companyService.updateCompany(company);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<Company> deleteCompany(@RequestBody Company company){
        Msg<Company> msg = companyService.deleteCompany(company);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<Company>> filterCompany(@RequestBody CompanyRequest request){
        return DataResponse.success(companyService.filterCompany(request));
    }
}
