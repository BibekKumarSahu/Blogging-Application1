package com.blog.Payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    private long id;
    @NotEmpty
    @Size(min = 4,max = 20,message = "Name  should be 4 - 20 character")
    private String name;
    @NotEmpty
    private String comment;
    @NotNull
    private String send;
}
