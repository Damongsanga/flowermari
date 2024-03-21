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

        //클라이언트와 연결 종료 시 이벤트 핸들러
        emitter.onCompletion(() -> emitters.remove(requestId));
        emitter.onTimeout(() -> emitters.remove(requestId));
        emitter.onError((e) -> emitters.remove(requestId));

        return emitter;
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
                emitter.send(SseEmitter.event().name("reGenerateEvent").data(regeneratedto));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // sse를 통해 연결되어 있는 클라이언트에게 메세지를 보냄.

}
