CREATE TABLE IF NOT EXISTS "Пользователи" (
	"id" serial NOT NULL UNIQUE,
	"имя" varchar(255) NOT NULL,
	"email" varchar(255) NOT NULL UNIQUE,
	"пароль" varchar(255) NOT NULL,
	"xp" bigint NOT NULL,
	"уровень" bigint NOT NULL,
	"дата_регистрации" timestamp with time zone NOT NULL,
	"дата_последнего_входа" timestamp with time zone NOT NULL,
	"streak" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Проекты" (
	"id" serial NOT NULL UNIQUE,
	"название" varchar(255) NOT NULL,
	"описание" varchar(255),
	"владелец" bigint NOT NULL,
	"статус" varchar(255) NOT NULL,
	"дата_создания" timestamp with time zone NOT NULL,
	"предметы_проекта" bigint,
	"завершен" boolean NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "История_версий" (
	"id" serial NOT NULL UNIQUE,
	"заголовок" varchar(255) NOT NULL,
	"описание" varchar(255),
	"статус" varchar(255) NOT NULL,
	"приоритет" varchar(255) NOT NULL,
	"сложность" bigint NOT NULL,
	"автор" bigint NOT NULL,
	"исполнитель" bigint NOT NULL,
	"дата_обновления" timestamp with time zone NOT NULL,
	"бонус_за_быстрое_выполнение" bigint NOT NULL,
	"комбо" boolean NOT NULL,
	"награда_xp" bigint NOT NULL,
	"награда_валюта" bigint NOT NULL,
	"дедлайн" date,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Кланы" (
	"id" serial NOT NULL UNIQUE,
	"название" varchar(255) NOT NULL UNIQUE,
	"лидер" bigint NOT NULL,
	"дата_создания" timestamp with time zone NOT NULL,
	"рейтинг" bigint NOT NULL,
	"проект" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Кланы_Участники" (
	"id" serial NOT NULL UNIQUE,
	"клан" bigint NOT NULL,
	"пользователь" bigint NOT NULL,
	"роль" varchar(255) NOT NULL,
	"дата_вступления" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "PvP_Соревнования" (
	"id" serial NOT NULL UNIQUE,
	"игрок_1" bigint NOT NULL,
	"игрок_2" bigint NOT NULL,
	"статус" varchar(255) NOT NULL,
	"победитель" bigint NOT NULL,
	"награда_xp" bigint NOT NULL,
	"награда_валюта" bigint NOT NULL,
	"дата_начала" timestamp with time zone NOT NULL,
	"дата_окончания" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Предметы" (
	"id" serial NOT NULL UNIQUE,
	"название" varchar(255) NOT NULL UNIQUE,
	"описание" varchar(255),
	"редкость" varchar(255) NOT NULL,
	"эффект_xp" bigint NOT NULL,
	"эффект_валюта" bigint NOT NULL,
	"длительность" varchar(255) NOT NULL,
	"стоимость" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Пользователи_Инвентарь" (
	"id" serial NOT NULL UNIQUE,
	"пользователь" bigint NOT NULL,
	"предмет" bigint NOT NULL,
	"количество" bigint NOT NULL,
	"дата_получения" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Магазин" (
	"id" serial NOT NULL UNIQUE,
	"предмет" bigint NOT NULL,
	"цена" bigint NOT NULL,
	"доступность" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Участники_проекта" (
	"id" serial NOT NULL UNIQUE,
	"проект" bigint NOT NULL,
	"пользователь" bigint NOT NULL,
	"роль" varchar(255) NOT NULL,
	"дата_добавления" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Указатель_на_задачу" (
	"id" serial NOT NULL UNIQUE,
	"задача" bigint NOT NULL,
	"проект" bigint NOT NULL,
	"дата_создания" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Комментарии" (
	"id" serial NOT NULL UNIQUE,
	"задача" bigint NOT NULL,
	"пользователь" bigint NOT NULL,
	"текст" varchar(255) NOT NULL,
	"дата_создания" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Сферы" (
	"id" serial NOT NULL UNIQUE,
	"название" varchar(255) NOT NULL,
	"проект" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Задачи_Сферы" (
	"задача" bigint NOT NULL,
	"сфера" bigint NOT NULL,
	PRIMARY KEY ("задача", "сфера")
);

CREATE TABLE IF NOT EXISTS "Достижения" (
	"id" serial NOT NULL UNIQUE,
	"название" varchar(255) NOT NULL UNIQUE,
	"описание" varchar(255) NOT NULL,
	"требуемый_xp" bigint NOT NULL,
	"значок" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Пользователи_Достижения" (
	"id" serial NOT NULL UNIQUE,
	"пользователь" bigint NOT NULL,
	"достижение" bigint NOT NULL,
	"дата_получения" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Доходы" (
	"id" serial NOT NULL UNIQUE,
	"пользователь" bigint NOT NULL,
	"количество" bigint NOT NULL,
	"дата" timestamp with time zone NOT NULL,
	"описание" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Расходы" (
	"id" serial NOT NULL UNIQUE,
	"пользователь" bigint NOT NULL,
	"количество" bigint NOT NULL,
	"дата" timestamp with time zone NOT NULL,
	"описание" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Команды" (
	"id" serial NOT NULL UNIQUE,
	"id_проекта" bigint NOT NULL,
	"название" varchar(255) NOT NULL,
	"описание" varchar(255),
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Участники_команды" (
	"id" serial NOT NULL UNIQUE,
	"команда" bigint NOT NULL,
	"пользователь" bigint NOT NULL,
	"дата_добавления" timestamp with time zone NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Предметы_проекта" (
	"id" serial NOT NULL UNIQUE,
	"проект" bigint NOT NULL,
	"предмет" bigint NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "Получение_xp" (
	"id" serial NOT NULL UNIQUE,
	"количество" bigint NOT NULL,
	"дата" timestamp with time zone NOT NULL,
	"пользователь" bigint NOT NULL,
	PRIMARY KEY ("id")
);


ALTER TABLE "Проекты" ADD CONSTRAINT "Проекты_fk3" FOREIGN KEY ("владелец") REFERENCES "Пользователи"("id");
ALTER TABLE "История_версий" ADD CONSTRAINT "История_версий_fk6" FOREIGN KEY ("автор") REFERENCES "Пользователи"("id");

ALTER TABLE "История_версий" ADD CONSTRAINT "История_версий_fk7" FOREIGN KEY ("исполнитель") REFERENCES "Пользователи"("id");
ALTER TABLE "Кланы" ADD CONSTRAINT "Кланы_fk2" FOREIGN KEY ("лидер") REFERENCES "Пользователи"("id");

ALTER TABLE "Кланы" ADD CONSTRAINT "Кланы_fk5" FOREIGN KEY ("проект") REFERENCES "Проекты"("id");
ALTER TABLE "Кланы_Участники" ADD CONSTRAINT "Кланы_Участники_fk1" FOREIGN KEY ("клан") REFERENCES "Кланы"("id");

ALTER TABLE "Кланы_Участники" ADD CONSTRAINT "Кланы_Участники_fk2" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "PvP_Соревнования" ADD CONSTRAINT "PvP_Соревнования_fk1" FOREIGN KEY ("игрок_1") REFERENCES "Пользователи"("id");

ALTER TABLE "PvP_Соревнования" ADD CONSTRAINT "PvP_Соревнования_fk2" FOREIGN KEY ("игрок_2") REFERENCES "Пользователи"("id");

ALTER TABLE "PvP_Соревнования" ADD CONSTRAINT "PvP_Соревнования_fk4" FOREIGN KEY ("победитель") REFERENCES "Пользователи"("id");

ALTER TABLE "Пользователи_Инвентарь" ADD CONSTRAINT "Пользователи_Инвентарь_fk1" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");

ALTER TABLE "Пользователи_Инвентарь" ADD CONSTRAINT "Пользователи_Инвентарь_fk2" FOREIGN KEY ("предмет") REFERENCES "Предметы"("id");
ALTER TABLE "Магазин" ADD CONSTRAINT "Магазин_fk1" FOREIGN KEY ("предмет") REFERENCES "Предметы"("id");
ALTER TABLE "Участники_проекта" ADD CONSTRAINT "Участники_проекта_fk1" FOREIGN KEY ("проект") REFERENCES "Проекты"("id");

ALTER TABLE "Участники_проекта" ADD CONSTRAINT "Участники_проекта_fk2" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "Указатель_на_задачу" ADD CONSTRAINT "Указатель_на_задачу_fk1" FOREIGN KEY ("задача") REFERENCES "История_версий"("id");

ALTER TABLE "Указатель_на_задачу" ADD CONSTRAINT "Указатель_на_задачу_fk2" FOREIGN KEY ("проект") REFERENCES "Проекты"("id");
ALTER TABLE "Комментарии" ADD CONSTRAINT "Комментарии_fk1" FOREIGN KEY ("задача") REFERENCES "Указатель_на_задачу"("id");

ALTER TABLE "Комментарии" ADD CONSTRAINT "Комментарии_fk2" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "Сферы" ADD CONSTRAINT "Сферы_fk2" FOREIGN KEY ("проект") REFERENCES "Проекты"("id");
ALTER TABLE "Задачи_Сферы" ADD CONSTRAINT "Задачи_Сферы_fk0" FOREIGN KEY ("задача") REFERENCES "История_версий"("id");

ALTER TABLE "Задачи_Сферы" ADD CONSTRAINT "Задачи_Сферы_fk1" FOREIGN KEY ("сфера") REFERENCES "Сферы"("id");

ALTER TABLE "Пользователи_Достижения" ADD CONSTRAINT "Пользователи_Достижения_fk1" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");

ALTER TABLE "Пользователи_Достижения" ADD CONSTRAINT "Пользователи_Достижения_fk2" FOREIGN KEY ("достижение") REFERENCES "Достижения"("id");
ALTER TABLE "Доходы" ADD CONSTRAINT "Доходы_fk1" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "Расходы" ADD CONSTRAINT "Расходы_fk1" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "Команды" ADD CONSTRAINT "Команды_fk1" FOREIGN KEY ("id_проекта") REFERENCES "Проекты"("id");
ALTER TABLE "Участники_команды" ADD CONSTRAINT "Участники_команды_fk1" FOREIGN KEY ("команда") REFERENCES "Команды"("id");

ALTER TABLE "Участники_команды" ADD CONSTRAINT "Участники_команды_fk2" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");
ALTER TABLE "Предметы_проекта" ADD CONSTRAINT "Предметы_проекта_fk1" FOREIGN KEY ("проект") REFERENCES "Проекты"("id");

ALTER TABLE "Предметы_проекта" ADD CONSTRAINT "Предметы_проекта_fk2" FOREIGN KEY ("предмет") REFERENCES "Предметы"("id");
ALTER TABLE "Получение_xp" ADD CONSTRAINT "Получение_xp_fk3" FOREIGN KEY ("пользователь") REFERENCES "Пользователи"("id");