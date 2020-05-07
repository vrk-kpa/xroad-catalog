-- data is populated automatically by spring boot, for unit/integration tests

-- manipulate sequences
ALTER SEQUENCE member_id_seq RESTART WITH 1000;
ALTER SEQUENCE subsystem_id_seq RESTART WITH 1000;
ALTER SEQUENCE service_id_seq RESTART WITH 1000;
ALTER SEQUENCE wsdl_id_seq RESTART WITH 1000;
ALTER SEQUENCE open_api_id_seq RESTART WITH 1000;

INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (1, 'dev-cs', 'PUB', '14151328', 'Nahka-Albert', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (2, 'dev-cs', 'PUB', '88855888', 'Suutari Simo', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (3, 'dev-cs', 'PUB', '11', 'Updated Member', '2016-01-01 00:00:00+02', '2017-02-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (1, 1, 'TestSubSystem', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (1, 1, 'testService', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (2, 1, 'getRandom', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (1, 1, '<?xml version="1.0" standalone="no"?><wsdl-6-1-1-1-changed/>', '1000', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);

INSERT INTO open_api (id, service_id, data, external_id, created, changed, fetched, removed)
VALUES (1, 2, '<openapi>', '3003', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO organization (id, organization_type, publishing_status, business_code, guid, created, changed, fetched, removed)
VALUES (1, 'Municipality', 'Published', '0123456-9', 'abcdef123456', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO organization_name (id, organization_id, language, type, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Name', 'Vaasan kaupunki', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO organization_description (id, organization_id, language, type, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Description', 'Vaasa on yli 67 000 asukkaan voimakkaasti kasvava kaupunki', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO email (id, organization_id, language, description, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Asiakaspalvelu', 'vaasa@vaasa.fi', '2016-01-01 00:00:00+02', '2020-01-01 00:00:00+02', '2020-01-01 00:00:00+02', NULL);

INSERT INTO phone_number (id, organization_id, language, additional_information, service_charge_type,
                          charge_description, prefix_number, is_finnish_service_number,
                          number, created, changed, fetched, removed)
VALUES (1, 1, 'FI', 'Puhelinvaihde', 'Chargeable', 'charge', '+358', false, '62249111', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO web_page (id, organization_id, language, url, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'https://www.vaasa.fi/', 'Vaasan kaupunki', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO address (id, organization_id, country, type, sub_type, created, changed, fetched, removed)
VALUES (1, 1, 'FI', 'Postal', 'Street', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street_address (id, address_id, street_number, postal_code, latitude, longitude,
                            coordinate_state, created, changed, fetched, removed)
VALUES (1, 1, '2', '64200', '6939589.246', '208229.722', 'Ok', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office_box_address (id, address_id, postal_code, created, changed, fetched, removed)
VALUES (1, 1, '64200', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street (id, street_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Motellikuja', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street_address_post_office (id, street_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'NIVALA', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street_address_additional_information (id, street_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Kaupungintalo/kaupunginjohtaja', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street_address_municipality (id, street_address_id, code, created, changed, fetched, removed)
VALUES (1, 1, '545', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO street_address_municipality_name (id, street_address_municipality_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Nivala', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office (id, post_office_box_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'FI', 'NIVALA', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office_box (id, post_office_box_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'FI', 'NIVALA', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office_box_address_additional_information (id, post_office_box_address_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Kaupungintalo/kaupunginjohtaja', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office_box_address_municipality (id, post_office_box_address_id, code, created, changed, fetched, removed)
VALUES (1, 1, '545', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO post_office_box_address_municipality_name (id, post_office_box_address_municipality_id, language, value, created, changed, fetched, removed)
VALUES (1, 1, 'fi', 'Nivala', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO company(id, business_id, company_form, details_uri, name, registration_date, created, changed, fetched, removed)
VALUES (1, '1710128-9', 'OYJ', '', 'Gofore Oyj', '2001-06-11 00:00:00+02', '2020-05-04 11:41:17.484+03',
        '2020-05-04 11:41:17.484+03', '2020-01-04 11:41:17.484+03', NULL);

INSERT INTO business_address(id, company_id, source, version, care_of, street, post_code,
                             city, language, type, country, registration_date, end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 1, '', 'Kalevantie 2', '33100', 'TAMPERE', 'FI', 2, '', '2016-07-12 00:00:00+03', NULL,
        '2020-05-04 11:41:17.54+03', '2020-05-04 11:41:17.54+03', '2020-05-04 11:41:17.54+03', NULL);

INSERT INTO business_auxiliary_name(id, company_id, source, ordering, version, name, language, registration_date,
                                    end_date, created, changed, fetched, removed)
VALUES (1, 1, 1, 5, 1, 'Solinor', '', '2019-01-31 00:00:00+02', NULL, '2020-05-04 11:41:24.717+03', '2020-05-04 11:41:24.717+03',
        '2020-05-04 11:41:24.717+03', NULL);

INSERT INTO business_id_change(id, company_id, source, description, reason, change_date, change,
                               old_business_id, new_business_id, language, created, changed, fetched, removed)
VALUES (1, 1, 2, '', '', '2019-02-01', 44, '1796717-0', '1710128-9', '', '2020-05-04 11:41:24.717+03', '2020-05-04 11:41:24.717+03',
        '2020-05-04 11:41:24.717+03', NULL);

INSERT INTO business_line(id, company_id, source, ordering, version, name, language, registration_date,
                          end_date, created, changed, fetched, removed)
VALUES (1, 1, 2, 0, 1, 'Dataprogrammering', 'SE', '2007-12-31 00:00:00+02', NULL, '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO business_name(id, company_id, source, ordering, version, name, language, registration_date,
                          end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 0, 0, '', 'FI', '2019-01-31 00:00:00+02', NULL,
        '2020-05-04 11:41:24.717+03', '2020-05-04 11:41:24.717+03', '2020-05-04 11:41:24.717+03', NULL);

INSERT INTO company_form(id, company_id, source, version, name, language, type, registration_date,
                         end_date, created, changed, fetched, removed)
VALUES (1, 1, 1, 1, 'Public limited company', 'EN', 0, '2017-10-19 00:00:00+03', NULL, '2020-05-04 11:41:24.792+03',
        '2020-05-06 11:41:24.792+03', '2020-05-06 11:41:24.792+03', NULL);

INSERT INTO contact_detail(id, company_id, source, version, language, value, type, registration_date,
                           end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 1, 'EN', '', 0, '2010-05-03 00:00:00+03', NULL, '2020-05-04 11:41:24.792+03', '2020-05-06 11:41:24.792+03',
        '2020-05-06 11:41:24.792+03', NULL);

INSERT INTO language(id, company_id, source, version, name, language, registration_date, end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 1, 'Finska', 'SE', '2001-06-27 00:00:00+02', NULL, '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO liquidation(id, company_id, source, version, name, language, type, registration_date,
                        end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 0, '', 'FI', 1, '2001-06-27 00:00:00+02', NULL,
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO registered_entry(id, company_id, description, status, register, language, authority,registration_date, end_date,
                             created, changed, fetched, removed)
VALUES (1, 1, 'Unregistered', 2, 1, 'EN', 2, '2001-06-11 00:00:00+02', '2001-06-24 00:00:00+02', '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO registered_office(id, company_id, source, ordering, version, name, language, registration_date,
                              end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 0, 0, '', 'FI', '2001-06-11 00:00:00+02',
        NULL, '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);