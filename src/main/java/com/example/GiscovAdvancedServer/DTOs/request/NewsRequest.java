package com.example.GiscovAdvancedServer.DTOs.request;

import com.example.GiscovAdvancedServer.constans.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class NewsRequest {

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    private String description;

    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    @NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    @Pattern(regexp = "^\\S+\\.(?:png|jpg|jpeg|gif)$", message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;

    @NotEmpty(message = ValidationConstants.TAGS_NOT_VALID)
    private List<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_TITLE_SIZE)
    @NotBlank(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
    private String title;
}
