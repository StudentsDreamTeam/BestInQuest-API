CREATE TABLE IF NOT EXISTS "Users" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"email" varchar(255) NOT NULL UNIQUE,
	"password" varchar(255) NOT NULL,
	"xp" bigint NOT NULL,
	"level" bigint NOT NULL,
	"registration_date" timestamp with time zone NOT NULL,
	"last_online_date" timestamp with time zone NOT NULL,
	"streak" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Projects" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"description" varchar(255),
	"owner" bigint NOT NULL,
	"status" varchar(255) NOT NULL,
	"creation_date" timestamp with time zone NOT NULL,
	"project_items" bigint,
	"done" boolean NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Version_history" (
	"id" serial NOT NULL UNIQUE,
	"title" varchar(255) NOT NULL,
	"description" varchar(255),
	"status" varchar(255) NOT NULL,
	"priority" varchar(255) NOT NULL,
	"difficulty" bigint NOT NULL,
	"author" bigint NOT NULL,
	"executor" bigint NOT NULL,
	"update_date" timestamp with time zone NOT NULL,
	"fast_done_bonus" bigint NOT NULL,
	"combo" boolean NOT NULL,
	"reward_xp" bigint NOT NULL,
	"reward_currency" bigint NOT NULL,
	"deadline" date,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Clans" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL UNIQUE,
	"leader" bigint NOT NULL,
	"creation_date" timestamp with time zone NOT NULL,
	"rating" bigint NOT NULL,
	"project" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Clans_Participants" (
	"id" serial NOT NULL UNIQUE,
	"clan" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"role" varchar(255) NOT NULL,
	"joining_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "PvP_Competitions" (
	"id" serial NOT NULL UNIQUE,
	"player_1" bigint NOT NULL,
	"player_2" bigint NOT NULL,
	"status" varchar(255) NOT NULL,
	"winner" bigint NOT NULL,
	"reward_xp" bigint NOT NULL,
	"reward_currency" bigint NOT NULL,
	"start_date" timestamp with time zone NOT NULL,
	"end_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Items" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL UNIQUE,
	"description" varchar(255),
	"rarity" varchar(255) NOT NULL,
	"xp_multiplier" bigint NOT NULL,
	"currency_multiplier" bigint NOT NULL,
	"duration" varchar(255) NOT NULL,
	"cost" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Users_Inventory" (
	"id" serial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"item" bigint NOT NULL,
	"amount" bigint NOT NULL,
	"acquire_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Shop" (
	"id" serial NOT NULL UNIQUE,
	"item" bigint NOT NULL,
	"cost" bigint NOT NULL,
	"availability" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Projects_participants" (
	"id" serial NOT NULL UNIQUE,
	"project" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"role" varchar(255) NOT NULL,
	"update_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Tasks_pointers" (
	"id" serial NOT NULL UNIQUE,
	"task" bigint NOT NULL,
	"project" bigint NOT NULL,
	"creation_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Commentaries" (
	"id" serial NOT NULL UNIQUE,
	"task" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"text" varchar(255) NOT NULL,
	"creation_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Spheres" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL,
	"project" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Tasks_Spheres" (
	"task" bigint NOT NULL,
	"sphere" bigint NOT NULL,
	PRIMARY KEY ("task", "sphere")
);

CREATE TABLE IF NOT EXISTS "Achievements" (
	"id" serial NOT NULL UNIQUE,
	"name" varchar(255) NOT NULL UNIQUE,
	"description" varchar(255) NOT NULL,
	"required_xp" bigint NOT NULL,
	"icon" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Users_Achievements" (
	"id" serial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"achievement" bigint NOT NULL,
	"acquire_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Income" (
	"id" serial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"amount" bigint NOT NULL,
	"date" timestamp with time zone NOT NULL,
	"description" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Spendings" (
	"id" serial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"amount" bigint NOT NULL,
	"date" timestamp with time zone NOT NULL,
	"description" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Teams" (
	"id" serial NOT NULL UNIQUE,
	"project_id" bigint NOT NULL,
	"name" varchar(255) NOT NULL,
	"description" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Teams_Participants" (
	"id" serial NOT NULL UNIQUE,
	"team" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"update_date" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Project_items" (
	"id" serial NOT NULL UNIQUE,
	"project" bigint NOT NULL,
	"item" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Xp_gains" (
	"id" serial NOT NULL UNIQUE,
	"amount" bigint NOT NULL,
	"date" timestamp with time zone NOT NULL,
	"user_id" bigint NOT NULL,
	PRIMARY KEY ("id")
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