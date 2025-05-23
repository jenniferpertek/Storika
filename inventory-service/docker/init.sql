-- Clear existing data (optional, use with caution on non-test environments)
-- Consider the order if you have foreign key constraints that are not ON DELETE CASCADE
-- DELETE FROM items;
-- DELETE FROM compartments;
-- DELETE FROM storage_units;
-- DELETE FROM categories;
-- DELETE FROM locations;

-- Declare variables to store generated UUIDs (PostgreSQL specific syntax for easy reuse)
DO $$
DECLARE
    -- Locations
    loc1_id UUID := gen_random_uuid();
    loc2_id UUID := gen_random_uuid();
    loc3_id UUID := gen_random_uuid();
    loc4_id UUID := gen_random_uuid();
    loc5_id UUID := gen_random_uuid();

    -- Categories
    cat1_id UUID := gen_random_uuid(); -- Electronics
    cat2_id UUID := gen_random_uuid(); -- Groceries - Perishable
    cat3_id UUID := gen_random_uuid(); -- Groceries - Non-Perishable
    cat4_id UUID := gen_random_uuid(); -- Tools
    cat5_id UUID := gen_random_uuid(); -- Office Supplies

    -- Storage Units
    su1_id UUID := gen_random_uuid(); -- At loc1_id
    su2_id UUID := gen_random_uuid(); -- At loc1_id
    su3_id UUID := gen_random_uuid(); -- At loc2_id
    su4_id UUID := gen_random_uuid(); -- At loc3_id
    su5_id UUID := gen_random_uuid(); -- At loc5_id
    su6_id UUID := gen_random_uuid(); -- At loc1_id (Secure Cage)

    -- Compartments
    comp1_id UUID := gen_random_uuid(); -- In su1_id
    comp2_id UUID := gen_random_uuid(); -- In su1_id
    comp3_id UUID := gen_random_uuid(); -- In su2_id
    comp4_id UUID := gen_random_uuid(); -- In su3_id
    comp5_id UUID := gen_random_uuid(); -- In su5_id
    comp6_id UUID := gen_random_uuid(); -- In su1_id (Pallet Spot)
    comp7_id UUID := gen_random_uuid(); -- In su6_id (Safe Box)

BEGIN

    -- 1. Categories (Based on 001_create_table_categories.yaml)
    -- Added 'description' column
    INSERT INTO categories (category_id, name, description, created_at, updated_at) VALUES
    (cat1_id, 'Electronics', 'Consumer electronics, components, and accessories.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (cat2_id, 'Groceries - Perishable', 'Food items requiring refrigeration or with a short shelf life.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (cat3_id, 'Groceries - Non-Perishable', 'Shelf-stable food items like canned goods and dry pasta.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (cat4_id, 'Tools', 'Hand tools, power tools, and workshop equipment.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (cat5_id, 'Office Supplies', 'Stationery, printer supplies, and general office equipment.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- 2. Locations (Based on 002_create_table_locations.yaml)
    INSERT INTO locations (location_id, name, description, created_at, updated_at) VALUES
    (loc1_id, 'Main Warehouse', 'Primary distribution center with multiple storage units.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (loc2_id, 'Downtown Store', 'Retail outlet with backstock storage.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (loc3_id, 'North Depot', 'Secondary storage facility for bulk items.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (loc4_id, 'Westside Storage', 'Smaller, secure storage units for overflow.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (loc5_id, 'Home Office Garage', 'Personal storage for tools and supplies.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- 3. Storage Units (Based on 003_create_table_storage_units.yaml)
    INSERT INTO storage_units (storage_unit_id, location_id, name, description, created_at, updated_at) VALUES
    (su1_id, loc1_id, 'Aisle 1 Racks', 'Heavy duty racking for large items', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (su2_id, loc1_id, 'Cold Storage Unit A', 'Refrigerated unit for perishables', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (su3_id, loc2_id, 'Showroom Backstock', 'Shelving for store overflow', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (su4_id, loc3_id, 'Bulk Pallet Area', 'Open space for palletized goods', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (su5_id, loc5_id, 'Tool Cabinet', 'Metal cabinet for tools and small parts', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (su6_id, loc1_id, 'Secure Cage', 'Fenced area for high-value items', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- 4. Compartments (Based on 004_create_table_compartments.yaml)
    INSERT INTO compartments (compartment_id, storage_unit_id, name, description, created_at, updated_at) VALUES
    (comp1_id, su1_id, 'Shelf A1-01', 'Top shelf, left side', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp2_id, su1_id, 'Shelf A1-02', 'Top shelf, right side', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp3_id, su2_id, 'Fridge Section 1', 'Dairy and produce', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp4_id, su3_id, 'Bin 2B', 'Small electronics parts', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp5_id, su5_id, 'Drawer 1', 'Screwdrivers and wrenches', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp6_id, su1_id, 'Pallet Spot P3', 'Reserved for incoming large boxes', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (comp7_id, su6_id, 'Safe Box Alpha', 'Small secure box within cage', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

    -- 5. Items (Corrected based on your table structure including description and notes)
    INSERT INTO items (
        item_id, name, quantity, unit, purchase_date, expiration_date, description, notes,
        category_id, storage_unit_id, compartment_id, created_at, updated_at
    ) VALUES
    -- Item 1: Electronics in Aisle 1, Shelf A1-01
    (gen_random_uuid(), 'Wireless Mouse X100', 50.00, 'pcs', CURRENT_DATE - INTERVAL '30 day', NULL, 'Ergonomic wireless mouse, bulk order', NULL, cat1_id, su1_id, comp1_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 2: Electronics in Aisle 1, Shelf A1-02
    (gen_random_uuid(), 'USB-C Cable 2m', 120.00, 'pcs', CURRENT_DATE - INTERVAL '10 day', NULL, 'Braided USB-C charging/data cable, black', NULL, cat1_id, su1_id, comp2_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 3: Perishable Groceries in Cold Storage, Fridge Section 1
    (gen_random_uuid(), 'Organic Milk 1L', 24.00, 'ltr', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE + INTERVAL '7 day', 'Fresh organic whole milk, Brand X', NULL, cat2_id, su2_id, comp3_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 4: Perishable Groceries in Cold Storage, Fridge Section 1
    (gen_random_uuid(), 'Cheddar Cheese Block', 30.00, 'pcs', CURRENT_DATE - INTERVAL '5 day', CURRENT_DATE + INTERVAL '30 day', 'Aged cheddar cheese, 250g blocks', NULL, cat2_id, su2_id, comp3_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 5: Non-Perishable Groceries in Showroom Backstock, Bin 2B
    (gen_random_uuid(), 'Canned Tuna', 200.00, 'cans', CURRENT_DATE - INTERVAL '90 day', CURRENT_DATE + INTERVAL '365 day', 'Tuna in spring water, 150g cans, Brand Y', NULL, cat3_id, su3_id, comp4_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 6: Non-Perishable Groceries in Bulk Pallet Area (no specific compartment)
    (gen_random_uuid(), 'Bulk Rice 20kg Bag', 10.00, 'bags', CURRENT_DATE - INTERVAL '60 day', NULL, 'Long grain white rice, store in dry area', NULL, cat3_id, su4_id, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 7: Tools in Home Office Garage, Tool Cabinet, Drawer 1
    (gen_random_uuid(), 'Phillips Screwdriver Set', 5.00, 'sets', CURRENT_DATE - INTERVAL '180 day', NULL, 'Set of 5 Phillips head screwdrivers, various sizes', NULL, cat4_id, su5_id, comp5_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 8: Tools in Home Office Garage, Tool Cabinet, Drawer 1
    (gen_random_uuid(), 'Adjustable Wrench 10"', 3.00, 'pcs', CURRENT_DATE - INTERVAL '180 day', NULL, 'Chrome vanadium adjustable wrench, heavy duty', NULL, cat4_id, su5_id, comp5_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 9: Office Supplies in Aisle 1 Racks (no specific compartment)
    (gen_random_uuid(), 'A4 Printer Paper Ream', 40.00, 'reams', CURRENT_DATE - INTERVAL '20 day', NULL, '500 sheets, 80gsm, bright white', NULL, cat5_id, su1_id, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 10: High-value Electronics in Secure Cage, Safe Box Alpha
    (gen_random_uuid(), 'High-End Graphics Card', 5.00, 'pcs', CURRENT_DATE - INTERVAL '5 day', NULL, 'Latest model GPU, handle with care, anti-static bag', NULL, cat1_id, su6_id, comp7_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 11: Uncategorized item in Showroom Backstock (no compartment, no category)
    (gen_random_uuid(), 'Uncategorized Box', 1.00, 'box', CURRENT_DATE - INTERVAL '2 day', NULL, 'Miscellaneous items, needs sorting, found in receiving', NULL, NULL, su3_id, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Item 12: Another item for Aisle 1 Racks, Shelf A1-01 (same compartment as item 1)
    (gen_random_uuid(), 'Keyboard K270', 25.00, 'pcs', CURRENT_DATE - INTERVAL '30 day', NULL, 'Standard wireless keyboard', NULL, cat1_id, su1_id, comp1_id, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

END $$;