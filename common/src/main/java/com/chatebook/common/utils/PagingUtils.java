package com.chatebook.common.utils;

import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public final class PagingUtils {

  private PagingUtils() {}

  public static Pageable makePageRequestWithCamelCase(String sortBy, String order, int page, int paging) {
    String sortField = CaseUtils.toCamelCase(sortBy, false, '_');
    Sort sort =
        CommonConstant.ASC.equalsIgnoreCase(order)
            ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();
    return PageRequest.of(page - 1, paging, sort);
  }

  public static Pageable makePageRequestWithSnakeCase(
      String sortBy, String order, int page, int paging) {
    String sortField = CommonFunction.convertToSnakeCase(sortBy);
    Sort sort =
        CommonConstant.ASC.equalsIgnoreCase(order)
            ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();
    return PageRequest.of(page - 1, paging, sort);
  }
}
