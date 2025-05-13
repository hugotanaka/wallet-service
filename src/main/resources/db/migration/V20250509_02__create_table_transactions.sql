CREATE TABLE transactions (
    id                      VARCHAR2(36)    NOT NULL,
    id_source_wallet        VARCHAR2(36),
    id_target_wallet        VARCHAR2(36),
    id_external_reference   VARCHAR2(36)    NOT NULL,
    num_amount              NUMBER(19,2)    NOT NULL,
    ind_type                VARCHAR2(20)    NOT NULL,
    dat_creation            TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id),
    CONSTRAINT fk_transaction_source_wallet FOREIGN KEY (id_source_wallet) REFERENCES wallet(id),
    CONSTRAINT fk_transaction_target_wallet FOREIGN KEY (id_target_wallet) REFERENCES wallet(id),
    CONSTRAINT uk_external_reference UNIQUE (id_external_reference)
);
