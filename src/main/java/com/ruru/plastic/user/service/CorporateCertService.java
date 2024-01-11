package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.request.CorporateCertRequest;

import java.util.List;

public interface CorporateCertService {

    CorporateCert getCorporateCertById(Long id);

    CorporateCert getCorporateCertBySocialCode(String socialCode);

    List<CorporateCert> queryCorporateCert(CorporateCertRequest request);

    Msg<CorporateCert> createCorporateCert(CorporateCert corporateCert);

    Msg<CorporateCert> updateCorporateCert(CorporateCert corporateCert);

    Msg<Integer> deleteCorporateCert(CorporateCert corporateCert);

    PageInfo<CorporateCert> filterCorporateCert(CorporateCertRequest request);
}
