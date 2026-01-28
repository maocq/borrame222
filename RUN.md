
```sql
CREATE TABLE account_reconciliation (
    account TEXT COLLATE "C" PRIMARY KEY,
    iseries_balance NUMERIC NOT NULL DEFAULT 0,
    iseries_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    vault_balance NUMERIC NOT NULL DEFAULT 0,
    vault_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    qm_balance NUMERIC NOT NULL DEFAULT 0,
    qm_datetime TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_reconciled BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Iseries
UPDATE account_reconciliation
    SET iseries_balance=10.2, iseries_datetime='2026-01-27 01:00:00.000', updated_at='2026-01-27 01:00:00.000'
WHERE account='S1';

INSERT INTO account_reconciliation
    (account, iseries_balance, iseries_datetime, updated_at)
VALUES('S2', 1, '2026-01-01 00:00:00.000', '2026-01-01 00:00:00.000')
    ON CONFLICT (account)
DO UPDATE SET
    iseries_balance = 2, iseries_datetime = '2026-01-01 00:00:00.000', updated_at = '2026-01-01 00:00:00.000'



-- Vault
UPDATE account_reconciliation
    SET vault_balance=10.2, vault_datetime='2026-01-27 01:00:00.000', updated_at='2026-01-27 01:00:00.000'
WHERE account='S1';

INSERT INTO account_reconciliation
    (account, vault_balance, vault_datetime, updated_at)
VALUES('S2', 1, '2026-01-01 00:00:00.000', '2026-01-01 00:00:00.000')
    ON CONFLICT (account)
DO UPDATE SET
    vault_balance = 2, vault_datetime = '2026-01-01 00:00:00.000', updated_at = '2026-01-01 00:00:00.000'


-- QM
UPDATE account_reconciliation
    SET qm_balance=10.2, qm_datetime='2026-01-27 01:00:00.000', updated_at='2026-01-27 01:00:00.000'
WHERE account='S1';

INSERT INTO account_reconciliation
    (account, qm_balance, qm_datetime, updated_at)
VALUES('S2', 1, '2026-01-01 00:00:00.000', '2026-01-01 00:00:00.000')
    ON CONFLICT (account)
DO UPDATE SET
    qm_balance = 2, qm_datetime = '2026-01-01 00:00:00.000', updated_at = '2026-01-01 00:00:00.000'

--GRANT SELECT, INSERT, UPDATE, DELETE ON account_reconciliation TO cnxcxacd;
```
