package com.agroerp.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class ReportDtos {
    private ReportDtos() {
    }

    public record ReportMetricDto(String label, BigDecimal value, String format) {
    }

    public record ReportColumnDto(String key, String label, String type) {
    }

    public record ReportTableDto(String title, List<ReportColumnDto> columns,
                                 List<Map<String, Object>> rows, Map<String, Object> totals) {
    }

    public record ReportResponseDto(String reportKey, String title, String periodLabel,
                                    LocalDate fromDate, LocalDate toDate, Instant generatedAt,
                                    List<ReportMetricDto> metrics, List<ReportTableDto> tables) {
    }
}
