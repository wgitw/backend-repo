package com.madeyepeople.pocketpt.domain.account.entity;

import com.madeyepeople.pocketpt.domain.account.dto.request.PurposeUpdateRequest;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE purpose SET is_deleted = true WHERE purpose_id = ?")
public class Purpose extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purpose_id")
    private Long purposeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Nonnull
    private String title;

    private String content;

    private LocalDate targetDate;

    @Builder
    public Purpose(Account account, @Nonnull String title, String content, LocalDate targetDate) {
        this.account = account;
        this.title = title;
        this.content = content;
        this.targetDate = targetDate;
    }

    public void updateByPurposeUpdateRequest(PurposeUpdateRequest purposeUpdateRequest) {
        if (purposeUpdateRequest.getTitle() != null) {
            this.title = purposeUpdateRequest.getTitle();
        }
        if (purposeUpdateRequest.getContent() != null) {
            this.content = purposeUpdateRequest.getContent();
        }
        if (purposeUpdateRequest.getTargetDate() != null) {
            this.targetDate = LocalDate.parse(purposeUpdateRequest.getTargetDate());
        }
    }
}
