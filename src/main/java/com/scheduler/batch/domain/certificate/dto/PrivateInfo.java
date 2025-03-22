package com.scheduler.batch.domain.certificate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrivateInfo {

    private String createDate;

    private String status;

    private String institution;

    private String createNum;

    private String name;

    private String grade;

    private String agency;

    private String summary;

    private String description;

    private String authorizedStatus;

    private String authorizedYn;

    private String coOperation;
}
