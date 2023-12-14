package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.BlueCert;
import com.ruru.plastic.user.request.BlueCertRequest;

import java.util.List;

public interface BlueCertService {
    BlueCert getBlueCertById(Long id);

    List<BlueCert> queryBlueCert(BlueCertRequest request);

    Msg<BlueCert> createBlueCert(BlueCert blueCert);

    Msg<BlueCert> updateBlueCert(BlueCert blueCert);

    Msg<BlueCert> deleteBlueCert(BlueCert blueCert);

    PageInfo<BlueCert> filterBlueCert(BlueCertRequest request);
}
