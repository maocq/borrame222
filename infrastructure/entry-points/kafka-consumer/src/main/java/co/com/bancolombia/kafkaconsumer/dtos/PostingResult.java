package co.com.bancolombia.kafkaconsumer.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record PostingResult(PostingInstructionBatch posting_instruction_batch, List<Balance> balances) {

    public static final String POSTING_PHASE_COMMITTED = "POSTING_PHASE_COMMITTED";

    public ZonedDateTime insertionTimestamp() {
        return posting_instruction_batch.insertion_timestamp();
    }

    public List<Balance> committedBalance() {
        return balances.stream()
                .filter(balance -> POSTING_PHASE_COMMITTED.equals(balance.phase()))
                .toList();
    }

    @Builder(toBuilder = true)
    public record PostingInstructionBatch(
            String id,
            ZonedDateTime value_timestamp,
            ZonedDateTime booking_timestamp,
            String status,
            String error,
            ZonedDateTime insertion_timestamp
    ) {}

    @Builder(toBuilder = true)
    public record Balance(
            String id,
            String account_id,
            String account_address,
            String phase,
            String asset,
            String denomination,
            String posting_instruction_batch_id,
            ZonedDateTime value_time,
            BigDecimal amount,
            BigDecimal total_debit,
            BigDecimal total_credit) {}
}