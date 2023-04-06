package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.response.CorporateCertResponse;

public interface CorporateCertService {

    CorporateCert getCorporateCertById(Long id);

    CorporateCertResponse getCorporateCertResponseById(Long id);

    CorporateCert getCorporateCertByUserId(Long userId);

    Msg<CorporateCert> createCorporateCert(CorporateCert corporateCert);

    Msg<CorporateCert> updateCorporateCert(CorporateCert corporateCert);

    Msg<Integer> deleteCorporateCert(CorporateCert corporateCert);

    PageInfo<CorporateCert> filterCorporateCert(CorporateCertRequest request);

    PageInfo<CorporateCertResponse> filterCorporateCertResponse(CorporateCertRequest request);
}
