package com.ssafy.maryflower.bouquet.service;

import com.ssafy.maryflower.bouquet.data.entitiy.Flower;
import com.ssafy.maryflower.bouquet.data.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

@RequiredArgsConstructor
public class FlwoerDataLoader implements CommandLineRunner {

    private final FlowerRepository flowerRepository;
    @Override
    public void run(String... args) throws Exception {
        loadFlowerData();
    }

//    private void loadFlowerData(){
//        flowerRepository.save(new Flower(1L,"빨강 장미", "Red Rose", "red", "사랑, 아름다움, 열정", "imageUrl1"));
//        flowerRepository.save(new Flower("하양 장미", "White Rose", "white", "존경,순결,순진,매력", "imageUrl2"));
//        flowerRepository.save(new Flower("분홍 장미", "Pink Rose", "pink", "맹세, 단순, 행복한사랑", "imageUrl3"));
//        flowerRepository.save(new Flower("빨강 튤립", "Red Tulip", "red", "사랑의 고백, 신중함", "imageUrl4"));
//        flowerRepository.save(new Flower("노랑 튤립", "Yellow Tulip", "yellow", "희망, 생명력", "imageUrl5"));
//        flowerRepository.save(new Flower("보라 튤립", "Purple Tulip", "purple", "영원한 사랑, 영원하지 않은 사랑", "imageUrl6"));
//        flowerRepository.save(new Flower("안개꽃", "White Gypsophila", "white", "맑은 마음, 순수한 사랑, 미지의 사랑", "imageUrl7"));
//        flowerRepository.save(new Flower("아이리스", "Iris", "purple", "지혜, 자신감, 아름다움", "imageUrl8"));
//        flowerRepository.save(new Flower("백합", "White Lily", "white", "순결, 우아함", "imageUrl9"));
//        flowerRepository.save(new Flower("빨강 카네이션", "Red Carnation", "red", "부모를 향한 사랑, 감사함, 건강 기원", "imageUrl10"));
//        flowerRepository.save(new Flower("분홍 카네이션", "Pink Carnation", "pink", "사랑의 고백, 감사하는 마음", "imageUrl11"));
//        flowerRepository.save(new Flower("수국", "White Hydrangea", "white", "변덕, 진실된 마음", "imageUrl12"));
//        flowerRepository.save(new Flower("거베라", "Pink Gerbera", "pink", "감사, 숭고한 아름다움", "imageUrl13"));
//        flowerRepository.save(new Flower("라벤더", "Purple Lavender", "purple", "사랑, 행운", "imageUrl14"));
//        flowerRepository.save(new Flower("히아신스", "Pink Hyacinth", "pink", "사랑의 기쁨", "imageUrl15"));
//        flowerRepository.save(new Flower("해바라기", "Yellow Sunflower", "yellow", "활력, 인내", "imageUrl16"));
//
)
    }
}
