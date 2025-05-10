CREATE TABLE transactions (
    id                  VARCHAR2(36)    NOT NULL,
    id_source_wallet    VARCHAR2(36)    NOT NULL,
    id_target_wallet    VARCHAR2(36)    NOT NULL,
    num_amount          NUMBER(19,2)    NOT NULL,
    ind_type            VARCHAR2(20)    NOT NULL,
    dat_creation        TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);
