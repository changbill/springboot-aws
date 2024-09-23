package com.springboot_aws.service.posts;

import com.springboot_aws.domain.posts.Posts;
import com.springboot_aws.domain.posts.PostsRepository;
import com.springboot_aws.web.dto.PostsListResponseDto;
import com.springboot_aws.web.dto.PostsResponseDto;
import com.springboot_aws.web.dto.PostsSaveRequestDto;
import com.springboot_aws.web.dto.PostsUpdateRequestDto;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostsService {
    private final PostsRepository postsRepository;

    public Long save (PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllByOrderByIdDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id ){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. = " + id));
        // 존재하는 Posts인지 확인 후 삭제
        postsRepository.delete(posts);
    }
}
