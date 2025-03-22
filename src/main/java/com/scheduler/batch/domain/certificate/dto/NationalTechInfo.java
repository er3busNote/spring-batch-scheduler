package com.scheduler.batch.domain.certificate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NationalTechInfo {

    private String name;

    private String type;

    private String institution;
}
