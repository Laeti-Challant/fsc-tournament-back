-- Séquence pour l'héritage JOINED Event/Tournament
-- La table event contient les champs communs
CREATE TABLE event (
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    dtype       VARCHAR(31) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    event_date  DATE NOT NULL,
    registration_deadline DATE NOT NULL,
    status      VARCHAR(20) NOT NULL,
    max_participants INTEGER NOT NULL,
    nb_rounds   INTEGER NOT NULL
);

-- La table tournament ne contient que les champs spécifiques
-- Son id est une FK vers event.id
CREATE TABLE tournament (
    id          BIGINT NOT NULL PRIMARY KEY,
    location    VARCHAR(100),
    address     VARCHAR(150),
    postal_code VARCHAR(5),
    city        VARCHAR(50),
    FOREIGN KEY (id) REFERENCES event(id)
);

CREATE TABLE users (
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    pseudo        VARCHAR(30) NOT NULL UNIQUE,
    role          VARCHAR(20) NOT NULL DEFAULT 'PLAYER',
    active        BOOLEAN NOT NULL DEFAULT true,
    last_login    TIMESTAMP,
    created_at    TIMESTAMP NOT NULL
);

CREATE TABLE coach (
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    coach_pseudo  VARCHAR(30) NOT NULL,
    team_name     VARCHAR(50),
    race          VARCHAR(50) NOT NULL,
    eating        BOOLEAN NOT NULL DEFAULT false,
    vegetarian    BOOLEAN NOT NULL DEFAULT false,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    roster_status VARCHAR(20) NOT NULL DEFAULT 'NOT_SUBMITTED',
    roster_link   VARCHAR(500),
    substitute    BOOLEAN NOT NULL DEFAULT false,
    user_id       BIGINT NOT NULL,
    event_id      BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES event(id),
    CONSTRAINT uk_coach_user_event UNIQUE (user_id, event_id)
);

CREATE TABLE round (
    id           BIGSERIAL NOT NULL PRIMARY KEY,
    round_number INTEGER NOT NULL,
    pairing_type VARCHAR(10) NOT NULL DEFAULT 'SWISS',
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    event_id     BIGINT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event(id)
);

CREATE TABLE match (
    id       BIGSERIAL NOT NULL PRIMARY KEY,
    status   VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    round_id BIGINT NOT NULL,
    coach1_id BIGINT NOT NULL,
    coach2_id BIGINT NOT NULL,
    FOREIGN KEY (round_id) REFERENCES round(id),
    FOREIGN KEY (coach1_id) REFERENCES coach(id),
    FOREIGN KEY (coach2_id) REFERENCES coach(id)
);

CREATE TABLE coach_result (
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    result         VARCHAR(10) NOT NULL,
    touchdowns     INTEGER NOT NULL DEFAULT 0,
    casualties     INTEGER NOT NULL DEFAULT 0,
    objectives     INTEGER NOT NULL DEFAULT 0,
    passes         INTEGER NOT NULL DEFAULT 0,
    foul_actions   INTEGER NOT NULL DEFAULT 0,
    bonus_objective BOOLEAN NOT NULL DEFAULT false,
    match_id       BIGINT NOT NULL,
    coach_id       BIGINT NOT NULL,
    FOREIGN KEY (match_id) REFERENCES match(id),
    FOREIGN KEY (coach_id) REFERENCES coach(id)
);

CREATE TABLE tournament_rules (
    id                    BIGSERIAL NOT NULL PRIMARY KEY,
    budget_po             INTEGER NOT NULL,
    psp_pool              SMALLINT NOT NULL,
    max_skills_per_player SMALLINT NOT NULL,
    resurrection_mode     BOOLEAN NOT NULL DEFAULT true,
    mogette_psp_value     SMALLINT NOT NULL,
    mogette_po_value      INTEGER NOT NULL,
    notes_text            TEXT,
    roster_text           TEXT,
    tournament_id         BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id)
);

CREATE TABLE allowed_inducement (
    id       BIGSERIAL NOT NULL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    min_qty  SMALLINT NOT NULL DEFAULT 0,
    max_qty  SMALLINT NOT NULL,
    rules_id BIGINT NOT NULL,
    FOREIGN KEY (rules_id) REFERENCES tournament_rules(id)
);

CREATE TABLE roster_category (
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    race_name      VARCHAR(50) NOT NULL,
    is_minus       BOOLEAN NOT NULL,
    category_value SMALLINT NOT NULL,
    rules_id       BIGINT NOT NULL,
    FOREIGN KEY (rules_id) REFERENCES tournament_rules(id)
);

CREATE TABLE menu (
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    label         VARCHAR(50) NOT NULL,
    description   VARCHAR(255),
    display_order INTEGER NOT NULL,
    tournament_id BIGINT NOT NULL,
    FOREIGN KEY (tournament_id) REFERENCES tournament(id)
);
