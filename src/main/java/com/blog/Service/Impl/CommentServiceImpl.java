package com.blog.Service.Impl;

import com.blog.Entity.Comment;
import com.blog.Entity.Post;
import com.blog.Exception.BlogAPIException;
import com.blog.Exception.ResourceNotFound;
import com.blog.Payload.CommentDto;
import com.blog.Repository.CommentRepository;
import com.blog.Repository.PostRepository;
import com.blog.Service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "Id", postId));
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment saveComment = commentRepository.save(comment);
        CommentDto dto = mapToDto(saveComment);
        return dto;
    }


    private CommentDto mapToDto(Comment saveComment) {
        CommentDto commentDto = modelMapper.map(saveComment, CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> list = commentRepository.findByPostId(postId);
        List<CommentDto> collect = list.stream().map((l) -> mapToDto(l)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "Id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comment", "Id", commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment Does not belong the post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "PostId", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comment", "CommentId", commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment doesn't belongs the post");
        }
        comment.setName(commentDto.getName());
        comment.setComment(commentDto.getComment());
        comment.setSend(commentDto.getSend());
        Comment UpdateComment = commentRepository.save(comment);
        return mapToDto(UpdateComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "PostId", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comment", "CommentId", commentId));
        if (comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment Doesn't Belongs to the post");
        }
        commentRepository.deleteById(commentId);
    }
}




