package co.com.bancolombia.kafkaconsumer.dtos;

import co.com.bancolombia.model.exceptions.BusinessException;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.DATA_VALIDATION_ERROR;

@Builder(toBuilder = true)
public record PostingResult(PostingInstructionBatch posting_instruction_batch, List<Balance> balances) {

    public static final String POSTING_PHASE_COMMITTED = "POSTING_PHASE_COMMITTED";
    public static final String CORE_AUTORIZADOR = "CORE_AUTORIZADOR";

    public String requestId() {
        return posting_instruction_batch.create_request_id();
    }

    public ZonedDateTime valueTimestamp() {
        return posting_instruction_batch.value_timestamp();
    }

    public BigDecimal balance() {
        var balances = committedBalance().stream()
                .map(Balance::amount).toList();

        if (balances.size() != 1) {
            throw new BusinessException(DATA_VALIDATION_ERROR, "Posting id:" + requestId()); //Pendiente
        }
        return balances.getFirst();
    }

    public List<Balance> committedBalance() {
        return balances.stream()
                .filter(balance -> POSTING_PHASE_COMMITTED.equals(balance.phase()))
                .toList();
    }

    public String coreAutorizador() {
        return posting_instruction_batch()
                .batch_details()
                .get(CORE_AUTORIZADOR);
    }

    @Builder(toBuilder = true)
    public record PostingInstructionBatch(
            String id,
            String create_request_id,
            String client_id,
            String client_batch_id,
            List<PostingInstruction> posting_instructions,
            Map<String, String> batch_details,
            ZonedDateTime value_timestamp,
            ZonedDateTime booking_timestamp,
            String status,
            String error,
            ZonedDateTime insertion_timestamp,
            ZonedDateTime source_insertion_timestamp,
            Boolean dryRun
    ) {}

    @Builder(toBuilder = true)
    public record PostingInstruction(
            String id,
            String posting_instruction_batch_id,
            String client_transaction_id,
            Transfer transfer,
            Settlement inbound_hard_settlement,
            Settlement outbound_hard_settlement,
            List<Object> pics,
            Map<String, String> instruction_details,
            List<Posting> committed_postings,
            List<Object> posting_violations,
            List<AccountViolations> account_violations,
            List<RestrictionViolation> restriction_violations,
            List<ContractViolations> contract_violations,
            Object override,
            Object transaction_code,
            String booking_localised_date_time) {}

    @Builder(toBuilder = true)
    public record Transfer(
            String amount,
            String denomination,
            AccountId debtor_target_account,
            AccountId creditor_target_account) {}

    @Builder(toBuilder = true)
    public record Settlement(
            String amount,
            String denomination,
            AccountId target_account,
            String internal_account_id,
            Boolean advice,
            String target_account_id,
            String internal_account_processing_label,
            String target_account_address,
            String asset) {}

    public record AccountId(String account_id) {}

    public record AccountViolations(String account_id, String payment_device_token, String type) {}

    public record RestrictionViolation(
            String restriction_set_id, String account_id, String payment_device_id,
            String customer_id, Boolean requires_review, String type) {}

    public record ContractViolations(String account_id, String type, String reason, String violation_type) {}

    @Builder(toBuilder = true)
    public record Posting(
            Boolean credit,
            String amount,
            String denomination,
            String account_id,
            String account_address,
            String asset,
            String phase) {}

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