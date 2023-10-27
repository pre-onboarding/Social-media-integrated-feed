package com.wanted.socialMediaIntegratedFeed.global.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.io.*;

/**
 * 이메일과 비밀번호를 이용한 로그인 시 HTTP 요청의 Body에 있는 이메일과 패스워드를 추출하여 유효성 검사를 수행하고, Token으로 만들어 반환합니다.
 * @author 정성국
 */
public class EmailPasswordAuthenticationConverter implements AuthenticationConverter {

    private static final String EMAIL_PROPERTY = "email";

    private static final String PASSWORD_PROPERTY = "password";

    /**
     * HTTP 요청에 포함된 이메일과 패스워드를 추출하고, {@link UsernamePasswordAuthenticationToken} 에 담아 반환합니다.
     * @param request {@link HttpServletRequest} 객체에 저장된 HTTP 요청 정보
     * @return {@link UsernamePasswordAuthenticationToken}
     * @author 정성국
     */
    @Override
    public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
        var loginUserInfo = getLoginUserInfoFromBody(getBody(request));
        if (isValidUserInfo(loginUserInfo))
            return UsernamePasswordAuthenticationToken.unauthenticated(loginUserInfo.email(), loginUserInfo.password());
        else
            return null;
    }

    /**
     * {@link HttpServletRequest} 객체에 저장된 HTTP 요청 정보를 문자열(String) 형식으로 변환하여 반환합니다.
     * @param request {@link HttpServletRequest} 객체에 저장된 HTTP 요청 정보
     * @return HTTP 요청을 문자열(String) 형식으로 반환
     * @author 정성국
     */
    private String getBody(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder();
        try (var bufferedReader = new BufferedReader(new InputStreamReader(copyInputStream(request)))) {
            bufferedReader.lines().forEach(stringBuilder::append);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return stringBuilder.toString();
    }

    /**
     * {@link HttpServletRequest} 객체에 저장된 HTTP 요청 정보를 문자열(String) 형식으로 변환하기위한 사전 작업으로,
     * {@link ByteArrayInputStream} 으로 변환하여 반환합니다.
     * @param request {@link HttpServletRequest} 객체에 저장된 HTTP 요청 정보
     * @return {@link ByteArrayInputStream}
     * @author 정성국
     */
    private ByteArrayInputStream copyInputStream(HttpServletRequest request) {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            request.getInputStream().transferTo(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * 문자열(String) 형태로 표현된 HTTP 요청의 body에서 JSON 형식을 추출하여 {@link JsonNode} 형식으로 변환하고, 이메일과 비밀번호를 추출합니다.
     * 그리고 {@link LoginMemberInfo}에 정보를 저장하여 반환합니다.
     * @param body 문자열 형태로 표현된 HTTP 요청의 body
     * @return {@link LoginMemberInfo}
     * @author 정성국
     */
    private LoginMemberInfo getLoginUserInfoFromBody(String body) {
        JsonNode jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String email = jsonNode.get(EMAIL_PROPERTY).asText();
        String password = jsonNode.get(PASSWORD_PROPERTY).asText();
        return new LoginMemberInfo(email, password);
    }

    /**
     * 사용자가 입력한 이메일과 비밀번호를 저장하는 클래스 입니다.
     * @param email 이메일
     * @param password 비밀번호
     * @author 정성국
     */
    private record LoginMemberInfo(String email, String password) {}

    /**
     * 이메일과 비밀번호의 유효성을 검사한 결과를 반환합니다.
     * @param memberInfo 사용자가 입력한 이메일과 비밀번호 정보
     * @return 유효성 검사 통과 여부
     * @author 정성국
     */
    private boolean isValidUserInfo(LoginMemberInfo memberInfo) {
        int passwordLength = memberInfo.password.length();
        return memberInfo.email().matches("^[^@]*@[^@]*$") && passwordLength >= 10 && passwordLength <= 64;
    }
}
