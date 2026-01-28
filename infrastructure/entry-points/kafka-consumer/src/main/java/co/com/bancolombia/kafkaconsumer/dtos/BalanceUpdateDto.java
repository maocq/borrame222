package co.com.bancolombia.kafkaconsumer.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record BalanceUpdateDto(
        String id,
        String TIPO_CUENTA,
        String NUMERO_DE_CUENTA,
        BigDecimal RSLDEFE,
        BigDecimal RSLDCXC,
        BigDecimal VALOR_EMBARGADO,
        BigDecimal RSLDBLS,
        String NOMBRE_ESTADO,
        String CODIGO_PLAN,
        String RELACION_CLIENTE_CUENTA,
        String FLAGS_CUENTA,
        @JsonFormat(pattern = "yyyy-MM-dd-HH.mm.ss.SSSSSS")
        LocalDateTime UPDATE_TIMESTAMP
) {
    public static final String SAVING_ACCOUNT = "S";
    public static final String CHECKING_ACCOUNT = "D";
    public static final int SIZE = 11;

    public String getAccount() {
        return TIPO_CUENTA + StringUtils.leftPad(NUMERO_DE_CUENTA, SIZE, '0');
    }

    public BigDecimal getAmount() {
        var balance = RSLDEFE.subtract(RSLDCXC).subtract(VALOR_EMBARGADO);
        return TIPO_CUENTA.equals(CHECKING_ACCOUNT) ? balance.subtract(RSLDBLS) : balance;
    }
}
