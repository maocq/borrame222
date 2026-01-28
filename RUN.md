
```sh
#Pre-request Script:
function formatCustomZonedDate() {
    const d = new Date();

    const year   = d.getFullYear();
    const month  = String(d.getMonth() + 1).padStart(2, '0');
    const day    = String(d.getDate()).padStart(2, '0');
    const hour   = String(d.getHours()).padStart(2, '0');
    const minute = String(d.getMinutes()).padStart(2, '0');
    const second = String(d.getSeconds()).padStart(2, '0');

    const micro = String(d.getMilliseconds() * 1000).padStart(6, '0');
    return `${year}-${month}-${day}-${hour}.${minute}.${second}.${micro}`;
}

pm.environment.set("customZonedDate", formatCustomZonedDate());


```

## Balance
```sh
curl --location 'https://corenomonetario-int-dev.apps.ambientesbc.com/query-model-corex/kafka-publisher/api/event/publish' \
--header 'Content-Type: application/json' \
--data '[
    {
        "topic": "deposits.generalinformationupdatedv1",
        "key": "S10015303377",
        "value": {
            "id": "{{$guid}}",
            "TIPO_CUENTA": "S",
            "NUMERO_DE_CUENTA": "10015303377",
            "RELACION_CLIENTE_CUENTA": "T",
            "FLAGS_CUENTA": "NNYNN     NNNNNNNNNYNNNNNNN            YNY        ",
            "RSLDEFE": "12.00",	
            "RSLDCXC": ".00",
            "RSLDBLS": ".00",
            "VALOR_EMBARGADO": ".00",
            "UPDATE_TIMESTAMP": "{{customZonedDate}}",
            "CODIGO_PLAN": "71",
            "NOMBRE_ESTADO": "ACTIVO"	
        }
    }
]'
```


```sql
CREATE TABLE account_reconciliation (
    account TEXT COLLATE "C" PRIMARY KEY,
    iseries_balance NUMERIC NOT NULL,
    iseries_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    vault_balance NUMERIC NOT NULL,
    vault_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    qm_balance NUMERIC NOT NULL,
    qm_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
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
