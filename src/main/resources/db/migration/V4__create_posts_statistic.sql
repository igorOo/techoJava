CREATE TABLE "public"."statistic_post_read" (
     "id" serial8,
     "post_id" int8 NOT NULL,
     "time_read" int4 NOT NULL DEFAULT 0,
     "user_id" int8,
     "ip_address" varchar(50),
     "date_create" timestamp NOT NULL DEFAULT now(),
     PRIMARY KEY ("id"),
     FOREIGN KEY ("post_id") REFERENCES "public"."posts" ("id") ON DELETE CASCADE
);

CREATE INDEX ON "public"."statistic_post_read" USING btree (
  "time_read"
);