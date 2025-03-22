package com.scheduler.batch.domain.certificate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NationalProInfo {

    private String name;

    private String law;

    private String institution;

    private String institutionUrl;

    private String agency;

    private String agencyUrl;

    private String purpose;

    private String effect;

    private String grade;

    private String description;

    private String requirements;

    private String qualification;

    private String receptionDetail;

    private String process;

    private String subject;

    private String criteria;

    private String career;

    private String exemptions;
}
