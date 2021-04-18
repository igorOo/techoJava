ALTER TABLE "public"."comment"
    ADD COLUMN "reply_to" int8;

CREATE INDEX "comment_entity_index" ON "public"."comment" USING btree (
  "entity"
);