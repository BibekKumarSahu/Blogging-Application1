package com.blog.Service.Impl;

import com.blog.Entity.Post;
import com.blog.Exception.ResourceNotFound;
import com.blog.Payload.PostDto;
import com.blog.Payload.PostResponse;
import com.blog.Repository.PostRepository;
import com.blog.Service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savePost = postRepository.save(post);
        PostDto dto = mapToDto(savePost);
        return dto;
    }

    private PostDto mapToDto(Post savePost) {
        PostDto dto = modelMapper.map(savePost, PostDto.class);
        return dto;
    }

    private Post mapToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto, Post.class);
        return post;
    }

    @Override
    public PostResponse getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest request = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> postPage = postRepository.findAll(request);
        List<Post> posts = postPage.getContent();
        List<PostDto> collect = posts.stream().map((p) -> mapToDto(p)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(collect);
        postResponse.setPageNumber(postPage.getNumber());
        postResponse.setPageSize(postPage.getSize());
        postResponse.setTotalElement(postPage.getTotalElements());
        postResponse.setTotalPages(postPage.getTotalPages());
        postResponse.setLastPage(postPage.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "Id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePostById(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post savePost = postRepository.save(post);
        return mapToDto(post);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
        postRepository.deleteById(id);
    }
}
