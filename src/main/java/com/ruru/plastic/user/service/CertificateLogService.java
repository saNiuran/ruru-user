package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.request.CertificateLogRequest;

import java.util.List;

public interface CertificateLogService {

    CertificateLog getCertificateLogById(Long id);

    List<CertificateLog> queryCertificateLog(CertificateLog log);

    Msg<CertificateLog> createCertificateLog(CertificateLog log);

    PageInfo<CertificateLog> filterCertificateLog(CertificateLogRequest request);
}
