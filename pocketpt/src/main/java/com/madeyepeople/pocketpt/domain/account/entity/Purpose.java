package com.madeyepeople.pocketpt.domain.account.entity;

import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
}
