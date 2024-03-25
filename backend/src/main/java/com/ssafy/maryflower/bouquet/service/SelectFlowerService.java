package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.request.ChatGPTRequest;
import com.ssafy.maryflower.bouquet.data.dto.response.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
@RequiredArgsConstructor
@Service
public class SelectFlowerService {

    @Value("${chatgpt.api.model}")
    private String model;

    @Value("${chatgpt.api.endpoint}")
    private String apiURL;

    private final RestTemplate template;

    public String chat(String prompt){
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    public String makePrompt(String whom,String situation,String message){
        String prompt="\"프롬프트\" : 누구에게 : "+whom+" 어떤 상황 : "+situation+ " 어떤 메시지 : " + message +" 꽃말을 통해 조합하고 싶어.\n" +
                "\n" +
                "| 빨강 장미 |  |  | 사랑, 아름다움, 열정 |\n" +
                "| --- | --- | --- | --- |\n" +
                "| 하양 장미 |  |  | 존경,순결,순진,매력 |\n" +
                "| 분홍 장미 |  |  | 맹세,단순,행복한사랑 |\n" +
                "| 빨강 튤립 |  |  | 사랑의 고백, 신중함 |\n" +
                "| 노랑 튤립 |  |  | 희망, 생명력 |\n" +
                "| 보라 튤립 |  |  | 영원한 사랑, 영원하지 않은 사랑 |\n" +
                "| 안개꽃 |  |  | 맑은 마음, 순수한 사랑, 미지의 사랑 |\n" +
                "| 아이리스 |  |  | 지혜, 자신감, 아름다움 |\n" +
                "| 백합 |  |  | 순결, 우아함 |\n" +
                "| 빨강 카네이션 |  |  | 부모를 향한 사랑, 감사함, 건강 기원 |\n" +
                "| 분홍 카네이션 |  |  | 사랑의 고백, 감사하는 마음 |\n" +
                "| 수국 |  |  | 변덕, 진실된 마음 |\n" +
                "| 거베라 |  |  | 감사, 숭고한 아름다움 |\n" +
                "| 라벤더 |  |  | 사랑, 행운 |\n" +
                "| 히아신스 |  |  | 사랑의 기쁨 |\n" +
                "| 해바라기 |  |  | 활력, 인내 |\n" +
                "\n" +
                "위 꽃들 중에 \"프롬프트\" 조건에 맞는 꽃을 3가지 제공해줘\n" +
                "그리고 위 꽃 중에서 먼저 뽑은 꽃이랑 가장 꽃말이 비슷한 거 하나 씩 뽑아줘\n" +
                "6개의 모든 꽃은 위 리스트 내에서 뽑아야 해. 그리고 겹치는 게 있으면 안돼\n" +
                "추천할 데이터 : 꽃1,꽃1과 비슷한 꽃말의 꽃,꽃2,꽃2과 비슷한 꽃말의 꽃,꽃3,꽃3과 비슷한 꽃말의 꽃\n + " +
                "응답형식 -> 꽃1의이름,(꽃1과 비슷한 꽃말의 꽃)의이름,꽃2의이름,(꽃2과 비슷한 꽃말의 꽃)의이름,꽃3의이름,(꽃3과 비슷한 꽃말의 꽃)의이름 ";
        return prompt;
    }

    public String makePrompt(List<String> flowers){
        String flowerText="";
        int size=flowers.size();
        for(String flower:flowers){
            flowerText+=flower+" ";
        }
        String prompt= "| 빨강 장미 |  |  | 사랑, 아름다움, 열정 |\n" +
                "| --- | --- | --- | --- |\n" +
                "| 하양 장미 |  |  | 존경,순결,순진,매력 |\n" +
                "| 분홍 장미 |  |  | 맹세,단순,행복한사랑 |\n" +
                "| 빨강 튤립 |  |  | 사랑의 고백, 신중함 |\n" +
                "| 노랑 튤립 |  |  | 희망, 생명력 |\n" +
                "| 보라 튤립 |  |  | 영원한 사랑, 영원하지 않은 사랑 |\n" +
                "| 안개꽃 |  |  | 맑은 마음, 순수한 사랑, 미지의 사랑 |\n" +
                "| 아이리스 |  |  | 지혜, 자신감, 아름다움 |\n" +
                "| 백합 |  |  | 순결, 우아함 |\n" +
                "| 빨강 카네이션 |  |  | 부모를 향한 사랑, 감사함, 건강 기원 |\n" +
                "| 분홍 카네이션 |  |  | 사랑의 고백, 감사하는 마음 |\n" +
                "| 수국 |  |  | 변덕, 진실된 마음 |\n" +
                "| 거베라 |  |  | 감사, 숭고한 아름다움 |\n" +
                "| 라벤더 |  |  | 사랑, 행운 |\n" +
                "| 히아신스 |  |  | 사랑의 기쁨 |\n" +
                "| 해바라기 |  |  | 활력, 인내 |\n" +
                "\n" +
                "위 꽃 들중에서 "+flowerText+" 와 꽃말이 비슷한 꽃 하나씩 추천해줘\n"+
                "내가 준 꽃의 개수는 "+size+ " 개이니까 너가 "+size+ " 개 추천해주면 돼\n"+
                "이때 "+flowerText+"에 있는 꽃들은 제외하고 추천해줘\n"+
                "응답형식은 꽃의 이름을 , 기준으로 나눠줘\n"+
                "위 응답 형식에 맞게 꽃 "+size+"개 추천해줘 추천 꽃 개수 꼭 지켜줘";
//        System.out.println(prompt);
         return prompt;
    }





}
