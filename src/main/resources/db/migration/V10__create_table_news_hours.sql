CREATE TABLE "public"."posts_hour"
(
    "id"         serial8,
    "post_id"    int8      NOT NULL,
    "created_at" timestamp NOT NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("post_id") REFERENCES "public"."posts" ("id") ON DELETE CASCADE
)
;

CREATE INDEX ON "public"."posts_hour" USING btree (
    "post_id"
    );

CREATE INDEX ON "public"."posts_hour" USING btree (
    "created_at"
    );