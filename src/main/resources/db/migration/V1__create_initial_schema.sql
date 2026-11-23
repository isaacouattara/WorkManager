-- =====================================================================
-- V1 : schéma initial de WorkManager (état final décrit dans la doc, §6)
-- Une migration appliquée ne doit JAMAIS être modifiée : toute évolution
-- passe par un nouveau fichier (V2__..., V3__...).
-- =====================================================================

-- ---------- users ----------
CREATE TABLE users (
    id            UUID         PRIMARY KEY,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL
);

-- Unicité de l'e-mail sans tenir compte de la casse
CREATE UNIQUE INDEX uq_users_email_lower ON users (LOWER(email));

-- ---------- companies ----------
CREATE TABLE companies (
    id         UUID         PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

-- ---------- company_members ----------
CREATE TABLE company_members (
    id         UUID        PRIMARY KEY,
    user_id    UUID        NOT NULL,
    company_id UUID        NOT NULL,
    role       VARCHAR(20) NOT NULL,
    active     BOOLEAN     NOT NULL,
    joined_at  TIMESTAMP   NOT NULL,

    CONSTRAINT fk_company_member_user    FOREIGN KEY (user_id)    REFERENCES users (id),
    CONSTRAINT fk_company_member_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT chk_company_member_role   CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER')),
    -- Nécessaire pour les clés étrangères composites de project_members
    CONSTRAINT uq_company_member_id_company UNIQUE (id, company_id)
);

-- Un utilisateur : une seule appartenance ACTIVE
CREATE UNIQUE INDEX uq_one_active_company_per_user
    ON company_members (user_id)
    WHERE active = TRUE;

-- Une entreprise : un seul OWNER ACTIF
CREATE UNIQUE INDEX uq_one_active_owner_per_company
    ON company_members (company_id)
    WHERE active = TRUE AND role = 'OWNER';

-- ---------- invitations ----------
CREATE TABLE invitations (
    id         UUID         PRIMARY KEY,
    company_id UUID         NOT NULL,
    email      VARCHAR(255) NOT NULL,
    token      VARCHAR(255) NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP    NOT NULL,

    CONSTRAINT fk_invitation_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT uq_invitation_token   UNIQUE (token),
    CONSTRAINT chk_invitation_status CHECK (status IN ('PENDING', 'ACCEPTED', 'REFUSED', 'EXPIRED'))
);

-- ---------- projects ----------
CREATE TABLE projects (
    id          UUID         PRIMARY KEY,
    company_id  UUID         NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL,

    CONSTRAINT fk_project_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT chk_project_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'ARCHIVED')),
    -- Nécessaire pour la clé étrangère composite de project_members
    CONSTRAINT uq_project_id_company UNIQUE (id, company_id)
);

-- ---------- project_members ----------
CREATE TABLE project_members (
    id                UUID      PRIMARY KEY,
    project_id        UUID      NOT NULL,
    company_member_id UUID      NOT NULL,
    company_id        UUID      NOT NULL,
    assigned_at       TIMESTAMP NOT NULL,

    CONSTRAINT uq_project_member UNIQUE (project_id, company_member_id),

    -- Les deux clés composites garantissent que le projet ET le membre
    -- appartiennent à la même entreprise (elles incluent déjà l'existence du projet et du membre)
    CONSTRAINT fk_project_member_project_company
        FOREIGN KEY (project_id, company_id)
        REFERENCES projects (id, company_id),
    CONSTRAINT fk_project_member_member_company
        FOREIGN KEY (company_member_id, company_id)
        REFERENCES company_members (id, company_id)
);
