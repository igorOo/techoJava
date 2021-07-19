CREATE TABLE "public"."posts_bookmarks"
(
    "id"      serial8,
    "user_id" int8 NOT NULL,
    "post_id" int8 NOT NULL,
    PRIMARY KEY ("id")
)
;

CREATE
INDEX "posts_bookmarks_user_id_index" ON "public"."posts_bookmarks" USING btree (
  "user_id"
);

CREATE
INDEX "posts_bookmarks_post_id_index" ON "public"."posts_bookmarks" USING btree (
  "post_id"
);