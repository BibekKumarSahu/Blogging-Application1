package com.blog.Controller;

import com.blog.Payload.PostDto;
import com.blog.Payload.PostResponse;
import com.blog.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/") // http://localhost:8080/api/
public class PostController {

    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    // http://localhost:8080/api/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult  bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto post = postService.createPost(postDto);
        return new ResponseEntity<>(post,HttpStatus.CREATED);
    }

    @GetMapping  // http://localhost:8080/api/?pageNo=0&pageSize=5&sortBy=id&sortDir=asc
    public PostResponse getAllPost(@RequestParam(value = "pageNo",defaultValue = "0",required = false) int pageNo,
                                   @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
                                   @RequestParam(value = "sortBy",defaultValue = "id",required = false) String sortBy,
                                   @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir){
        PostResponse allPost = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
        return allPost;
    }
    @GetMapping("/{id}") // http://localhost:8080/api/1
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto postById = postService.getPostById(id);
        return new ResponseEntity<>(postById,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") // http://localhost:8080/api/1
    public ResponseEntity<PostDto> updatePostById(@PathVariable(value = "id") long id,@RequestBody PostDto postDto){
        PostDto postDto1 = postService.updatePostById(id, postDto);
        return new ResponseEntity<>(postDto1,HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") // http://localhost:8080/api/1
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post is Deleted Successfully !!!", HttpStatus.OK);
    }


}
