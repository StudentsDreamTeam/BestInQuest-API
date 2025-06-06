DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'postgres')
THEN
    CREATE ROLE postgres WITH LOGIN PASSWORD '561928' SUPERUSER;
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS users (
    id            serial    PRIMARY KEY,
    name           varchar(255) NOT NULL CHECK (length(name) > 0),
    email         varchar(255) NOT NULL UNIQUE CHECK (email ~* '^[^@]+@[^@]+\.[^@]+$'),
    password        varchar(255) NOT NULL CHECK (length(password) >= 8),
    xp            bigint    NOT NULL CHECK (xp >= 0),
    level       bigint    NOT NULL CHECK (level >= 0),
    registration_date     timestamptz NOT NULL,
    last_online_date timestamptz NOT NULL
        CHECK (last_online_date >= registration_date),
    streak        bigint    NOT NULL CHECK (streak >= 0)
);

CREATE TABLE IF NOT EXISTS projects (
    id                serial    PRIMARY KEY,
    name          varchar(255) NOT NULL CHECK (length(name) > 0),
    description          varchar(255),
    owner          bigint    NOT NULL CHECK (owner > 0),
    status            varchar(255) NOT NULL CHECK (length(status) > 0),
    creation_date     timestamptz NOT NULL,
    project_items  bigint    CHECK (project_items >= 0),
    done          boolean   NOT NULL
);

CREATE TABLE IF NOT EXISTS version_history (
    id                          serial    PRIMARY KEY,
    title                   varchar(255) NOT NULL CHECK (length(title) > 0),
    description                    varchar(255),
    status                      varchar(255) NOT NULL CHECK (length(status) > 0),
    priority                   varchar(255) NOT NULL CHECK (length(priority) > 0),
    difficulty                   bigint    NOT NULL CHECK (difficulty >= 0),
    author                       bigint    NOT NULL CHECK (author > 0),
    executor                 bigint    NOT NULL CHECK (executor > 0),
    update_date             timestamptz NOT NULL,
    fast_done_bonus bigint    NOT NULL CHECK (fast_done_bonus >= 0),
    combo                       boolean   NOT NULL,
    reward_xp                  bigint    NOT NULL CHECK (reward_xp >= 0),
    reward_currency              bigint    NOT NULL CHECK (reward_currency >= 0),
    deadline                     timestamptz      CHECK (deadline >= CURRENT_DATE),
    linked_task_id            bigint,
    sphere                      varchar(255),
    duration                    numeric(21,0) NOT NULL
);

CREATE TABLE IF NOT EXISTS clans (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    leader         bigint    NOT NULL CHECK (leader > 0),
    creation_date timestamptz NOT NULL,
    rating       bigint    NOT NULL CHECK (rating >= 0),
    project        bigint    NOT NULL CHECK (project > 0)
);

CREATE TABLE IF NOT EXISTS clans_participants (
    id              serial    PRIMARY KEY,
    clan            bigint    NOT NULL CHECK (clan > 0),
    user_id    bigint    NOT NULL CHECK (user_id > 0),
    role            varchar(255) NOT NULL CHECK (length(role) > 0),
    joining_date timestamptz NOT NULL,
    UNIQUE (clan, user_id)
);

CREATE TABLE IF NOT EXISTS pvp_competitions (
    id              serial    PRIMARY KEY,
    player_1         bigint    NOT NULL CHECK (player_1 > 0),
    player_2         bigint    NOT NULL CHECK (player_2 > 0 AND player_2 <> player_1),
    status          varchar(255) NOT NULL CHECK (length(status) > 0),
    winner      bigint    NOT NULL CHECK (winner IN (player_1, player_2)),
    reward_xp      bigint    NOT NULL CHECK (reward_xp >= 0),
    reward_currency  bigint    NOT NULL CHECK (reward_currency >= 0),
    start_date     timestamptz NOT NULL,
    end_date  timestamptz NOT NULL CHECK (end_date >= start_date)
);

CREATE TABLE IF NOT EXISTS items (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    description      varchar(255),
    rarity      varchar(255) NOT NULL CHECK (length(rarity) > 0),
    xp_multiplier     float    NOT NULL CHECK (xp_multiplier >= 0),
    currency_multiplier float    NOT NULL,
    duration  numeric(21,0) NOT NULL,
    cost     bigint    NOT NULL CHECK (cost >= 0)
);

CREATE TABLE IF NOT EXISTS users_inventory (
    id             serial    PRIMARY KEY,
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    item        bigint    NOT NULL CHECK (item > 0),
    amount     bigint    NOT NULL CHECK (amount >= 0),
    acquire_date timestamptz NOT NULL,
    UNIQUE (user_id, item)
);

CREATE TABLE IF NOT EXISTS shop (
    id            serial    PRIMARY KEY,
    item       bigint    NOT NULL CHECK (item > 0),
    cost          bigint    NOT NULL CHECK (cost >= 0),
    availability   varchar(255) NOT NULL CHECK (length(availability) > 0),
    UNIQUE (item)
);

CREATE TABLE IF NOT EXISTS projects_participants (
    id             serial    PRIMARY KEY,
    project         bigint    NOT NULL CHECK (project > 0),
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    role           varchar(255) NOT NULL CHECK (length(role) > 0),
    update_date timestamptz NOT NULL,
    UNIQUE (project, user_id)
);

CREATE TABLE IF NOT EXISTS tasks_pointers (
    id             serial    PRIMARY KEY,
--    task         bigint,
    project         bigint,
    creation_date  timestamptz,
    linked_task_id integer,
    UNIQUE (linked_task_id, project)
);

CREATE TABLE IF NOT EXISTS commentaries (
    id             serial    PRIMARY KEY,
    task         bigint    NOT NULL CHECK (task > 0),
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    text          varchar(255) NOT NULL CHECK (length(text) > 0),
    creation_date  timestamptz NOT NULL
);

CREATE TABLE IF NOT EXISTS spheres (
    id       serial    PRIMARY KEY,
    name varchar(255) NOT NULL CHECK (length(name) > 0),
    project   bigint    NOT NULL CHECK (project > 0)
);

CREATE TABLE IF NOT EXISTS tasks_spheres (
    task bigint NOT NULL CHECK (task > 0),
    sphere  bigint NOT NULL CHECK (sphere > 0),
    PRIMARY KEY (task, sphere)
);

CREATE TABLE IF NOT EXISTS achievements (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    description      varchar(255) NOT NULL CHECK (length(description) > 0),
    required_value  bigint    NOT NULL CHECK (required_value >= 0),
    icon        varchar(255),
    type        varchar(255) NOT NULL CHECK (length(type) > 0)
);

CREATE TABLE IF NOT EXISTS users_achievements (
    id               serial    PRIMARY KEY,
    user_id     bigint    NOT NULL CHECK (user_id > 0),
    achievement       bigint    NOT NULL CHECK (achievement > 0),
    acquire_date   timestamptz NOT NULL,
    UNIQUE (user_id, achievement)
);

CREATE TABLE IF NOT EXISTS income (
    id            serial    PRIMARY KEY,
    user_id  bigint    NOT NULL CHECK (user_id > 0),
    amount    bigint    NOT NULL CHECK (amount >= 0),
    date          timestamptz NOT NULL,
    description      varchar(255)
);

CREATE TABLE IF NOT EXISTS spendings (
    id            serial    PRIMARY KEY,
    user_id  bigint    NOT NULL CHECK (user_id > 0),
    amount    bigint    NOT NULL CHECK (amount >= 0),
    date          timestamptz NOT NULL,
    description      varchar(255)
);

CREATE TABLE IF NOT EXISTS teams (
    id          serial    PRIMARY KEY,
    project_id  bigint    NOT NULL CHECK (project_id > 0),
    name    varchar(255) NOT NULL CHECK (length(name) > 0),
    description    varchar(255)
);

CREATE TABLE IF NOT EXISTS teams_participants (
    id               serial    PRIMARY KEY,
    team          bigint    NOT NULL CHECK (team > 0),
    user_id     bigint    NOT NULL CHECK (user_id > 0),
    update_date  timestamptz NOT NULL,
    UNIQUE (team, user_id)
);

CREATE TABLE IF NOT EXISTS project_items (
    id        serial    PRIMARY KEY,
    project    bigint    NOT NULL CHECK (project > 0),
    item   bigint    NOT NULL CHECK (item > 0),
    UNIQUE (project, item)
);

CREATE TABLE IF NOT EXISTS xp_gains (
    id            serial    PRIMARY KEY,
    amount    bigint    NOT NULL,
    date          timestamptz NOT NULL,
    user_id  bigint    NOT NULL CHECK (user_id > 0)
);

CREATE TABLE level_requirements (
    level INTEGER PRIMARY KEY,
    required_xp BIGINT NOT NULL
);

--следующие +100 * (n-1)
INSERT INTO level_requirements (level, required_xp) VALUES (1, 0);
INSERT INTO level_requirements (level, required_xp) VALUES (2, 100);
INSERT INTO level_requirements (level, required_xp) VALUES (3, 300);
INSERT INTO level_requirements (level, required_xp) VALUES (4, 600);
INSERT INTO level_requirements (level, required_xp) VALUES (5, 1000);
INSERT INTO level_requirements (level, required_xp) VALUES (6, 1500);
INSERT INTO level_requirements (level, required_xp) VALUES (7, 2100);
INSERT INTO level_requirements (level, required_xp) VALUES (8, 2800);
INSERT INTO level_requirements (level, required_xp) VALUES (9, 3600);
INSERT INTO level_requirements (level, required_xp) VALUES (10, 4500);
INSERT INTO level_requirements (level, required_xp) VALUES (11, 5500);
INSERT INTO level_requirements (level, required_xp) VALUES (12, 6600);
INSERT INTO level_requirements (level, required_xp) VALUES (13, 7800);
INSERT INTO level_requirements (level, required_xp) VALUES (14, 9100);
INSERT INTO level_requirements (level, required_xp) VALUES (15, 10500);
INSERT INTO level_requirements (level, required_xp) VALUES (16, 12000);
INSERT INTO level_requirements (level, required_xp) VALUES (17, 13600);
INSERT INTO level_requirements (level, required_xp) VALUES (18, 15300);
INSERT INTO level_requirements (level, required_xp) VALUES (19, 17100);
INSERT INTO level_requirements (level, required_xp) VALUES (20, 19000);
INSERT INTO level_requirements (level, required_xp) VALUES (21, 21000);
INSERT INTO level_requirements (level, required_xp) VALUES (22, 23100);
INSERT INTO level_requirements (level, required_xp) VALUES (23, 25300);
INSERT INTO level_requirements (level, required_xp) VALUES (24, 27600);
INSERT INTO level_requirements (level, required_xp) VALUES (25, 30000);
INSERT INTO level_requirements (level, required_xp) VALUES (26, 32500);
INSERT INTO level_requirements (level, required_xp) VALUES (27, 35100);
INSERT INTO level_requirements (level, required_xp) VALUES (28, 37800);
INSERT INTO level_requirements (level, required_xp) VALUES (29, 40600);
INSERT INTO level_requirements (level, required_xp) VALUES (30, 43500);
INSERT INTO level_requirements (level, required_xp) VALUES (31, 46500);
INSERT INTO level_requirements (level, required_xp) VALUES (32, 49600);
INSERT INTO level_requirements (level, required_xp) VALUES (33, 52800);
INSERT INTO level_requirements (level, required_xp) VALUES (34, 56100);
INSERT INTO level_requirements (level, required_xp) VALUES (35, 59500);
INSERT INTO level_requirements (level, required_xp) VALUES (36, 63000);
INSERT INTO level_requirements (level, required_xp) VALUES (37, 66600);
INSERT INTO level_requirements (level, required_xp) VALUES (38, 70300);
INSERT INTO level_requirements (level, required_xp) VALUES (39, 74100);
INSERT INTO level_requirements (level, required_xp) VALUES (40, 78000);
INSERT INTO level_requirements (level, required_xp) VALUES (41, 82000);
INSERT INTO level_requirements (level, required_xp) VALUES (42, 86100);
INSERT INTO level_requirements (level, required_xp) VALUES (43, 90300);
INSERT INTO level_requirements (level, required_xp) VALUES (44, 94600);
INSERT INTO level_requirements (level, required_xp) VALUES (45, 99000);
INSERT INTO level_requirements (level, required_xp) VALUES (46, 103500);
INSERT INTO level_requirements (level, required_xp) VALUES (47, 108100);
INSERT INTO level_requirements (level, required_xp) VALUES (48, 112800);
INSERT INTO level_requirements (level, required_xp) VALUES (49, 117600);
INSERT INTO level_requirements (level, required_xp) VALUES (50, 122500);

ALTER TABLE projects ADD CONSTRAINT project_fk3 FOREIGN KEY (owner) REFERENCES users(id);
ALTER TABLE version_history ADD CONSTRAINT version_history_fk6 FOREIGN KEY (author) REFERENCES users(id);
ALTER TABLE version_history ADD CONSTRAINT version_history_fk7 FOREIGN KEY (executor) REFERENCES users(id);
ALTER TABLE version_history ADD CONSTRAINT version_history_fk8 FOREIGN KEY (linked_task_id) REFERENCES tasks_pointers(id);
ALTER TABLE clans ADD CONSTRAINT clans_fk2 FOREIGN KEY (leader) REFERENCES users(id);
ALTER TABLE clans ADD CONSTRAINT clans_fk5 FOREIGN KEY (project) REFERENCES projects(id);
ALTER TABLE clans_participants ADD CONSTRAINT clans_participants_fk1 FOREIGN KEY (clan) REFERENCES clans(id);
ALTER TABLE clans_participants ADD CONSTRAINT clans_participants_fk2 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE pvp_competitions ADD CONSTRAINT pvp_competitions_fk1 FOREIGN KEY (player_1) REFERENCES users(id);
ALTER TABLE pvp_competitions ADD CONSTRAINT pvp_competitions_fk2 FOREIGN KEY (player_2) REFERENCES users(id);
ALTER TABLE pvp_competitions ADD CONSTRAINT pvp_competitions_fk4 FOREIGN KEY (winner) REFERENCES users(id);
ALTER TABLE users_inventory ADD CONSTRAINT users_inventory_fk1 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE users_inventory ADD CONSTRAINT users_inventory_fk2 FOREIGN KEY (item) REFERENCES items(id);
ALTER TABLE shop ADD CONSTRAINT shop_fk1 FOREIGN KEY (item) REFERENCES items(id);
ALTER TABLE projects_participants ADD CONSTRAINT projects_participants_fk1 FOREIGN KEY (project) REFERENCES projects(id);
ALTER TABLE projects_participants ADD CONSTRAINT projects_participants_fk2 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE tasks_pointers ADD CONSTRAINT tasks_pointers_fk1 FOREIGN KEY (linked_task_id) REFERENCES version_history(id);
ALTER TABLE tasks_pointers ADD CONSTRAINT tasks_pointers_fk2 FOREIGN KEY (project) REFERENCES projects(id);
ALTER TABLE commentaries ADD CONSTRAINT commentaries_fk1 FOREIGN KEY (task) REFERENCES tasks_pointers(id);
ALTER TABLE commentaries ADD CONSTRAINT commentaries_fk2 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE spheres ADD CONSTRAINT spheres_fk2 FOREIGN KEY (project) REFERENCES projects(id);
ALTER TABLE tasks_spheres ADD CONSTRAINT tasks_spheres_fk0 FOREIGN KEY (task) REFERENCES version_history(id);
ALTER TABLE tasks_spheres ADD CONSTRAINT tasks_spheres_fk1 FOREIGN KEY (sphere) REFERENCES spheres(id);
ALTER TABLE users_achievements ADD CONSTRAINT users_achievements_fk1 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE users_achievements ADD CONSTRAINT users_achievements_fk2 FOREIGN KEY (achievement) REFERENCES achievements(id);
ALTER TABLE income ADD CONSTRAINT income_fk1 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE spendings ADD CONSTRAINT spendings_fk1 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE teams ADD CONSTRAINT teams_fk1 FOREIGN KEY (project_id) REFERENCES projects(id);
ALTER TABLE teams_participants ADD CONSTRAINT teams_participants_fk1 FOREIGN KEY (team) REFERENCES teams(id);
ALTER TABLE teams_participants ADD CONSTRAINT teams_participants_fk2 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE project_items ADD CONSTRAINT project_items_fk1 FOREIGN KEY (project) REFERENCES projects(id);
ALTER TABLE project_items ADD CONSTRAINT project_items_fk2 FOREIGN KEY (item) REFERENCES items(id);
ALTER TABLE xp_gains ADD CONSTRAINT xp_gains_fk3 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE version_history
ADD COLUMN applied_xp_reward BIGINT DEFAULT 0,
ADD COLUMN applied_currency_reward BIGINT DEFAULT 0;
