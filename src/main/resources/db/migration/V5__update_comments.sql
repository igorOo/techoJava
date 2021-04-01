ALTER TABLE "public"."comment"
    ADD COLUMN "date_create_d" timestamp NOT NULL DEFAULT NOW(),
    ADD COLUMN "date_edit_d" timestamp NOT NULL DEFAULT NOW();

UPDATE "public"."comment" SET date_create_d = to_timestamp(created_at)::TIMESTAMP;
UPDATE "public"."comment" SET date_edit_d = to_timestamp(updated_at)::TIMESTAMP;


ALTER TABLE "public"."comment"
    DROP COLUMN "created_at",
    DROP COLUMN "updated_at";

ALTER TABLE "public"."comment" RENAME COLUMN "date_create_d" TO "created_at";
ALTER TABLE "public"."comment" RENAME COLUMN "date_edit_d" TO "updated_at";