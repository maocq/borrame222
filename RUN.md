
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
```
