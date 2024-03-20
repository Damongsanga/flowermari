package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.dto.response.firstGenerateDto;
import com.ssafy.maryflower.bouquet.data.dto.transfer.BouquetUrlTransferDto;
import com.ssafy.maryflower.bouquet.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DataSubscribeService {
    private final SseEmitters sseEmitters;
    private final  CacheService cacheService;
    public void subscribeBouquetDataFromAIServer(BouquetUrlTransferDto bouquetUrlTransferDto){

        firstGenerateDto firstgeneratedto = cacheService.cachefirstGenerateDto(bouquetUrlTransferDto.getRequestId());
        firstgeneratedto.setBouquetUrl(bouquetUrlTransferDto.getBouquetUrl());
        sseEmitters.sendfirstGenerateDtoToClient(bouquetUrlTransferDto.getRequestId(),firstgeneratedto);
    }

}
