package com.springboot_aws.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postRepository;

    @AfterEach // 단위테스트가 끝날때마다 수행되는 메소드, 데이터 간 데이터침범을 막기위해사용
    public void cleanUp(){
        postRepository.deleteAll();
    }

    @Test
    public void 게시글_저장불러오기(){
        //given
        String title = "테스트 제목";
        String content = "테스트 내용";

        postRepository.save(Posts.builder() //post에 insert,update쿼리를 실행 (id가 있으면 update, 없으면 insert)
                .title(title)
                .content(content)
                .author("me")
                .build());
        // when
        List<Posts> postsList = postRepository.findAll(); // 테이블의 모든 데이터 조회

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }

    @Test
    public void BaseTimeEntity_등록() {
        // given
        LocalDateTime now = LocalDateTime.of(2022,1,4,0,0,0,0);
        postRepository.save(Posts.builder()
                .title("BaseTimeEntity title")
                .content("BaseTimeEntity content")
                .author("BaseTimeEntity author")
                .build());

        //when
        List<Posts> postsList = postRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>> createDate = "+posts.getCreatedDate()+", modifiedDate = "+posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

    }
}