package com.github.kmu_wink.seoul_in_culture.domain.bookmark.controller;

import com.github.kmu_wink.seoul_in_culture.common.api.ApiResponse;
import com.github.kmu_wink.seoul_in_culture.common.security.authentication.AuthGuard;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.dto.response.GetBookmarkResponse;
import com.github.kmu_wink.seoul_in_culture.domain.bookmark.service.BookmarkService;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AuthGuard
@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
@Tag(name = "[북마크]")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "북마크 조회")
    public ApiResponse<GetBookmarkResponse> getBookmark(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok(bookmarkService.getBookmark(user));
    }

    @PostMapping("/{eventId}")
    @Operation(summary = "북마크 추가")
    public ApiResponse<Void> postBookmark(
            @AuthenticationPrincipal User user,
            @PathVariable String eventId
    ) {
        bookmarkService.postBookmark(user, eventId);

        return ApiResponse.ok();
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "북마크 삭제")
    public ApiResponse<Void> deleteBookmark(
            @AuthenticationPrincipal User user,
            @PathVariable String eventId
    ) {
        bookmarkService.deleteBookmark(user, eventId);

        return ApiResponse.ok();
    }
}
