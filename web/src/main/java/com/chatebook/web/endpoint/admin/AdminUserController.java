package com.chatebook.web.endpoint.admin;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.common.utils.PagingUtils;
import com.chatebook.security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin User APIs")
public class AdminUserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseDataAPI getListUser(
      @RequestParam(name = "sort", defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging) {
    Pageable pageable = PagingUtils.makePageRequestWithCamelCase(sortBy, order, page, paging);
    return userService.getListUserByAdmin(pageable);
  }
}
