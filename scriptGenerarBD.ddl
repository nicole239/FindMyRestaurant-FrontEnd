-- Generado por Oracle SQL Developer Data Modeler 18.1.0.082.1035
--   en:        2019-09-10 20:01:33 CST
--   sitio:      Oracle Database 12cR2
--   tipo:      Oracle Database 12cR2



CREATE TABLE calification (
    idrestaurant   INTEGER NOT NULL,
    useremail      VARCHAR2(100) NOT NULL,
    calification   NUMBER(7,2)
);

COMMENT ON COLUMN calification.calification IS
    'User calification for a restaurant. Max is 5 stars';

ALTER TABLE calification ADD CONSTRAINT calificacion_pk PRIMARY KEY ( useremail,
                                                                      idrestaurant );

CREATE TABLE "Comment" (
    useremail      VARCHAR2(100) NOT NULL,
    idrestaurant   INTEGER NOT NULL,
    "Date"         DATE NOT NULL,
    "comment"      VARCHAR2(250)
);

ALTER TABLE "Comment"
    ADD CONSTRAINT comentarios_pk PRIMARY KEY ( idrestaurant,
                                                "Date",
                                                useremail );

CREATE TABLE foodtype (
    idfoodtype   INTEGER NOT NULL,
    name         VARCHAR2(50)
);

ALTER TABLE foodtype ADD CONSTRAINT foodtype_pk PRIMARY KEY ( idfoodtype );

CREATE TABLE photo (
    url            VARCHAR2 
--  ERROR: VARCHAR2 size not specified 
     NOT NULL,
    idrestaurant   INTEGER NOT NULL
);

ALTER TABLE photo ADD CONSTRAINT foto_pk PRIMARY KEY ( url );

CREATE TABLE restaurant (
    idrestaurant   INTEGER NOT NULL,
    name           VARCHAR2(70) NOT NULL,
    latitude       NUMBER(10,7) NOT NULL,
    longitude      NUMBER(10,7) NOT NULL,
    schedule       VARCHAR2(200) NOT NULL,
    idfoodtype     INTEGER NOT NULL,
    phonenumber    NUMBER(10) NOT NULL,
    website        VARCHAR2(200) NOT NULL,
    price          CHAR(1) NOT NULL,
    usrcreator     VARCHAR2(100) NOT NULL
);

COMMENT ON COLUMN restaurant.idrestaurant IS
    'Restaurant ID';

COMMENT ON COLUMN restaurant.name IS
    'Restaurant name';

COMMENT ON COLUMN restaurant.latitude IS
    'GPS latitude';

COMMENT ON COLUMN restaurant.longitude IS
    'GPS longitude';

COMMENT ON COLUMN restaurant.schedule IS
    'Wich days it''s opened and the opening and closing time';

COMMENT ON COLUMN restaurant.idfoodtype IS
    'Food type';

COMMENT ON COLUMN restaurant.phonenumber IS
    'phone number';

COMMENT ON COLUMN restaurant.website IS
    'website name';

COMMENT ON COLUMN restaurant.price IS
    'Price range: ''H'' for  high,''M'' for  medium or ''L'' for  low';

COMMENT ON COLUMN restaurant.usrcreator IS
    'User who created the restaurant';

ALTER TABLE restaurant
    ADD CONSTRAINT restaurant_price_ck CHECK (
        price = 'H'
        OR price = 'M'
        OR price = 'L'
    );

ALTER TABLE restaurant ADD CONSTRAINT restaurant_pk PRIMARY KEY ( idrestaurant );

CREATE TABLE "User" (
    email      VARCHAR2(100) NOT NULL,
    name       VARCHAR2(70),
    password   VARCHAR2(20),
    type       CHAR(1)
);

COMMENT ON COLUMN "User".email IS
    'User email';

COMMENT ON COLUMN "User".name IS
    'User name';

COMMENT ON COLUMN "User".password IS
    'User password';

COMMENT ON COLUMN "User".type IS
    '''A'' for Administrator and ''R'' for Regular';

ALTER TABLE "User"
    ADD CONSTRAINT user_type_ck CHECK (
        type = 'A'
        OR type = 'R'
    );

ALTER TABLE "User" ADD CONSTRAINT usuario_pk PRIMARY KEY ( email );

ALTER TABLE calification
    ADD CONSTRAINT calification_restaurant_fk FOREIGN KEY ( idrestaurant )
        REFERENCES restaurant ( idrestaurant );

ALTER TABLE calification
    ADD CONSTRAINT calification_user_fk FOREIGN KEY ( useremail )
        REFERENCES "User" ( email );

ALTER TABLE "Comment"
    ADD CONSTRAINT comment_restaurant_fk FOREIGN KEY ( idrestaurant )
        REFERENCES restaurant ( idrestaurant );

ALTER TABLE "Comment"
    ADD CONSTRAINT comment_user_fk FOREIGN KEY ( useremail )
        REFERENCES "User" ( email );

ALTER TABLE photo
    ADD CONSTRAINT foto_restaurant_fk FOREIGN KEY ( idrestaurant )
        REFERENCES restaurant ( idrestaurant );

ALTER TABLE restaurant
    ADD CONSTRAINT restaurant_foodtype_fk FOREIGN KEY ( idfoodtype )
        REFERENCES foodtype ( idfoodtype );



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             6
-- CREATE INDEX                             0
-- ALTER TABLE                             14
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   1
-- WARNINGS                                 0
