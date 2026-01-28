package co.com.bancolombia.kafkaconsumer.dtos.filter;

import co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

import static co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto.CHECKING_ACCOUNT;
import static co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto.SAVING_ACCOUNT;

@Service
public class AccountFilter {
    public static final Set<String> VALID_ACCOUNT_TYPES = Set.of(SAVING_ACCOUNT, CHECKING_ACCOUNT);
    public static final int MARK_LOCATION = 17;
    public static final char YES = 'Y';
    public static final String OWNER = "T";

    private final Predicate<BalanceUpdateDto> isInvalidAccountType;
    private final Predicate<BalanceUpdateDto> isInvalidStatus;
    private final Predicate<BalanceUpdateDto> isInvalidSavingAccount;
    private final Predicate<BalanceUpdateDto> isInvalidCheckingAccount;
    private final Predicate<BalanceUpdateDto> isMasterAccount;
    private final Predicate<BalanceUpdateDto> isNotOwner;

    public AccountFilter(AccountFilterConfig filter) {
        isInvalidAccountType = account -> !VALID_ACCOUNT_TYPES.contains(account.TIPO_CUENTA());
        isInvalidStatus = account -> filter.excludedStatus().equals(account.NOMBRE_ESTADO());
        isInvalidSavingAccount = account -> SAVING_ACCOUNT.equals(account.TIPO_CUENTA())
                && filter.excludedSavingsAccounts().contains(account.CODIGO_PLAN());
        isInvalidCheckingAccount = account -> CHECKING_ACCOUNT.equals(account.TIPO_CUENTA())
                && filter.excludedCheckingAccounts().contains(account.CODIGO_PLAN());
        isMasterAccount = account -> account.FLAGS_CUENTA().length() >= MARK_LOCATION
                && YES == account.FLAGS_CUENTA().charAt(MARK_LOCATION - 1);
        isNotOwner = account -> !account.RELACION_CLIENTE_CUENTA().equals(OWNER);
    }

    public boolean isInvalid(BalanceUpdateDto dto) {
        return isInvalidAccountType
                .or(isInvalidStatus)
                .or(isInvalidSavingAccount)
                .or(isInvalidCheckingAccount)
                .or(isMasterAccount)
                .or(isNotOwner)
                .test(dto);
    }
}