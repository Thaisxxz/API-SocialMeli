-- =========================
-- USERS (idempotente) - sem VALUES() deprecated
-- =========================
INSERT INTO app_user (nickname, password, name, email, phone)
    VALUES ('Thaisamorimvendas', '$2a$10$8YxDL.76Hd7ZXIXi5EC3..IFIyVBrSs/R8Ftclwnp2JiMLoXzEGL', 'Seller User', 'thaisamorimvendas@mail.com', '11 99999-2342')
        AS new
ON DUPLICATE KEY UPDATE
                     name  = new.name,
                     phone = new.phone;

INSERT INTO app_user (nickname, password, name, email, phone)
    VALUES ('thaisamorimcompras', '$2a$10$8YxDL.76Hd7ZXIXi5EC3..IFIyVBrSs/R8Ftclwnp2JiMLoXzEGL', 'Buyer User', 'thaisamorimcompras@mail.com', '11 99999-0004')
        AS new
ON DUPLICATE KEY UPDATE
                     name  = new.name,
                     phone = new.phone;


-- =========================
-- PROFILES (idempotente)
-- =========================
INSERT INTO profile (type, active, user_id)
SELECT 'SELLER', true, u.id
FROM app_user u
WHERE u.nickname = 'Thaisamorimvendas'
  AND NOT EXISTS (
    SELECT 1 FROM profile p WHERE p.user_id = u.id AND p.type = 'SELLER'
);

INSERT INTO profile (type, active, user_id)
SELECT 'BUYER', true, u.id
FROM app_user u
WHERE u.nickname = 'thaisamorimcompras'
  AND NOT EXISTS (
    SELECT 1 FROM profile p WHERE p.user_id = u.id AND p.type = 'BUYER'
);


-- =========================
-- CATEGORY (idempotente) - sem VALUES() deprecated
-- =========================
INSERT INTO category (name, description, active)
    VALUES ('Eletrônicos', 'Categoria de eletrônicos', true)
        AS new
ON DUPLICATE KEY UPDATE
                     description = new.description,
                     active      = new.active;


-- =========================
-- PRODUCTS (idempotente)
-- (mantido com NOT EXISTS porque product.name não é unique)
-- =========================
INSERT INTO product (name, type, description, price, active, created_at, category_id)
SELECT 'Teclado Mecânico', 'Periférico', 'Teclado mecânico ABNT2', 299.90, true, NOW(), c.id
FROM category c
WHERE c.name = 'Eletrônicos'
  AND NOT EXISTS (
    SELECT 1 FROM product p WHERE p.name = 'Teclado Mecânico'
);

INSERT INTO product (name, type, description, price, active, created_at, category_id)
SELECT 'Mouse Gamer', 'Periférico', 'Mouse RGB 16000 DPI', 159.90, true, NOW(), c.id
FROM category c
WHERE c.name = 'Eletrônicos'
  AND NOT EXISTS (
    SELECT 1 FROM product p WHERE p.name = 'Mouse Gamer'
);


-- =========================
-- FOLLOWER (buyer follows seller) (idempotente) - sem VALUES() deprecated
-- Requer UNIQUE(follower_id, seller_id)
-- =========================
INSERT INTO follower (follower_id, seller_id, followed_at)
SELECT pb.id, ps.id, NOW()
FROM profile pb
         JOIN app_user ub ON ub.id = pb.user_id
         JOIN profile ps
         JOIN app_user us ON us.id = ps.user_id
WHERE ub.nickname = 'thaisamorimcompras' AND pb.type = 'BUYER'
  AND us.nickname = 'Thaisamorimvendas' AND ps.type = 'SELLER'
ON DUPLICATE KEY UPDATE
    followed_at = NOW();