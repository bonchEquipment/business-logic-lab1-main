package ru.buisnesslogiclab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationStatusDto {


    private boolean operationSucceed;

    private String info;


}
