-- Einfügen von Beispiel-Standorten
INSERT INTO location (name) VALUES
('Küche'),
('Keller'),
('Wohnzimmer');

-- Einfügen von Lagerungseinheiten (Storage Units) für jeden Standort
INSERT INTO storage_unit (name, location_id) VALUES
('Tiefkühler', (SELECT id FROM location WHERE name = 'Küche')),
('Kühlschrank', (SELECT id FROM location WHERE name = 'Küche')),
('Weinkühler', (SELECT id FROM location WHERE name = 'Keller')),
('Regal', (SELECT id FROM location WHERE name = 'Wohnzimmer'));

-- Falls es Kategorien gibt
INSERT INTO category (name) VALUES
('Lebensmittel'),
('Werkzeuge'),
('Elektronik');

-- Einfügen von Fächern für jede Lagerungseinheit
INSERT INTO compartment (name, storage_unit_id) VALUES
('Fach 1', (SELECT id FROM storage_unit WHERE name = 'Tiefkühler')),
('Fach 2', (SELECT id FROM storage_unit WHERE name = 'Kühlschrank')),
('Fach 3', (SELECT id FROM storage_unit WHERE name = 'Weinkühler')),
('Fach 4', (SELECT id FROM storage_unit WHERE name = 'Regal'));

-- Einfügen von Items mit zufälligen Kategorien und Fächern
INSERT INTO item (name, category_id, compartment_id, quantity) VALUES
('Eiscreme', (SELECT id FROM category WHERE name = 'Lebensmittel'), (SELECT id FROM compartment WHERE name = 'Fach 1'), 2),
('Gefrorenes Gemüse', (SELECT id FROM category WHERE name = 'Lebensmittel'), (SELECT id FROM compartment WHERE name = 'Fach 2'), 5),
('Bohrmaschine', (SELECT id FROM category WHERE name = 'Werkzeuge'), (SELECT id FROM compartment WHERE name = 'Fach 3'), 1),
('Taschenlampe', (SELECT id FROM category WHERE name = 'Elektronik'), (SELECT id FROM compartment WHERE name = 'Fach 4'), 3);
