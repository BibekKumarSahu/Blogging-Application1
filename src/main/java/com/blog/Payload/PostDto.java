package com.blog.Payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostDto {

    private long id;
    @NotNull
    @Size(min = 5,max = 40,message = "Title should be min 5 to 40 character")
    private String title;
    @NotNull
    private String description;
    @NotEmpty
    private String content;

}
