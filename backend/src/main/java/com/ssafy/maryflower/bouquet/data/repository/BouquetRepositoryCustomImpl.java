package com.ssafy.maryflower.bouquet.data.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.maryflower.bouquet.data.BouquetOrderBy;
import com.ssafy.maryflower.bouquet.data.SearchType;
import com.ssafy.maryflower.bouquet.data.dto.request.BouquetListRequestDto;
import com.ssafy.maryflower.bouquet.data.dto.response.BouquetFlowerResponseDto;
import com.ssafy.maryflower.bouquet.data.entity.Bouquet;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.maryflower.bouquet.data.entity.QBouquet.bouquet;
import static com.ssafy.maryflower.bouquet.data.entity.QFlower.flower;
import static com.ssafy.maryflower.bouquet.data.entity.QFlowerBouquet.flowerBouquet;
import static com.ssafy.maryflower.member.data.entity.QMember.member;
import static com.ssafy.maryflower.bouquet.data.entity.QMemberBouquet.memberBouquet;

@RequiredArgsConstructor
@Repository
public class BouquetRepositoryCustomImpl implements BouquetRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<BouquetFlowerResponseDto> searchRelevantBouquet(BouquetListRequestDto req, Pageable pageable) {
    int pageSize = pageable.getPageSize();
    JPAQuery<BouquetFlowerResponseDto> query = jpaQueryFactory.select(
            Projections.constructor(
                BouquetFlowerResponseDto.class,
                member.memberId,
                bouquet.bouquetId,
                bouquet.imageUrl,
                flowerBouquet.flower
            ) //dto로 수정,
        )
        .from(bouquet)
        .leftJoin(bouquet.member, member)
        .leftJoin(bouquet.flowerBouquets, flowerBouquet)
        .leftJoin(flowerBouquet.flower, flower).distinct()
        .where(
            eqKeyword(req)
        );


    switch(req.getOrderBy()){
      case LIKE -> query.leftJoin(memberBouquet)
            .on(bouquet.bouquetId.eq(memberBouquet.bouquet.bouquetId))
            .groupBy(bouquet.bouquetId)
            .orderBy(memberBouquet.bouquet.count().desc(), bouquet.createDateTime.desc());
      case RECENT -> query.orderBy(bouquet.createDateTime.desc());
      }

    List<BouquetFlowerResponseDto> content = query.offset(pageable.getOffset())
        .limit(pageSize + 1)
        .fetch();

    boolean hasNext = false;
    if (content.size() > pageSize) {
      content.remove(pageSize);
      hasNext = true;
    }

    return new SliceImpl(content, pageable, hasNext);
  }

  private BooleanExpression eqKeyword(BouquetListRequestDto req) {
    switch(req.getType()){
      case NAME:
        return flower.koreanName.contains(req.getSearchKeyword());
      case MEANING:
        return flower.meaning.contains(req.getSearchKeyword());
      case TEXT:
        return bouquet.whom.contains(req.getSearchKeyword())
          .or(bouquet.situation.contains(req.getSearchKeyword()))
          .or(bouquet.message.contains(req.getSearchKeyword()));
    }
    return null;
  }

}
