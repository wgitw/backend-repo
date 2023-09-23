package com.madeyepeople.pocketpt.domain.historicalData.entity;

import com.madeyepeople.pocketpt.domain.account.entity.Account;
import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.constant.ScopeEnumConverter;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import com.madeyepeople.pocketpt.global.common.CommonFunction;
import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;
import jakarta.persistence.*;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "historical_data")
@Access(value = AccessType.FIELD)
//@SQLDelete(sql = "UPDATE historical_data SET is_deleted = true WHERE historical_data_id = ?")
//@Where(clause = "is_deleted = false")
public class HistoricalData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historical_data_id")
    private Long historicalDataId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany
    @JoinColumn(name = "historical_data_id")
    private List<HistoricalDataFile> historicalDataFileList;

    @Column(name = "date")
    private Date date;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    // HistoricalData의 scope: 사진 영역까지 포함해서 한 번에 영역설정
    // public: 전체 공개
        // public-public: 전체 공개
        // public-private: 공개하지 않음 - 일부공개와 동일한 의미
            // 사진별로 공개할지 말지 설정하는 의미
    // private: 공개하지 않음
        // private-public: 불가능
        // private-private: 자동 설정
    @Convert(converter = ScopeEnumConverter.class)
    @Column(name = "scope")
    private Scope scope;

    public HistoricalData updateTitleAndDescriptionAndScope(Date date, String title, String description, String scope) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.scope = Scope.valueOf(scope);
        return this;
    }

}
