package com.scheduler.batch.domain.certificate.mapper;

import com.scheduler.batch.domain.certificate.dto.CertificationInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CertificateMapper {

    void insertNationalTechnical(@Param("p") CertificationInfo certificationInfo);

    void insertNationalProfessional(@Param("p") CertificationInfo certificationInfo);

    void insertPrivate(@Param("p") CertificationInfo certificationInfo);
}
