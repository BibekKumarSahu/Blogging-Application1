package com.blog.Service;

import com.blog.Payload.PostDto;
import com.blog.Payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePostById(long id, PostDto postDto);

    void deletePostById(long id);
}
