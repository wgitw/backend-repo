package com.madeyepeople.pocketpt.domain.chattingMessage.entity;

import com.madeyepeople.pocketpt.domain.chattingMessageBookmark.entity.ChattingMessageBookmark;
import com.madeyepeople.pocketpt.domain.chattingParticipant.entity.ChattingParticipant;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@ToString
@Entity(name = "chatting_message")
public class ChattingMessage extends BaseEntity {
    @Id
    @Column(name = "chatting_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
//            @JoinColumn(name = "chattingParticipantId"),
            @JoinColumn(name = "chattingRoom", referencedColumnName = "chatting_room_id"),
            @JoinColumn(name = "account", referencedColumnName = "account_id")
    }, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ChattingParticipant chattingParticipant;

    @OneToMany(mappedBy = "chattingMessage")
    private List<ChattingMessageBookmark> chattingMessageBookmarkList;

    @Column(name = "content")
    private String content;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "is_edited", nullable = false)
    @Builder.Default
    private Boolean isEdited = Boolean.FALSE;

    @Column(name = "not_view_count", nullable = false)
    @Builder.Default
    private int notViewCount = 0;

}
