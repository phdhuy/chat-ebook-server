package com.chatebook.chat.mapper;

import com.chatebook.chat.model.CitedExcerpt;
import com.chatebook.chat.payload.response.CitedExcerptInfoResponse;
import com.chatebook.common.config.SpringMapStructConfig;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface CitedExcerptMapper {

  List<CitedExcerptInfoResponse> toCitedExcerptInfoResponseList(List<CitedExcerpt> entities);
}
