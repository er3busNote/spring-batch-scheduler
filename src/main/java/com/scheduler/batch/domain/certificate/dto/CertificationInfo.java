package com.scheduler.batch.domain.certificate.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class CertificationInfo {

    private String seq;

    private String name;

    private String grade;

    private String type;

    private Character authYn;

    private String agency;

    private String institution;

    public static CertificationInfo of(Item item) {
        return CertificationInfo.builder()
                .name(item.getJmNm())
                .grade(item.getSeriesNm())
                .type("국가기술자격")
                .authYn('Y')
                .agency(item.getImplNm())
                .institution(item.getInstiNm())
                .build();
    }

    public static CertificationInfo of(NationalTechInfo nationalTechInfo) {
        return CertificationInfo.builder()
                .name(nationalTechInfo.getName())
                .type(nationalTechInfo.getType())
                .authYn('Y')
                .institution(nationalTechInfo.getInstitution())
                .build();
    }

    public static CertificationInfo of(NationalProInfo nationalProInfo, String grade) {
        return CertificationInfo.builder()
                .name(nationalProInfo.getName())
                .grade(grade)
                .type("국가전문자격")
                .authYn('Y')
                .agency(nationalProInfo.getAgency())
                .institution(nationalProInfo.getInstitution())
                .build();
    }

    public static CertificationInfo of(PrivateInfo privateInfo, String grade, String authYn) {
        return CertificationInfo.builder()
                .name(privateInfo.getName())
                .grade(grade)
                .type("민간자격증")
                .authYn(convertToChar(authYn))
                .agency(privateInfo.getAgency())
                .institution(privateInfo.getInstitution())
                .build();
    }

    private static Character convertToChar(String str) {
        return Optional.ofNullable(str)
                .filter(s -> s.equals("Y") || s.equals("N"))
                .map(s -> s.charAt(0))
                .orElse('\0');
    }
}
