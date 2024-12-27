package com.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class InviteService {
    private final SharedCalenderService sharedCalenderService;

    public InviteService(SharedCalenderService sharedCalenderService) {
        this.sharedCalenderService = sharedCalenderService;
    }

    // 초대로직: 초대링크로 초대 API 호출 -> 초대링크 누른 사용자의 http session에 공유캘린더 식별자를 임시 저장
    // -> 사용자 로그인 -> 로그인 성공 시 http session에 캘린더 식별자가 저장되어 있는지 확인(null이 아닌지)
    // -> 저장되어 있으면 사용자의 유저 식별자와 공유캘린더의 식별자를 조합하여 SharedUser 테이블에 추가 -> 캘린더 참여 -> 새로고침


    public String generateInviteLink(Long calenderId) throws JsonProcessingException {
        try {
            // 해당되는 캘린더 식별자의 캘린더가 존재하지 않으면 null 반환
            if(sharedCalenderService.calInfo(calenderId) == null) {
                return null;
            }

            // JSON 형태의 데이터 생성
            Map<String, String> tokenData = new HashMap<>();
            tokenData.put("calenderId", calenderId.toString());
            tokenData.put("timestamp", String.valueOf(System.currentTimeMillis())); // 토큰 생성 시간

            // JSON 문자열로 변환 후 Base64로 인코딩
            ObjectMapper objectMapper = new ObjectMapper(); // Jackson Library
            String tokenJson = objectMapper.writeValueAsString(tokenData);
            String encodedToken = Base64.getEncoder().encodeToString(tokenJson.getBytes());
            return "http://localhost:61314/invite?token=" + encodedToken;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean clickInvite(String token, HttpSession httpSession) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            ObjectMapper objectMapper = new ObjectMapper();
            // 문자열인 decodedToken을 Map 데이터타입에 매핑한다.
            Map<String, String> tokenData = objectMapper.readValue(decodedToken, Map.class);

            Long calenderId = Long.parseLong(tokenData.get("calenderId"));
            httpSession.setAttribute("calenderId", calenderId); // 세션에 캘린더 아이디를 저장. 사용자 로그인 이후 사용하기 위함

            return true;
        } catch (Exception e) {
            return false;
        }
    }




}