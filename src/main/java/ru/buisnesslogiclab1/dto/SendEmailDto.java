package ru.buisnesslogiclab1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SendEmailDto {

    private String text;

    private String userEmail;

    private String theme;

}
