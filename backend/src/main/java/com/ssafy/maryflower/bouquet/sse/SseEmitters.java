package com.ssafy.maryflower.bouquet.sse;

import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.response.reGenerateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitters {

    // requestId를 키로 하고, SseEmitter를 값으로 하는 Map
    private final Map<String, SseEmitter> emitters=new ConcurrentHashMap<>();

    // 클라이언트와 연결 추가.
    public SseEmitter addEmitter(String requestId){
        SseEmitter emitter=new SseEmitter(Long.MAX_VALUE);
        emitters.put(requestId, emitter);

        System.out.println("sse 연결 수 : "+emitters.size());
        //클라이언트와 연결 종료 시 이벤트 핸들러
        emitter.onCompletion(() -> emitters.remove(requestId));
        emitter.onTimeout(() -> emitters.remove(requestId));
        emitter.onError((e) -> emitters.remove(requestId));

        return emitter;
    }
    
    // 모든 연결 제거
    public void removeAllEmitter(){
        emitters.clear();
    }
    // 연결을 제거
    public void removeEmitter(String requestId) {
        emitters.remove(requestId);
    }

    // requestId에 따라 해당 클라이언트에게 메세지 전송.
    public void sendGenerateDtoToClient(String requestId,firstGenerateDto firstGenerateDto){
        SseEmitter emitter= emitters.get(requestId);

        if(emitter !=null){
            try{
                if(firstGenerateDto.getUsedFlower().size()!=firstGenerateDto.getRecommendByMeaning().size()){
                    for(int i=0;i<firstGenerateDto.getUsedFlower().size()-firstGenerateDto.getRecommendByMeaning().size();i++){
                        firstGenerateDto.getRecommendByMeaning().add(Long.valueOf(i+2));
                    }
                }
                System.out.println("--------------------firstGenerate-------------------");
                System.out.println("url : "+firstGenerateDto.getBouquetUrl());
                System.out.println("Api 호출 횟수 : "+firstGenerateDto.getApiUsageCount());
                System.out.println("AllFLower size = " +firstGenerateDto.getAllFlowers().size());
                System.out.println("UsedFlower size : "+firstGenerateDto.getUsedFlower().size());
                System.out.println("Recommended Flower Size : "+firstGenerateDto.getRecommendByMeaning());
                System.out.println("Recommended By Popularity : "+firstGenerateDto.getRecommendByPopularity());
                emitter.send(SseEmitter.event().name("firstGenerateEvent").data(firstGenerateDto));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendGenerateDtoToClient(String requestId, reGenerateDto regeneratedto){
        SseEmitter emitter= emitters.get(requestId);
        if(emitter !=null){
            try{
                System.out.println("--------------------regeneratedto-------------------");
                System.out.println("url : "+regeneratedto.getBouquetUrl());
                System.out.println("Api 호출 횟수 : "+regeneratedto.getApiUsageCount());
                System.out.println("UsedFlower size : "+regeneratedto.getUsedFlower().size());
                System.out.println("Recommended Flower Size : "+regeneratedto.getRecommendByMeaning());
                System.out.println("Recommended By Popularity : "+regeneratedto.getRecommendByPopularity());
                emitter.send(SseEmitter.event().name("reGenerateEvent").data(regeneratedto));


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendImageUrlToClient(String requestId, String ImageUrl)  {

        try{
            System.out.println("-------------------------middleImageSendEvent---------------------");
            System.out.println(ImageUrl);
            SseEmitter emitter= emitters.get(requestId);
            emitter.send(SseEmitter.event().name("middleImageSendEvent").data(ImageUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
