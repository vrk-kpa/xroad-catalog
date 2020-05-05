-- data is populated automatically by spring boot, for unit/integration tests

-- manipulate sequences
ALTER SEQUENCE member_id_seq RESTART WITH 1000;
ALTER SEQUENCE subsystem_id_seq RESTART WITH 1000;
ALTER SEQUENCE service_id_seq RESTART WITH 1000;
ALTER SEQUENCE wsdl_id_seq RESTART WITH 1000;

-- members with ids 3-7 contain graphs of member-subsystem-service-wsdl with different parts changed after 1.1.2017
-- members 1-7 are active (not removed). member 8 is removed.
-- (for testing find(date updatedSince))
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (1, 'dev-cs', 'PUB', '14151328', 'Nahka-Albert', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (2, 'dev-cs', 'PUB', '88855888', 'Suutari Simo', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (3, 'dev-cs', 'PUB', '11', 'Updated Member', '2016-01-01 00:00:00+02', '2017-02-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (4, 'dev-cs', 'PUB', '12', 'Updated Subsystem', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (5, 'dev-cs', 'PUB', '13', 'Updated Service', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (6, 'dev-cs', 'PUB', '14', 'Updated Wsdl', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (7, 'dev-cs', 'PUB', '15', 'Updated Everything', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched,  removed)
VALUES (8, 'dev-cs', 'PUB', '14151329', 'Removed item', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02');

-- member 1 has 3 subsystems, 2 active and 1 removed one
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (1, 1, 'subsystem_a1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (2, 1, 'subsystem_a2', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (3, 2, 'subsystem_b1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (4, 3, 'subsystem_3-1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (5, 4, 'subsystem_4-1-changed', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (6, 5, 'subsystem_5-1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (7, 6, 'subsystem_6-1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (8, 7, 'subsystem_7-1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (9, 7, 'subsystem_7-2-changed', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (10, 7, 'subsystem_7-3', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (11, 8, 'removed_subsystem', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02');
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched,  removed)
VALUES (12, 1, 'subsystem_a3_removed', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');

INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (1, 2, 'testService', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (2, 1, 'getRandom', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (3, 6, 'dummy-service_5-1-1-changed', 'v1', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (4, 7, 'dummy-service_6-1-1', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (5, 8, 'dummy-service_7-1-1-changed', 'v1', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (6, 8, 'dummy-service_7-1-2', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (7, 9, 'dummy-service_7-2-1', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (8, 8, 'removed-service_7-1-3', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (9, 8, 'removed-service_7-1-4', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (10, 8, 'service-with-null-version', NULL, '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (11, 8, 'removed-service_7-1-5', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched,  removed)
VALUES (12, 8, 'dummy-service_7-1-5', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (3, 4, '<?xml version="1.0" standalone="no"?><wsdl-6-1-1-1-changed/>', '1000', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (4, 6, '<?xml version="1.0" standalone="no"?><wsdl-7-1-2-1-changed/>', '1001', '2016-01-01 00:00:00+02', '2017-01-02 00:00:00+02', '2017-01-02 00:00:00+02', NULL);
INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (5, 7, '<?xml version="1.0" standalone="no"?><wsdl-7-2-1-1/>', '1002', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (6, 8, '<?xml version="1.0" standalone="no"?><removed-service_7-1-3-alive-wsdl/>', '3000', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (7, 9, '<?xml version="1.0" standalone="no"?><removed-service_7-1-4-removed-wsdl/>', '3001', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');

INSERT INTO open_api (id, service_id, data, external_id, created, changed, fetched, removed)
VALUES (1, 11, '<openapi>', '3003', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02');
INSERT INTO open_api (id, service_id, data, external_id, created, changed, fetched, removed)
VALUES (2, 12, '<openapi>', '3004', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

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
        '2020-05-04 11:41:17.484+03', '2020-05-04 11:41:17.484+03', NULL);

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

INSERT INTO company_form(id, company_id, source, version, name, language, type, registration_date,
    end_date, created, changed, fetched, removed)
VALUES (1, 1, 1, 1, 'Public limited company', 'EN', 0, '2017-10-19 00:00:00+03', NULL, '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO contact_detail(id, company_id, source, version, language, value, type, registration_date,
    end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 1, 'EN', '', 0, '2010-05-03 00:00:00+03', NULL, '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO language(id, company_id, source, version, name, language, registration_date, end_date, created, changed, fetched, removed)
VALUES (1, 1, 0, 1, 'Finska', 'SE', '2001-06-27 00:00:00+02', NULL, '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO registered_entry(id, company_id, description, status, register, language, authority,registration_date, end_date,
                             created, changed, fetched, removed)
VALUES (1, 1, 'Unregistered', 2, 1, 'EN', 2, '2001-06-11 00:00:00+02', '2001-06-24 00:00:00+02', '2020-05-04 11:41:24.792+03',
        '2020-05-04 11:41:24.792+03', '2020-05-04 11:41:24.792+03', NULL);

INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched,  removed)
VALUES (1, 1, '<?xml version="1.0" standalone="no"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                  xmlns:tns="http://test.x-road.fi/producer"
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:xrd="http://x-road.eu/xsd/xroad.xsd"
                  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                  xmlns:id="http://x-road.eu/xsd/identifiers"
                  name="testService" targetNamespace="http://test.x-road.fi/producer">
    <wsdl:types>
        <!-- Schema for identifiers (reduced) -->
        <xsd:schema elementFormDefault="qualified"
                    targetNamespace="http://x-road.eu/xsd/identifiers"
                    xmlns="http://x-road.eu/xsd/identifiers">
            <xsd:simpleType name="XRoadObjectType">
                <xsd:annotation>
                    <xsd:documentation>Enumeration for X-Road identifier
                        types that can be used in requests.
                    </xsd:documentation>
                </xsd:annotation>
                <xsd:restriction base="xsd:string">
                    <xsd:enumeration value="MEMBER" />
                    <xsd:enumeration value="SUBSYSTEM" />
                    <xsd:enumeration value="SERVICE" />
                </xsd:restriction>
            </xsd:simpleType>
            <xsd:element name="xRoadInstance" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Identifies the X-Road instance.
                        This field is applicable to all identifier
                        types.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberClass" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Type of the member (company,
                        government institution, private person, etc.)
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        member of given member type.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="subsystemCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        subsystem of given X-Road member.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        service offered by given X-Road member or
                        subsystem.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceVersion" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Version of the service.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:attribute name="objectType" type="XRoadObjectType" />
            <xsd:complexType name="XRoadClientIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
            <xsd:complexType name="XRoadServiceIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                    <xsd:element ref="serviceCode" />
                    <xsd:element minOccurs="0" ref="serviceVersion" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
        </xsd:schema>

        <!-- Schema for request headers -->
        <xsd:schema xmlns="http://www.w3.org/2001/XMLSchema"
                    targetNamespace="http://x-road.eu/xsd/xroad.xsd"
                    elementFormDefault="qualified">

            <xsd:element name="client" type="id:XRoadClientIdentifierType" />
            <xsd:element name="service" type="id:XRoadServiceIdentifierType" />
            <xsd:element name="userId" type="xsd:string" />
            <xsd:element name="id" type="xsd:string" />
            <xsd:element name="protocolVersion" type="xsd:string" />
        </xsd:schema>

        <!-- Schema for requests (reduced) -->
        <xsd:schema targetNamespace="http://test.x-road.fi/producer">
            <xsd:element name="testService">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="responseBodySize" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Response body character count.
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                    <xsd:element name="responseAttachmentSize" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Response attachment character count.
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="testServiceResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="responseBodySize" nillable="true" type="xsd:string"/>
                                    <xsd:element name="responseAttachmentSize" nillable="true" type="xsd:string"/>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="data" type="xsd:string" />
                                    <xsd:element name="processingTime" type="xsd:string" />
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
        </xsd:schema>
    </wsdl:types>

    <wsdl:message name="requestheader">
        <wsdl:part name="client" element="xrd:client" />
        <wsdl:part name="service" element="xrd:service" />
        <wsdl:part name="userId" element="xrd:userId" />
        <wsdl:part name="id" element="xrd:id" />
        <wsdl:part name="issue" element="xrd:issue"/>
        <wsdl:part name="protocolVersion" element="xrd:protocolVersion" />
    </wsdl:message>

    <wsdl:message name="testService">
        <wsdl:part name="body" element="tns:testService"/>
    </wsdl:message>
    <wsdl:message name="testServiceResponse">
        <wsdl:part name="body" element="tns:testServiceResponse"/>
    </wsdl:message>

    <wsdl:portType name="testServicePortType">
        <wsdl:operation name="testService">
            <wsdl:input message="tns:testService"/>
            <wsdl:output message="tns:testServiceResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <wsdl:binding name="testServiceBinding" type="tns:testServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />

        <wsdl:operation name="testService">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:service name="testService">
        <wsdl:port binding="tns:testServiceBinding" name="testServicePort">
            <soap:address location="http://localhost:8080/test-service-0.0.2-SNAPSHOT/Endpoint"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>', '1003', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);

INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched, removed)
VALUES (2, 2, '<?xml version="1.0" standalone="no"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                  xmlns:tns="http://test.x-road.fi/producer"
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:xrd="http://x-road.eu/xsd/xroad.xsd"
                  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                  xmlns:id="http://x-road.eu/xsd/identifiers"
                  name="testService" targetNamespace="http://test.x-road.fi/producer">
    <wsdl:types>
        <!-- Schema for identifiers (reduced) -->
        <xsd:schema elementFormDefault="qualified"
                    targetNamespace="http://x-road.eu/xsd/identifiers"
                    xmlns="http://x-road.eu/xsd/identifiers">
            <xsd:simpleType name="XRoadObjectType">
                <xsd:annotation>
                    <xsd:documentation>Enumeration for X-Road identifier
                        types that can be used in requests.
                    </xsd:documentation>
                </xsd:annotation>
                <xsd:restriction base="xsd:string">
                    <xsd:enumeration value="MEMBER" />
                    <xsd:enumeration value="SUBSYSTEM" />
                    <xsd:enumeration value="SERVICE" />
                </xsd:restriction>
            </xsd:simpleType>
            <xsd:element name="xRoadInstance" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Identifies the X-Road instance.
                        This field is applicable to all identifier
                        types.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberClass" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Type of the member (company,
                        government institution, private person, etc.)
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        member of given member type.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="subsystemCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        subsystem of given X-Road member.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        service offered by given X-Road member or
                        subsystem.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceVersion" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Version of the service.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:attribute name="objectType" type="XRoadObjectType" />
            <xsd:complexType name="XRoadClientIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
            <xsd:complexType name="XRoadServiceIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                    <xsd:element ref="serviceCode" />
                    <xsd:element minOccurs="0" ref="serviceVersion" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
        </xsd:schema>

        <!-- Schema for request headers -->
        <xsd:schema xmlns="http://www.w3.org/2001/XMLSchema"
                    targetNamespace="http://x-road.eu/xsd/xroad.xsd"
                    elementFormDefault="qualified">

            <xsd:element name="client" type="id:XRoadClientIdentifierType" />
            <xsd:element name="service" type="id:XRoadServiceIdentifierType" />
            <xsd:element name="userId" type="xsd:string" />
            <xsd:element name="id" type="xsd:string" />
            <xsd:element name="protocolVersion" type="xsd:string" />
        </xsd:schema>

        <!-- Schema for requests (reduced) -->
        <xsd:schema targetNamespace="http://test.x-road.fi/producer">
            <xsd:element name="getRandom" nillable="true" />
            <xsd:element name="getRandomResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="data" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Service response
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="helloService">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="name" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Name
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="helloServiceResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="name" nillable="true" type="xsd:string"/>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="message" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Service response
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
        </xsd:schema>
    </wsdl:types>

    <wsdl:message name="requestheader">
        <wsdl:part name="client" element="xrd:client" />
        <wsdl:part name="service" element="xrd:service" />
        <wsdl:part name="userId" element="xrd:userId" />
        <wsdl:part name="id" element="xrd:id" />
        <wsdl:part name="issue" element="xrd:issue"/>
        <wsdl:part name="protocolVersion" element="xrd:protocolVersion" />
    </wsdl:message>

    <wsdl:message name="getRandom">
        <wsdl:part name="body" element="tns:getRandom"/>
    </wsdl:message>
    <wsdl:message name="getRandomResponse">
        <wsdl:part name="body" element="tns:getRandomResponse"/>
    </wsdl:message>
    <wsdl:message name="helloService">
        <wsdl:part name="body" element="tns:helloService"/>
    </wsdl:message>
    <wsdl:message name="helloServiceResponse">
        <wsdl:part name="body" element="tns:helloServiceResponse"/>
    </wsdl:message>

    <wsdl:portType name="testServicePortType">
        <wsdl:operation name="getRandom">
            <wsdl:input message="tns:getRandom"/>
            <wsdl:output message="tns:getRandomResponse"/>
        </wsdl:operation>
        <wsdl:operation name="helloService">
            <wsdl:input message="tns:helloService"/>
            <wsdl:output message="tns:helloServiceResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <wsdl:binding name="testServiceBinding" type="tns:testServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />
        <wsdl:operation name="getRandom">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>

        <wsdl:operation name="helloService">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:service name="testService">
        <wsdl:port binding="tns:testServiceBinding" name="testServicePort">
            <soap:address location="http://localhost:8080/example-adapter-0.0.4-SNAPSHOT/Endpoint"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>
', '2050', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);


