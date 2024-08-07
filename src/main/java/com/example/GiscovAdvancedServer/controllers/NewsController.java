package com.example.GiscovAdvancedServer.controllers;

import com.example.GiscovAdvancedServer.DTOs.request.NewsRequest;
import com.example.GiscovAdvancedServer.DTOs.response.CreateNewsSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.CustomSuccessResponse;
import com.example.GiscovAdvancedServer.DTOs.response.GetNewsOutResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PageableResponse;
import com.example.GiscovAdvancedServer.DTOs.response.BaseSuccessResponse;
import com.example.GiscovAdvancedServer.constans.Constants;
import com.example.GiscovAdvancedServer.constans.ValidationConstants;
import com.example.GiscovAdvancedServer.services.NewsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/news")
@Validated
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(@Valid @RequestBody NewsRequest request) {
        return ResponseEntity.ok(newsService.createNews(request));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getNews(@RequestParam
                                   @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
                                   @Max(value = 100, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
                                   @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL) Integer page,
                                                                                                     @RequestParam
                                   @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
                                   @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
                                   @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL) Integer perPage) {
        return ResponseEntity.ok(newsService.getNews(page, perPage));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getUserNews(@RequestParam
          @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
          @Max(value = 100, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
          @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL) Integer page,
          @RequestParam
          @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
          @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
          @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL) Integer perPage,
          @PathVariable
          @Pattern(regexp = Constants.REGULAR_UUID, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String id) {
        return ResponseEntity.ok(newsService.getUserNews(page, perPage, UUID.fromString(id)));
    }

    @GetMapping("/find")
    public ResponseEntity<PageableResponse<List<GetNewsOutResponse>>> findNews(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String keywords,
            @RequestParam
            @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.PAGE_SIZE_NOT_VALID)
            @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL) Integer page,
            @RequestParam
            @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
            @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL) Integer perPage,
            @RequestParam(required = false) List<String> tags) {
        return ResponseEntity.ok(newsService.findNews(author, keywords, page, perPage, tags));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> putNews(@PathVariable
                                                       @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE)Long id,
                                                       @Valid @RequestBody NewsRequest request) {
        return ResponseEntity.ok(newsService.putNews(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNews(@PathVariable
                                                          @Positive(message = ValidationConstants.ID_MUST_BE_POSITIVE)Long id) {
        return ResponseEntity.ok(newsService.deleteNews(id));
    }
}
