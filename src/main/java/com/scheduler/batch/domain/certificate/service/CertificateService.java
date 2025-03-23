package com.scheduler.batch.domain.certificate.service;

import com.scheduler.batch.common.utils.WebClientUtil;
import com.scheduler.batch.domain.certificate.dto.*;
import com.scheduler.batch.domain.certificate.mapper.CertificateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateService {

    private static final String API_URL = "http://openapi.q-net.or.kr/api/service/rest/InquiryQualInfo/getList";
    private static final String API_KEY = "";
    private static final String SERVICE_CODE = "01"; // (계열코드) 01:기술사, 02:기능장, 03:기사, 04:기능사
    private static final NationalTechCode nationalTechCode = NationalTechCode.of(SERVICE_CODE);

    private final CertificateMapper certificateMapper;

    public List<Item> getNationalTechnical() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("serviceKey", API_KEY);
        param.add("seriesCd", SERVICE_CODE);

        String xmlData = WebClientUtil.callApiXMLString(API_URL, param); // 동기식 호출

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Response.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(Objects.requireNonNull(xmlData));
            Response buildingInfoResponse = (Response) unmarshaller.unmarshal(reader);
            List<Item> itemList = buildingInfoResponse.getBody().getItems();
            log.info("item = {}", itemList.get(0));
            return itemList;
        } catch (NullPointerException | JAXBException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNationalTechnical(Item item) {
        log.info("item = {}", item.toString());
        certificateMapper.insertNationalTechnical(CertificationInfo.of(item));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNationalTechnical(NationalTechInfo nationalTechInfo) {
        log.info("item = {}", nationalTechInfo.toString());
        certificateMapper.insertNationalTechnical(CertificationInfo.of(nationalTechInfo));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNationalProfessional(NationalProInfo nationalProInfo) {
        String name = nationalProInfo.getName();
        String agency = nationalProInfo.getAgency();
        String gradeInfo = nationalProInfo.getGrade();
        ArrayList<String> grades = parseGrade(gradeInfo);
        for (String grade : grades) {
            log.info("name = {}, agency = {}, grade = {}", name, agency, grade);
            certificateMapper.insertNationalProfessional(CertificationInfo.of(nationalProInfo, grade));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePrivate(PrivateInfo privateInfo) {
        String name = privateInfo.getName();
        String agency = privateInfo.getAgency();
        String gradeInfo = privateInfo.getGrade();
        String authorizedStatus = privateInfo.getAuthorizedStatus();
        String authorizedYn = privateInfo.getAuthorizedYn();
        if(authorizedStatus.equals("부분공인")) {
            Pattern pattern = Pattern.compile("([\\w가-힣\\s\\(\\)\\+\\-Ⅱ]+)\\(([YN])\\)");
            Matcher matcher = pattern.matcher(authorizedYn);
            while (matcher.find()) {
                String grade = matcher.group(1).trim();
                String authYn = matcher.group(2).trim();
                log.info("name = {}, agency = {}, grade = {}, authYn = {}", name, agency, grade, authYn);
                certificateMapper.insertPrivate(CertificationInfo.of(privateInfo, grade, authYn));
            }
        } else {    // 공인(Y), 등록(N)
            String[] grades = gradeInfo.split(",");
            for (String grade : grades) {
                log.info("name = {}, agency = {}, grade = {}, authYn = {}", name, agency, grade, authorizedYn);
                certificateMapper.insertPrivate(CertificationInfo.of(privateInfo, grade, authorizedYn));
            }
        }
    }

    private static ArrayList<String> parseGrade(String gradeInfo) {
        if(gradeInfo.contains(" : ")) {
            return combineValues(gradeInfo);
        }
        return splitString(gradeInfo);
    }

    private static ArrayList<String> splitString(String input) {
        ArrayList<String> items = new ArrayList<>();
        Pattern pattern = Pattern.compile("[\\w가-힣\\s]+\\(.*?\\)|[\\w가-힣\\s]+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group().trim();
            if (match.contains("(")) {
                items.addAll(splitParentheses(match));
            } else {
                items.add(match);
            }
        }
        return items;
    }

    private static ArrayList<String> splitParentheses(String input) {
        ArrayList<String> items = new ArrayList<>();
        String[] parts = input.split("\\(");
        String prefix = parts[0];
        String[] arguments = parts[1].replace(")", "").split(", ");
        for (String arg : arguments) {
            items.add(prefix + "(" + arg + ")");
        }
        return items;
    }

    private static ArrayList<String> combineValues(String input) {
        ArrayList<String> combinedValues = new ArrayList<>();
        Pattern pattern = Pattern.compile("(분야|등급|구분)\\s*:\\s*([^:]+)\\s*(?=(?:분야|등급|구분)|$)");
        Matcher matcher = pattern.matcher(input);
        ArrayList<ArrayList<String>> allValues = new ArrayList<>();

        // 필드별 값들을 모두 저장
        while (matcher.find()) {
            String fieldValues = matcher.group(2).trim();
            String[] values = fieldValues.split(", ");
            ArrayList<String> fieldList = new ArrayList<>();
            for (String value : values) {
                fieldList.add(value);
            }
            allValues.add(fieldList);
        }

        // 재귀적으로 모든 경우의 수를 조합하여 출력
        combineValuesRecursive(allValues, 0, new ArrayList<>(), combinedValues);
        return combinedValues;
    }

    private static void combineValuesRecursive(ArrayList<ArrayList<String>> allValues, int currentIndex, ArrayList<String> currentCombination, ArrayList<String> combinedValues) {
        if (currentIndex == allValues.size()) {
            combinedValues.add(String.join(" ", currentCombination));
            return;
        }

        ArrayList<String> valuesForCurrentField = allValues.get(currentIndex);
        for (String value : valuesForCurrentField) {
            ArrayList<String> newCombination = new ArrayList<>(currentCombination);
            newCombination.add(value);
            combineValuesRecursive(allValues, currentIndex + 1, newCombination, combinedValues);
        }
    }
}
