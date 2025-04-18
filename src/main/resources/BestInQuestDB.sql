CREATE TABLE IF NOT EXISTS "Users" (
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

CREATE TABLE IF NOT EXISTS "Projects" (
    id                serial    PRIMARY KEY,
    name          varchar(255) NOT NULL CHECK (length(name) > 0),
    description          varchar(255),
    owner          bigint    NOT NULL CHECK (owner > 0),
    status            varchar(255) NOT NULL CHECK (length(status) > 0),
    creation_date     timestamptz NOT NULL,
    project_items  bigint    CHECK (project_items >= 0),
    done          boolean   NOT NULL
);

CREATE TABLE IF NOT EXISTS "Version_history" (
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
    deadline                     date      CHECK (deadline >= CURRENT_DATE),
    linked_task_id            bigint    NOT NULL
);

CREATE TABLE IF NOT EXISTS "Clans" (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    leader         bigint    NOT NULL CHECK (leader > 0),
    creation_date timestamptz NOT NULL,
    rating       bigint    NOT NULL CHECK (rating >= 0),
    project        bigint    NOT NULL CHECK (project > 0)
);

CREATE TABLE IF NOT EXISTS "Clans_Participants" (
    id              serial    PRIMARY KEY,
    clan            bigint    NOT NULL CHECK (clan > 0),
    user_id    bigint    NOT NULL CHECK (user_id > 0),
    role            varchar(255) NOT NULL CHECK (length(role) > 0),
    joining_date timestamptz NOT NULL,
    UNIQUE (clan, user_id)
);

CREATE TABLE IF NOT EXISTS "PvP_Competitions" (
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

CREATE TABLE IF NOT EXISTS "Items" (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    description      varchar(255),
    rarity      varchar(255) NOT NULL CHECK (length(rarity) > 0),
    xp_multiplier     bigint    NOT NULL CHECK (xp_multiplier >= 0),
    currency_multiplier bigint    NOT NULL CHECK (currency_multiplier >= 0),
    duration  varchar(255) NOT NULL CHECK (length(duration) > 0),
    cost     bigint    NOT NULL CHECK (cost >= 0)
);

CREATE TABLE IF NOT EXISTS "Users_Inventory" (
    id             serial    PRIMARY KEY,
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    item        bigint    NOT NULL CHECK (item > 0),
    amount     bigint    NOT NULL CHECK (amount >= 0),
    acquire_date timestamptz NOT NULL,
    UNIQUE (user_id, item)
);

CREATE TABLE IF NOT EXISTS "Shop" (
    id            serial    PRIMARY KEY,
    item       bigint    NOT NULL CHECK (item > 0),
    cost          bigint    NOT NULL CHECK (cost >= 0),
    availability   varchar(255) NOT NULL CHECK (length(availability) > 0),
    UNIQUE (item)
);

CREATE TABLE IF NOT EXISTS "Projects_participants" (
    id             serial    PRIMARY KEY,
    project         bigint    NOT NULL CHECK (project > 0),
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    role           varchar(255) NOT NULL CHECK (length(role) > 0),
    update_date timestamptz NOT NULL,
    UNIQUE (project, user_id)
);

CREATE TABLE IF NOT EXISTS "Tasks_pointers" (
    id             serial    PRIMARY KEY,
    task         bigint    NOT NULL CHECK (task > 0),
    project         bigint    NOT NULL CHECK (project > 0),
    creation_date  timestamptz NOT NULL,
    UNIQUE (task, project)
);

CREATE TABLE IF NOT EXISTS "Commentaries" (
    id             serial    PRIMARY KEY,
    task         bigint    NOT NULL CHECK (task > 0),
    user_id   bigint    NOT NULL CHECK (user_id > 0),
    text          varchar(255) NOT NULL CHECK (length(text) > 0),
    creation_date  timestamptz NOT NULL
);

CREATE TABLE IF NOT EXISTS "Spheres" (
    id       serial    PRIMARY KEY,
    name varchar(255) NOT NULL CHECK (length(name) > 0),
    project   bigint    NOT NULL CHECK (project > 0)
);

CREATE TABLE IF NOT EXISTS "Tasks_Spheres" (
    task bigint NOT NULL CHECK (task > 0),
    sphere  bigint NOT NULL CHECK (sphere > 0),
    PRIMARY KEY (task, sphere)
);

CREATE TABLE IF NOT EXISTS "Achievements" (
    id            serial    PRIMARY KEY,
    name      varchar(255) NOT NULL UNIQUE CHECK (length(name) > 0),
    description      varchar(255) NOT NULL CHECK (length(description) > 0),
    required_xp  bigint    NOT NULL CHECK (required_xp >= 0),
    icon        varchar(255) NOT NULL CHECK (length(icon) > 0)
);

CREATE TABLE IF NOT EXISTS "Users_Achievements" (
    id               serial    PRIMARY KEY,
    user_id     bigint    NOT NULL CHECK (user_id > 0),
    achievement       bigint    NOT NULL CHECK (achievement > 0),
    acquire_date   timestamptz NOT NULL,
    UNIQUE (user_id, achievement)
);

CREATE TABLE IF NOT EXISTS "Income" (
    id            serial    PRIMARY KEY,
    user_id  bigint    NOT NULL CHECK (user_id > 0),
    amount    bigint    NOT NULL CHECK (amount >= 0),
    date          timestamptz NOT NULL,
    description      varchar(255)
);

CREATE TABLE IF NOT EXISTS "Spendings" (
    id            serial    PRIMARY KEY,
    user_id  bigint    NOT NULL CHECK (user_id > 0),
    amount    bigint    NOT NULL CHECK (amount >= 0),
    date          timestamptz NOT NULL,
    description      varchar(255)
);

CREATE TABLE IF NOT EXISTS "Teams" (
    id          serial    PRIMARY KEY,
    project_id  bigint    NOT NULL CHECK (project_id > 0),
    name    varchar(255) NOT NULL CHECK (length(name) > 0),
    description    varchar(255)
);

CREATE TABLE IF NOT EXISTS "Teams_Participants" (
    id               serial    PRIMARY KEY,
    team          bigint    NOT NULL CHECK (team > 0),
    user_id     bigint    NOT NULL CHECK (user_id > 0),
    update_date  timestamptz NOT NULL,
    UNIQUE (team, user_id)
);

CREATE TABLE IF NOT EXISTS "Project_items" (
    id        serial    PRIMARY KEY,
    project    bigint    NOT NULL CHECK (project > 0),
    item   bigint    NOT NULL CHECK (item > 0),
    UNIQUE (project, item)
);

CREATE TABLE IF NOT EXISTS "Xp_gains" (
    id            serial    PRIMARY KEY,
    amount    bigint    NOT NULL CHECK (amount >= 0),
    date          timestamptz NOT NULL,
    user_id  bigint    NOT NULL CHECK (user_id > 0)
);



ALTER TABLE "Projects" ADD CONSTRAINT "project_fk3" FOREIGN KEY ("owner") REFERENCES "Users"("id");
ALTER TABLE "Version_history" ADD CONSTRAINT "Version_history_fk6" FOREIGN KEY ("author") REFERENCES "Users"("id");

ALTER TABLE "Version_history" ADD CONSTRAINT "Version_history_fk7" FOREIGN KEY ("executor") REFERENCES "Users"("id");
ALTER TABLE "Clans" ADD CONSTRAINT "Clans_fk2" FOREIGN KEY ("leader") REFERENCES "Users"("id");

ALTER TABLE "Clans" ADD CONSTRAINT "Clans_fk5" FOREIGN KEY ("project") REFERENCES "Projects"("id");
ALTER TABLE "Clans_Participants" ADD CONSTRAINT "Clans_Participants_fk1" FOREIGN KEY ("clan") REFERENCES "Clans"("id");

ALTER TABLE "Clans_Participants" ADD CONSTRAINT "Clans_Participants_fk2" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "PvP_Competitions" ADD CONSTRAINT "PvP_Competitions_fk1" FOREIGN KEY ("player_1") REFERENCES "Users"("id");

ALTER TABLE "PvP_Competitions" ADD CONSTRAINT "PvP_Competitions_fk2" FOREIGN KEY ("player_2") REFERENCES "Users"("id");

ALTER TABLE "PvP_Competitions" ADD CONSTRAINT "PvP_Competitions_fk4" FOREIGN KEY ("winner") REFERENCES "Users"("id");

ALTER TABLE "Users_Inventory" ADD CONSTRAINT "Users_Inventory_fk1" FOREIGN KEY ("user_id") REFERENCES "Users"("id");

ALTER TABLE "Users_Inventory" ADD CONSTRAINT "Users_Inventory_fk2" FOREIGN KEY ("item") REFERENCES "Items"("id");
ALTER TABLE "Shop" ADD CONSTRAINT "Shop_fk1" FOREIGN KEY ("item") REFERENCES "Items"("id");
ALTER TABLE "Projects_participants" ADD CONSTRAINT "Projects_participants_fk1" FOREIGN KEY ("project") REFERENCES "Projects"("id");

ALTER TABLE "Projects_participants" ADD CONSTRAINT "Projects_participants_fk2" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "Tasks_pointers" ADD CONSTRAINT "Tasks_pointers_fk1" FOREIGN KEY ("task") REFERENCES "Version_history"("id");

ALTER TABLE "Tasks_pointers" ADD CONSTRAINT "Tasks_pointers_fk2" FOREIGN KEY ("project") REFERENCES "Projects"("id");
ALTER TABLE "Commentaries" ADD CONSTRAINT "Commentaries_fk1" FOREIGN KEY ("task") REFERENCES "Tasks_pointers"("id");

ALTER TABLE "Commentaries" ADD CONSTRAINT "Commentaries_fk2" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "Spheres" ADD CONSTRAINT "Spheres_fk2" FOREIGN KEY ("project") REFERENCES "Projects"("id");
ALTER TABLE "Tasks_Spheres" ADD CONSTRAINT "Tasks_Spheres_fk0" FOREIGN KEY ("task") REFERENCES "Version_history"("id");

ALTER TABLE "Tasks_Spheres" ADD CONSTRAINT "Tasks_Spheres_fk1" FOREIGN KEY ("sphere") REFERENCES "Spheres"("id");

ALTER TABLE "Users_Achievements" ADD CONSTRAINT "Users_Achievements_fk1" FOREIGN KEY ("user_id") REFERENCES "Users"("id");

ALTER TABLE "Users_Achievements" ADD CONSTRAINT "Users_Achievements_fk2" FOREIGN KEY ("achievement") REFERENCES "Achievements"("id");
ALTER TABLE "Income" ADD CONSTRAINT "Income_fk1" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "Spendings" ADD CONSTRAINT "Spendings_fk1" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "Teams" ADD CONSTRAINT "Teams_fk1" FOREIGN KEY ("project_id") REFERENCES "Projects"("id");
ALTER TABLE "Teams_Participants" ADD CONSTRAINT "Teams_Participants_fk1" FOREIGN KEY ("team") REFERENCES "Teams"("id");

ALTER TABLE "Teams_Participants" ADD CONSTRAINT "Teams_Participants_fk2" FOREIGN KEY ("user_id") REFERENCES "Users"("id");
ALTER TABLE "Project_items" ADD CONSTRAINT "Project_items_fk1" FOREIGN KEY ("project") REFERENCES "Projects"("id");

ALTER TABLE "Project_items" ADD CONSTRAINT "Project_items_fk2" FOREIGN KEY ("item") REFERENCES "Items"("id");
ALTER TABLE "Xp_gains" ADD CONSTRAINT "Xp_gains_fk3" FOREIGN KEY ("user_id") REFERENCES "Users"("id");