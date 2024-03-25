package com.ssafy.maryflower.bouquet.data.dto.request;

import com.ssafy.maryflower.bouquet.data.dto.transfer.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
// gpt api 에게 보낼 요청을 담을 dto
@Data
public class ChatGPTRequest {

    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model, String prompt){
        this.model=model;
        this.messages=new ArrayList<>();
        this.messages.add(new Message("user",prompt));
    }
}
