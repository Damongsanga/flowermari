package com.ssafy.maryflower.bouquet.data.dto.request;

import com.ssafy.maryflower.bouquet.data.BouquetOrderBy;
import com.ssafy.maryflower.bouquet.data.SearchType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BouquetListRequestDto {
  private SearchType type;
  private String searchKeyword;
  private BouquetOrderBy orderBy;

  @Builder
  public BouquetListRequestDto(SearchType type, String searchKeyword, BouquetOrderBy orderBy) {
    this.type = type;
    this.searchKeyword = searchKeyword;
    this.orderBy = orderBy;
  }

}
