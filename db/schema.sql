CREATE TABLE product_batch_information (
  id int(11) NOT NULL AUTO_INCREMENT,
  batch_id varchar(255) NOT NULL,
  dispatch tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY batch_id (batch_id)
);

CREATE TABLE product_generic_information (
  id int(11) NOT NULL AUTO_INCREMENT,
  generic_id varchar(255) NOT NULL,
  generic_information text NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY generic_id (generic_id)
);


CREATE TABLE product_instances (
  id int(11) NOT NULL AUTO_INCREMENT,
  uuid varchar(255) NOT NULL,
  generic_id varchar(255) NOT NULL,
  batch_id varchar(255) NOT NULL,
  mfg varchar(255) NOT NULL,
  expiry varchar(255) NOT NULL,
  mrp varchar(255) NOT NULL,
  verifier varchar(255) DEFAULT NULL,
  verification_timestamp varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uuid (uuid)
);


CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  phone_number varchar(255) NOT NULL,
  full_name varchar(255) NOT NULL,
  hashed_password varchar(255) NOT NULL,
  verified_product_uuids longtext DEFAULT NULL CHECK (json_valid(verified_product_uuids)),
  PRIMARY KEY (id),
  UNIQUE KEY phone_number (phone_number)
);
