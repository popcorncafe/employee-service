CREATE TABLE position
(
    id     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name   varchar(30) NOT NULL,
    salary float4      NOT NULL DEFAULT 0,
    roles  smallint[]  NOT NULL
);

CREATE TABLE employee
(
    id                  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname           varchar(40) NOT NULL,
    surname             varchar(40) NOT NULL,
    date_of_birth       date        NOT NULL,
    email               varchar(40) NULL UNIQUE,
    phone_number        varchar(20) NULL UNIQUE,
    register_date       timestamptz NOT NULL DEFAULT current_timestamp,
    position_id         uuid        NULL REFERENCES position (id),
    store_id            uuid        NULL,
    personal_percentage int2        NOT NULL DEFAULT 0
);