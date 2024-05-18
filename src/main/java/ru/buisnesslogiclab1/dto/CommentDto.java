package ru.buisnesslogiclab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

    private String nickName;
    private String text;

}
