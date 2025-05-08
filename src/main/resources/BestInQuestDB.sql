CREATE TABLE IF NOT EXISTS "Пользователи" (
    id            serial    PRIMARY KEY,
    имя           varchar(255) NOT NULL CHECK (length(имя) > 0),
    email         varchar(255) NOT NULL UNIQUE CHECK (email ~* '^[^@]+@[^@]+\.[^@]+$'),
    пароль        varchar(255) NOT NULL CHECK (length(пароль) >= 8),
    xp            bigint    NOT NULL CHECK (xp >= 0),
    уровень       bigint    NOT NULL CHECK (уровень >= 0),
    дата_регистрации     timestamptz NOT NULL,
    дата_последнего_входа timestamptz NOT NULL
        CHECK (дата_последнего_входа >= дата_регистрации),
    streak        bigint    NOT NULL CHECK (streak >= 0)
);

CREATE TABLE IF NOT EXISTS "Проекты" (
    id                serial    PRIMARY KEY,
    название          varchar(255) NOT NULL CHECK (length(название) > 0),
    описание          varchar(255),
    владелец          bigint    NOT NULL CHECK (владелец > 0),
    статус            varchar(255) NOT NULL CHECK (length(статус) > 0),
    дата_создания     timestamptz NOT NULL,
    предметы_проекта  bigint    CHECK (предметы_проекта >= 0),
    завершен          boolean   NOT NULL
);

CREATE TABLE IF NOT EXISTS "История_версий" (
    id                          serial    PRIMARY KEY,
    заголовок                   varchar(255) NOT NULL CHECK (length(заголовок) > 0),
    описание                    varchar(255),
    статус                      varchar(255) NOT NULL CHECK (length(статус) > 0),
    приоритет                   varchar(255) NOT NULL CHECK (length(приоритет) > 0),
    сложность                   bigint    NOT NULL CHECK (сложность >= 0),
    автор                       bigint    NOT NULL CHECK (автор > 0),
    исполнитель                 bigint    NOT NULL CHECK (исполнитель > 0),
    дата_обновления             timestamptz NOT NULL,
    бонус_за_быстрое_выполнение bigint    NOT NULL CHECK (бонус_за_быстрое_выполнение >= 0),
    комбо                       boolean   NOT NULL,
    награда_xp                  bigint    NOT NULL CHECK (награда_xp >= 0),
    награда_валюта              bigint    NOT NULL CHECK (награда_валюта >= 0),
    дедлайн                     date      CHECK (дедлайн >= CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS "Кланы" (
    id            serial    PRIMARY KEY,
    название      varchar(255) NOT NULL UNIQUE CHECK (length(название) > 0),
    лидер         bigint    NOT NULL CHECK (лидер > 0),
    дата_создания timestamptz NOT NULL,
    рейтинг       bigint    NOT NULL CHECK (рейтинг >= 0),
    проект        bigint    NOT NULL CHECK (проект > 0)
);

CREATE TABLE IF NOT EXISTS "Кланы_Участники" (
    id              serial    PRIMARY KEY,
    клан            bigint    NOT NULL CHECK (клан > 0),
    пользователь    bigint    NOT NULL CHECK (пользователь > 0),
    роль            varchar(255) NOT NULL CHECK (length(роль) > 0),
    дата_вступления timestamptz NOT NULL,
    UNIQUE (клан, пользователь)
);

CREATE TABLE IF NOT EXISTS "PvP_Соревнования" (
    id              serial    PRIMARY KEY,
    игрок_1         bigint    NOT NULL CHECK (игрок_1 > 0),
    игрок_2         bigint    NOT NULL CHECK (игрок_2 > 0 AND игрок_2 <> игрок_1),
    статус          varchar(255) NOT NULL CHECK (length(статус) > 0),
    победитель      bigint    NOT NULL CHECK (победитель IN (игрок_1, игрок_2)),
    награда_xp      bigint    NOT NULL CHECK (награда_xp >= 0),
    награда_валюта  bigint    NOT NULL CHECK (награда_валюта >= 0),
    дата_начала     timestamptz NOT NULL,
    дата_окончания  timestamptz NOT NULL CHECK (дата_окончания >= дата_начала)
);

CREATE TABLE IF NOT EXISTS "Предметы" (
    id            serial    PRIMARY KEY,
    название      varchar(255) NOT NULL UNIQUE CHECK (length(название) > 0),
    описание      varchar(255),
    редкость      varchar(255) NOT NULL CHECK (length(редкость) > 0),
    эффект_xp     bigint    NOT NULL CHECK (эффект_xp >= 0),
    эффект_валюта bigint    NOT NULL CHECK (эффект_валюта >= 0),
    длительность  varchar(255) NOT NULL CHECK (length(длительность) > 0),
    стоимость     bigint    NOT NULL CHECK (стоимость >= 0)
);

CREATE TABLE IF NOT EXISTS "Пользователи_Инвентарь" (
    id             serial    PRIMARY KEY,
    пользователь   bigint    NOT NULL CHECK (пользователь > 0),
    предмет        bigint    NOT NULL CHECK (предмет > 0),
    количество     bigint    NOT NULL CHECK (количество >= 0),
    дата_получения timestamptz NOT NULL,
    UNIQUE (пользователь, предмет)
);

CREATE TABLE IF NOT EXISTS "Магазин" (
    id            serial    PRIMARY KEY,
    предмет       bigint    NOT NULL CHECK (предмет > 0),
    цена          bigint    NOT NULL CHECK (цена >= 0),
    доступность   varchar(255) NOT NULL CHECK (length(доступность) > 0),
    UNIQUE (предмет)
);

CREATE TABLE IF NOT EXISTS "Участники_проекта" (
    id             serial    PRIMARY KEY,
    проект         bigint    NOT NULL CHECK (проект > 0),
    пользователь   bigint    NOT NULL CHECK (пользователь > 0),
    роль           varchar(255) NOT NULL CHECK (length(роль) > 0),
    дата_добавления timestamptz NOT NULL,
    UNIQUE (проект, пользователь)
);

CREATE TABLE IF NOT EXISTS "Указатель_на_задачу" (
    id             serial    PRIMARY KEY,
    задача         bigint    NOT NULL CHECK (задача > 0),
    проект         bigint    NOT NULL CHECK (проект > 0),
    дата_создания  timestamptz NOT NULL,
    UNIQUE (задача, проект)
);

CREATE TABLE IF NOT EXISTS "Комментарии" (
    id             serial    PRIMARY KEY,
    задача         bigint    NOT NULL CHECK (задача > 0),
    пользователь   bigint    NOT NULL CHECK (пользователь > 0),
    текст          varchar(255) NOT NULL CHECK (length(текст) > 0),
    дата_создания  timestamptz NOT NULL
);

CREATE TABLE IF NOT EXISTS "Сферы" (
    id       serial    PRIMARY KEY,
    название varchar(255) NOT NULL CHECK (length(название) > 0),
    проект   bigint    NOT NULL CHECK (проект > 0)
);

CREATE TABLE IF NOT EXISTS "Задачи_Сферы" (
    задача bigint NOT NULL CHECK (задача > 0),
    сфера  bigint NOT NULL CHECK (сфера > 0),
    PRIMARY KEY (задача, сфера)
);

CREATE TABLE IF NOT EXISTS "Достижения" (
    id            serial    PRIMARY KEY,
    название      varchar(255) NOT NULL UNIQUE CHECK (length(название) > 0),
    описание      varchar(255) NOT NULL CHECK (length(описание) > 0),
    требуемый_xp  bigint    NOT NULL CHECK (требуемый_xp >= 0),
    значок        varchar(255) NOT NULL CHECK (length(значок) > 0)
);

CREATE TABLE IF NOT EXISTS "Пользователи_Достижения" (
    id               serial    PRIMARY KEY,
    пользователь     bigint    NOT NULL CHECK (пользователь > 0),
    достижение       bigint    NOT NULL CHECK (достижение > 0),
    дата_получения   timestamptz NOT NULL,
    UNIQUE (пользователь, достижение)
);

CREATE TABLE IF NOT EXISTS "Доходы" (
    id            serial    PRIMARY KEY,
    пользователь  bigint    NOT NULL CHECK (пользователь > 0),
    количество    bigint    NOT NULL CHECK (количество >= 0),
    дата          timestamptz NOT NULL,
    описание      varchar(255)
);

CREATE TABLE IF NOT EXISTS "Расходы" (
    id            serial    PRIMARY KEY,
    пользователь  bigint    NOT NULL CHECK (пользователь > 0),
    количество    bigint    NOT NULL CHECK (количество >= 0),
    дата          timestamptz NOT NULL,
    описание      varchar(255)
);

CREATE TABLE IF NOT EXISTS "Команды" (
    id          serial    PRIMARY KEY,
    id_проекта  bigint    NOT NULL CHECK (id_проекта > 0),
    название    varchar(255) NOT NULL CHECK (length(название) > 0),
    описание    varchar(255)
);

CREATE TABLE IF NOT EXISTS "Участники_команды" (
    id               serial    PRIMARY KEY,
    команда          bigint    NOT NULL CHECK (команда > 0),
    пользователь     bigint    NOT NULL CHECK (пользователь > 0),
    дата_добавления  timestamptz NOT NULL,
    UNIQUE (команда, пользователь)
);

CREATE TABLE IF NOT EXISTS "Предметы_проекта" (
    id        serial    PRIMARY KEY,
    проект    bigint    NOT NULL CHECK (проект > 0),
    предмет   bigint    NOT NULL CHECK (предмет > 0),
    UNIQUE (проект, предмет)
);

CREATE TABLE IF NOT EXISTS "Получение_xp" (
    id            serial    PRIMARY KEY,
    количество    bigint    NOT NULL CHECK (количество >= 0),
    дата          timestamptz NOT NULL,
    пользователь  bigint    NOT NULL CHECK (пользователь > 0)
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