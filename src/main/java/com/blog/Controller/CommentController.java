package com.blog.Controller;

import com.blog.Payload.CommentDto;
import com.blog.Payload.PostDto;
import com.blog.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/post/{postId}/comment") // http://localhost:8080/api/post/1/comment
    public ResponseEntity<?> createComment(@Valid @PathVariable("postId") long postId,
                                                    @RequestBody CommentDto commentDto, BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto comment = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
    @GetMapping("/post/{postId}/comment") // http://localhost:8080/api/post/1/comment
    public List<CommentDto> getCommentByPostId(@PathVariable long postId){
        List<CommentDto> commentByPostId = commentService.getCommentByPostId(postId);
        return commentByPostId;
    }
    @GetMapping("/post/{postId}/comment/{commentId}")  // http://localhost:8080/api/post/1/comment/1
    public ResponseEntity<CommentDto> getCommentById(@PathVariable long postId,@PathVariable long commentId){
        CommentDto commentById = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentById,HttpStatus.OK);
    }
    @PutMapping("/post/{postId}/comment/{commentId}")  // http://localhost:8080/api/post/1/comment/1
    public ResponseEntity<CommentDto> updateComment(@PathVariable long postId,
                                                    @PathVariable long commentId,
                                                    @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @DeleteMapping("/post/{postId}/comment/{commentId}")  // http://localhost:8080/api/post/1/comment/1
    public ResponseEntity<String> deleteComment(@PathVariable long postId,@PathVariable long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment Deleted Successfully",HttpStatus.OK);
    }

}

