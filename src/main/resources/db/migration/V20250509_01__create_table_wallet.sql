CREATE TABLE wallet (
    id              VARCHAR2(36)    NOT NULL,
    id_user         VARCHAR2(36)    NOT NULL,
    num_balance     NUMBER(19,2)    DEFAULT 0 NOT NULL,
    dat_creation    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    dat_update      TIMESTAMP,
    CONSTRAINT pk_wallet PRIMARY KEY (id)
);
