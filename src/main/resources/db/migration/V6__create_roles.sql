ALTER TABLE "public"."user"
    ADD PRIMARY KEY ("id");

CREATE TABLE "public"."roles" (
     "id" serial8,
     "name" varchar(255) NOT NULL,
     PRIMARY KEY ("id")
);

CREATE TABLE "public"."user_roles" (
     "id" serial8,
     "role_id" int8 NOT NULL,
     "user_id" int8 NOT NULL,
     PRIMARY KEY ("id"),
     FOREIGN KEY ("role_id") REFERENCES "public"."roles" ("id") ON DELETE CASCADE,
     FOREIGN KEY ("user_id") REFERENCES "public"."user" ("id") ON DELETE CASCADE
);

CREATE INDEX "user_roles_role_id_index" ON "public"."user_roles" (
  "role_id"
);

CREATE INDEX "user_roles_user_id_index" ON "public"."user_roles" (
  "user_id"
);