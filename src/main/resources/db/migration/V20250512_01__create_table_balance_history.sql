CREATE TABLE balance_history (
    id              VARCHAR2(36)     NOT NULL,
    id_wallet       VARCHAR2(36)     NOT NULL,
    num_balance     NUMBER(19,2)     NOT NULL,
    dat_creation    TIMESTAMP        NOT NULL,
    CONSTRAINT pk_balance_history PRIMARY KEY (id),
    CONSTRAINT fk_balance_history_wallet FOREIGN KEY (id_wallet) REFERENCES wallet(id)
);
