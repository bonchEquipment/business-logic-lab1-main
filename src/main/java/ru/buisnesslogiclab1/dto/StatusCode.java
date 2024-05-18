package ru.buisnesslogiclab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class StatusCode {

    public static final StatusCode OK = new StatusCode("Ok", 200, "BLPS.0000");
    public static final StatusCode NO_CONTENT = new StatusCode("No content", 200, "BLPS.0001");
    public static final StatusCode CONTENT_IS_NOT_AVAILABLE = new StatusCode("Content is not available", 200, "BLPS.0002");
    public static final StatusCode THERE_IS_NO_SUCH_VIDEO = new StatusCode("There is no video with such id", 404, "BLPS.0003");
    public static final StatusCode THERE_IS_NO_SUCH_USER = new StatusCode("There is no USER/ADMIN/SUBSCRIBER with such id", 200, "BLPS.0003");
    public static final StatusCode VIDEO_DOES_NOT_SENT_FOR_APPROVAL = new StatusCode("Video does not sent for approval", 200, "BLPS.0004");
    public static final StatusCode VIDEO_IS_ALREADY_APPROVED = new StatusCode("Video is already approved", 200, "BLPS.0005");
    public static final StatusCode VIDEO_IS_ALREADY_REJECTED = new StatusCode("Video is already rejected", 200, "BLPS.0006");
    public static final StatusCode INTERNAL_ERROR = new StatusCode("Some internal error happened", 500, "BLPS.0500");


    private final String description;
    private final int httpCode;
    private final String businessCode;


    public static StatusCode createConstraintViolationCode(String constraint){
        return new StatusCode(constraint, 200, "BLPS.0031");
    }

}
