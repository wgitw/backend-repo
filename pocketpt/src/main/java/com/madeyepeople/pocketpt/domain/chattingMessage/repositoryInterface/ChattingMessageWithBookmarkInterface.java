package com.madeyepeople.pocketpt.domain.chattingMessage.repositoryInterface;

import java.time.LocalDateTime;

public interface ChattingMessageWithBookmarkInterface {

    Long getChattingRoomId();

    Long getAccountId();

    String getNickname();

    String getProfilePictureUrl();

    Long getChattingMessageId();

    String getContent();

    String getFileUrl();

    Long getIsBookmarked();

    Boolean getIsEdited();

    int getNotViewCount();

    Boolean getIsDeleted();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
