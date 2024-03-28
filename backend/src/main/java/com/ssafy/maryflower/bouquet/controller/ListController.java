package com.ssafy.maryflower.bouquet.controller;

import com.ssafy.maryflower.bouquet.data.dto.request.BouquetListRequestDto;
import com.ssafy.maryflower.bouquet.data.dto.response.BouquetFlowerResponseDto;
import com.ssafy.maryflower.bouquet.service.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bouquet")
@RequiredArgsConstructor
@Slf4j
public class ListController {

  private final ListService listService;

  @GetMapping("/list")
  public ResponseEntity<Slice<BouquetFlowerResponseDto>> search(@RequestBody BouquetListRequestDto req, @PageableDefault(size = 4) Pageable pageable) {
    Slice<BouquetFlowerResponseDto> res = listService.search(req, pageable);
    return ResponseEntity.ok(res);
  }

}
