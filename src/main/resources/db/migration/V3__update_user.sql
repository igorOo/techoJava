ALTER TABLE "public"."user"
    ADD COLUMN "date_create_d" timestamp NOT NULL DEFAULT NOW(),
    ADD COLUMN "date_edit_d" timestamp NOT NULL DEFAULT NOW(),
    ADD COLUMN "last_visit_d" timestamp;

UPDATE "public"."user" SET date_create_d = to_timestamp(date_create)::TIMESTAMP;
UPDATE "public"."user" SET date_edit_d = to_timestamp(date_edit)::TIMESTAMP;
UPDATE "public"."user" SET last_visit_d = to_timestamp(last_visit)::TIMESTAMP;


ALTER TABLE "public"."user"
DROP COLUMN "date_create",
    DROP COLUMN "date_edit",
    DROP COLUMN "last_visit";

ALTER TABLE "public"."user" RENAME COLUMN "date_create_d" TO "date_create";

ALTER TABLE "public"."user" RENAME COLUMN "date_edit_d" TO "date_edit";

ALTER TABLE "public"."user" RENAME COLUMN "last_visit_d" TO "last_visit";
