package com.agroerp.config;

import com.agroerp.entity.*;
import com.agroerp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import com.agroerp.enums.AccountType;
import com.agroerp.enums.MaterialType;
import com.agroerp.enums.StockTransactionType;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(CompanyRepository companyRepository, RoleRepository roleRepository, UserRepository userRepository, UnitRepository unitRepository,
                           CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, BrandRepository brandRepository,
                           WarehouseRepository warehouseRepository, ProductRepository productRepository, RetailerRepository retailerRepository,
                           TerritoryRepository territoryRepository, StockTransactionRepository stockRepository,
                           ChartOfAccountRepository accountRepository,
                           PasswordEncoder encoder) {
        return args -> {
            Company defaultCompany = companyRepository.findByCompanyCode("AGRO-001").orElseGet(() -> {
                Company company = new Company();
                company.setCompanyCode("AGRO-001");
                company.setCompanyName("Agro Business Ltd.");
                company.setMobileNumber("01700000000");
                company.setEmail("info@agroerp.local");
                company.setAddress("Head Office");
                return companyRepository.save(company);
            });
            List<String> roleNames = List.of("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_SALES_MANAGER", "ROLE_SALES_OFFICER",
                    "ROLE_ACCOUNTS_USER", "ROLE_STORE_USER", "ROLE_DELIVERY_USER", "ROLE_RETAILER");
            for (String roleName : roleNames) {
                roleRepository.findByName(roleName).orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    role.setDescription(roleName.replace("ROLE_", "").replace("_", " "));
                    return roleRepository.save(role);
                });
            }
            Role superAdminRole = roleRepository.findByName("ROLE_SUPER_ADMIN").orElseThrow();
            User adminUser = userRepository.findByUsernameAndDeletedFalse("admin").orElseGet(() -> {
                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@agroerp.local");
                user.setFullName("System Administrator");
                return user;
            });
            adminUser.setActive(true);
            adminUser.setDeleted(false);
            if (adminUser.getEmail() == null || adminUser.getEmail().isBlank()) {
                adminUser.setEmail("admin@agroerp.local");
            }
            if (adminUser.getFullName() == null || adminUser.getFullName().isBlank()) {
                adminUser.setFullName("System Administrator");
            }
            adminUser.setCompany(defaultCompany);
            if (adminUser.getPassword() == null || !encoder.matches("admin123", adminUser.getPassword())) {
                adminUser.setPassword(encoder.encode("admin123"));
            }
            adminUser.getRoles().add(superAdminRole);
            userRepository.save(adminUser);
            for (String unitName : List.of("KG", "Gram", "Liter", "ML", "Packet", "Bag", "Sack", "Bottle", "Box", "PCS")) {
                if (unitRepository.findAll().stream().noneMatch(u -> u.getName().equalsIgnoreCase(unitName))) {
                    Unit unit = new Unit();
                    unit.setName(unitName);
                    unitRepository.save(unit);
                }
            }
            Category fertilizer = seedCategory(categoryRepository, "Fertilizer");
            Category cropProtection = seedCategory(categoryRepository, "Crop Protection");
            Category bioProduct = seedCategory(categoryRepository, "Organic & Bio Product");
            SubCategory chemicalFertilizer = seedSubCategory(subCategoryRepository, fertilizer, "Chemical Fertilizer");
            SubCategory micronutrient = seedSubCategory(subCategoryRepository, fertilizer, "Micronutrient");
            SubCategory organicFertilizer = seedSubCategory(subCategoryRepository, bioProduct, "Organic Fertilizer");
            SubCategory insecticide = seedSubCategory(subCategoryRepository, cropProtection, "Insecticide");
            SubCategory fungicide = seedSubCategory(subCategoryRepository, cropProtection, "Fungicide");
            SubCategory herbicide = seedSubCategory(subCategoryRepository, cropProtection, "Herbicide");
            SubCategory bioPesticide = seedSubCategory(subCategoryRepository, bioProduct, "Bio Pesticide");
            Brand agroPrime = seedBrand(brandRepository, "Agro Prime");
            Brand banglaAgro = seedBrand(brandRepository, "Bangla Agro");
            Brand greenCare = seedBrand(brandRepository, "Green Care");
            if (warehouseRepository.count() == 0) {
                Warehouse w = new Warehouse();
                w.setCode("MAIN");
                w.setName("Main Warehouse");
                w.setAddress("Head office warehouse");
                warehouseRepository.save(w);
            }
            Warehouse mainWarehouse = warehouseRepository.findAll().get(0);
            Unit kg = findUnit(unitRepository, "KG");
            Unit liter = findUnit(unitRepository, "Liter");
            Unit packet = findUnit(unitRepository, "Packet");
            Unit bag = findUnit(unitRepository, "Bag");
            Unit bottle = findUnit(unitRepository, "Bottle");
            Territory dhakaNorth = seedTerritory(territoryRepository, "DHK-N", "Dhaka North", "Kawran Bazar");
            Territory jashore = seedTerritory(territoryRepository, "JSR-S", "Jashore South", "Monirampur Market");
            seedRetailer(retailerRepository, dhakaNorth, "RTL-001", "Rahman Agro Traders", "Md. Rahman", "01711000001",
                    "rahman.agro@example.com", "Kawran Bazar, Dhaka", "Kawran Bazar", "500000", "25000");
            seedRetailer(retailerRepository, dhakaNorth, "RTL-002", "Green Field Krishi Ghar", "Sadia Islam", "01711000002",
                    "greenfield@example.com", "Savar, Dhaka", "Savar Bazar", "350000", "12000");
            seedRetailer(retailerRepository, jashore, "RTL-003", "Molla Fertilizer Store", "Abdul Molla", "01711000003",
                    "molla.store@example.com", "Monirampur, Jashore", "Monirampur Market", "420000", "18000");

            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-UREA-001", "Urea Fertilizer",
                    "Nitrogen based fertilizer for fast leaf and stem growth.", MaterialType.TRADING_PRODUCT,
                    fertilizer, chemicalFertilizer, agroPrime, bag, "50 KG Bag", "1100", "1350", "1300", "5", "1", "UREA-2026-A", "500");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-TSP-001", "TSP Triple Super Phosphate",
                    "Phosphorus fertilizer for stronger root development.", MaterialType.TRADING_PRODUCT,
                    fertilizer, chemicalFertilizer, banglaAgro, bag, "50 KG Bag", "1400", "1700", "1650", "5", "1", "TSP-2026-A", "420");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-DAP-001", "DAP Di-Ammonium Phosphate",
                    "Combined nitrogen and phosphorus fertilizer for balanced crop nutrition.", MaterialType.TRADING_PRODUCT,
                    fertilizer, chemicalFertilizer, banglaAgro, bag, "50 KG Bag", "1550", "1900", "1850", "5", "1", "DAP-2026-A", "380");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-MOP-001", "MOP Muriate of Potash",
                    "Potassium fertilizer for better yield and crop disease resistance.", MaterialType.TRADING_PRODUCT,
                    fertilizer, chemicalFertilizer, agroPrime, bag, "50 KG Bag", "1250", "1550", "1500", "5", "1", "MOP-2026-A", "410");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-GYP-001", "Gypsum Fertilizer",
                    "Sulfur and calcium source for soil nutrition correction.", MaterialType.TRADING_PRODUCT,
                    fertilizer, chemicalFertilizer, greenCare, bag, "25 KG Bag", "450", "650", "620", "5", "0", "GYP-2026-A", "300");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-ZNB-001", "Zinc & Boron Micronutrient",
                    "Micronutrient support for rice panicle formation and fruit cracking control.", MaterialType.TRADING_PRODUCT,
                    fertilizer, micronutrient, greenCare, packet, "1 KG Packet", "180", "260", "245", "5", "0", "ZNB-2026-A", "900");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-VERMI-001", "Vermicompost",
                    "Organic compost made from cow dung and earthworm processing for soil fertility.", MaterialType.FINISHED_PRODUCTS,
                    bioProduct, organicFertilizer, greenCare, bag, "20 KG Bag", "180", "280", "260", "0", "2", "VRM-2026-A", "260");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-TRICO-001", "Trico-Compost",
                    "Bio compost with fungal disease suppression support.", MaterialType.FINISHED_PRODUCTS,
                    bioProduct, organicFertilizer, greenCare, bag, "25 KG Bag", "260", "390", "365", "0", "2", "TRC-2026-A", "220");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-BONEMEAL-001", "Bone Meal",
                    "Natural phosphorus source for nursery and rooftop gardening.", MaterialType.RAW_MATERIALS,
                    bioProduct, organicFertilizer, banglaAgro, kg, "5 KG Pack", "240", "360", "340", "0", "1", "BML-2026-A", "180");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-IMIDA-001", "Imidacloprid",
                    "Insecticide for jassid, thrips, and whitefly control.", MaterialType.TRADING_PRODUCT,
                    cropProtection, insecticide, agroPrime, bottle, "100 ML Bottle", "130", "210", "195", "5", "0", "IMD-2026-A", "800");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-CYPER-001", "Cypermethrin",
                    "Insecticide used for caterpillar and stem borer control.", MaterialType.TRADING_PRODUCT,
                    cropProtection, insecticide, agroPrime, bottle, "250 ML Bottle", "190", "310", "290", "5", "0", "CYP-2026-A", "650");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-CARBO-001", "Carbofuran Granules",
                    "Granular insecticide for soil insects and rice stem borer management.", MaterialType.TRADING_PRODUCT,
                    cropProtection, insecticide, banglaAgro, kg, "1 KG Pack", "210", "330", "310", "5", "0", "CRB-2026-A", "500");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-MANCO-001", "Mancozeb Fungicide",
                    "Fungicide for blight, dieback, and plant rot disease control.", MaterialType.TRADING_PRODUCT,
                    cropProtection, fungicide, greenCare, packet, "500 Gram Packet", "260", "410", "390", "5", "0", "MNZ-2026-A", "480");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-CARBEN-001", "Carbendazim Fungicide",
                    "Systemic fungicide for crop disease protection.", MaterialType.TRADING_PRODUCT,
                    cropProtection, fungicide, agroPrime, packet, "100 Gram Packet", "120", "210", "195", "5", "0", "CBD-2026-A", "720");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-GLYPHO-001", "Glyphosate Herbicide",
                    "Herbicide for clearing harmful weeds and grasses from fields.", MaterialType.TRADING_PRODUCT,
                    cropProtection, herbicide, banglaAgro, liter, "1 Liter Bottle", "420", "650", "620", "5", "0", "GLY-2026-A", "280");
            seedProduct(productRepository, stockRepository, mainWarehouse, "PRD-NEEM-001", "Neem Oil Bio-Pesticide",
                    "Environment-friendly bio pesticide for safer crop protection.", MaterialType.FINISHED_PRODUCTS,
                    bioProduct, bioPesticide, greenCare, bottle, "500 ML Bottle", "220", "350", "330", "0", "2", "NEEM-2026-A", "360");

            seedAccount(accountRepository, "1000", "Cash and Bank", AccountType.ASSET);
            seedAccount(accountRepository, "1100", "Accounts Receivable", AccountType.ASSET);
            seedAccount(accountRepository, "4000", "Sales Revenue", AccountType.INCOME);
            seedAccount(accountRepository, "5000", "Discount Allowed", AccountType.EXPENSE);
            seedAccount(accountRepository, "2100", "VAT Payable", AccountType.LIABILITY);
        };
    }

    private void seedAccount(ChartOfAccountRepository repository, String code, String name, AccountType type) {
        repository.findByAccountCode(code).orElseGet(() -> {
            ChartOfAccount account = new ChartOfAccount();
            account.setAccountCode(code);
            account.setAccountName(name);
            account.setAccountType(type);
            account.setSystemAccount(true);
            return repository.save(account);
        });
    }

    private Category seedCategory(CategoryRepository repository, String name) {
        return repository.findAll().stream()
                .filter(category -> category.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    return repository.save(category);
                });
    }

    private SubCategory seedSubCategory(SubCategoryRepository repository, Category category, String name) {
        return repository.findByCategoryIdAndDeletedFalse(category.getId()).stream()
                .filter(subCategory -> subCategory.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    SubCategory subCategory = new SubCategory();
                    subCategory.setName(name);
                    subCategory.setCategory(category);
                    return repository.save(subCategory);
                });
    }

    private Brand seedBrand(BrandRepository repository, String name) {
        return repository.findAll().stream()
                .filter(brand -> brand.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Brand brand = new Brand();
                    brand.setName(name);
                    return repository.save(brand);
                });
    }

    private Unit findUnit(UnitRepository repository, String name) {
        return repository.findAll().stream()
                .filter(unit -> unit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }

    private Territory seedTerritory(TerritoryRepository repository, String code, String name, String marketName) {
        return repository.findAll().stream()
                .filter(territory -> territory.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseGet(() -> {
                    Territory territory = new Territory();
                    territory.setCode(code);
                    territory.setName(name);
                    territory.setMarketName(marketName);
                    return repository.save(territory);
                });
    }

    private void seedRetailer(RetailerRepository repository, Territory territory, String code, String name,
                              String owner, String mobile, String email, String address, String marketName,
                              String creditLimit, String openingBalance) {
        if (repository.existsByRetailerCode(code)) return;
        Retailer retailer = new Retailer();
        retailer.setRetailerCode(code);
        retailer.setRetailerName(name);
        retailer.setOwnerName(owner);
        retailer.setMobileNumber(mobile);
        retailer.setEmail(email);
        retailer.setAddress(address);
        retailer.setTerritory(territory);
        retailer.setMarketName(marketName);
        retailer.setCreditLimit(new BigDecimal(creditLimit));
        retailer.setOpeningBalance(new BigDecimal(openingBalance));
        retailer.setCurrentDueBalance(new BigDecimal(openingBalance));
        repository.save(retailer);
    }

    private void seedProduct(ProductRepository productRepository, StockTransactionRepository stockRepository,
                             Warehouse warehouse, String code, String name, String description,
                             MaterialType materialType, Category category, SubCategory subCategory, Brand brand, Unit unit,
                             String packSize, String purchasePrice, String salesPrice, String retailerPrice,
                             String vatPercent, String discountPercent, String batchNumber, String openingStock) {
        if (productRepository.existsByProductCode(code)) return;
        Product product = new Product();
        product.setProductCode(code);
        product.setProductName(name + " - " + description);
        product.setMaterialType(materialType);
        product.setCategory(category);
        product.setSubCategory(subCategory);
        product.setBrand(brand);
        product.setUnit(unit);
        product.setPackSize(packSize);
        product.setPurchasePrice(new BigDecimal(purchasePrice));
        product.setSalesPrice(new BigDecimal(salesPrice));
        product.setRetailerPrice(new BigDecimal(retailerPrice));
        product.setVatPercent(new BigDecimal(vatPercent));
        product.setDiscountPercent(new BigDecimal(discountPercent));
        product.setBatchNumber(batchNumber);
        product.setReorderLevel(new BigDecimal("50"));
        Product saved = productRepository.save(product);

        StockTransaction opening = new StockTransaction();
        opening.setProduct(saved);
        opening.setWarehouse(warehouse);
        opening.setTransactionType(StockTransactionType.OPENING);
        opening.setQuantity(new BigDecimal(openingStock));
        opening.setBatchNumber(batchNumber);
        opening.setReferenceType("DEMO_SEED");
        opening.setReferenceId(saved.getId());
        opening.setRemarks("Opening stock for demo sales and delivery flow");
        stockRepository.save(opening);
    }
}
