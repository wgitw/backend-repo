package com.madeyepeople.pocketpt.domain.historicalData.entity;

import com.madeyepeople.pocketpt.domain.historicalData.constant.Scope;
import com.madeyepeople.pocketpt.domain.historicalData.constant.ScopeEnumConverter;
import com.madeyepeople.pocketpt.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@ToString
@Entity(name = "historical_data_file")
@Access(value = AccessType.FIELD)
//@SQLDelete(sql = "UPDATE historical_data_file SET is_deleted = true WHERE historical_data_file_id = ?")
//@Where(clause = "is_deleted = false")
//@OnDelete(action = OnDeleteAction.CASCADE) // 자식 테이블에서의 실제 삭제 방지
public class HistoricalDataFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historical_data_file_id")
    Long historicalDataFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historical_data_id", nullable = false)
    private HistoricalData historicalData;

    @Column(name = "file_url")
    private String fileUrl;

    @Convert(converter = ScopeEnumConverter.class)
    @Column(name = "scope")
    private Scope scope;

    public HistoricalDataFile updateScope(String scope) {
        this.scope = Scope.valueOf(scope);
        return this;
    }

}
