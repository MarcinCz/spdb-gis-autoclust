--tabela POIAfricaPoints powstała poprzez zaimportowanie pliku Shapefile

--utworzenie tabeli testowej ze wszystkimi punktami
CREATE TABLE "POIAfricaPointsTest" AS
  SELECT gid, the_geom FROM "POIAfricaPoints";

CREATE INDEX africaPOITest_gix ON "POIAfricaPointsTest" USING GIST (the_geom);

VACUUM ANALYZE "POIAfricaPointsTest";

ALTER TABLE "POIAfricaPointsTest" ADD COLUMN cluster INTEGER;

--usuniecie punktów o takiej samej geometrii
DELETE FROM "POIAfricaPointsTest"
WHERE gid IN(
SELECT a.gid
FROM "POIAfricaPointsTest" AS a, "POIAfricaPointsTest" AS b
WHERE ST_Equals(a.the_geom, b.the_geom)
AND a.gid != b.gid)

--utworzenie tabeli testowej ze 64k punktow
--DROP TABLE "POIAfricaPointsTest64"

CREATE TABLE "POIAfricaPointsTest64" AS
  SELECT gid, the_geom FROM "POIAfricaPointsTest" LIMIT 64000;

ALTER TABLE "POIAfricaPointsTest64" ADD COLUMN cluster INTEGER;

CREATE INDEX africaPOITest64_gix ON "POIAfricaPointsTest64" USING GIST (the_geom);

--utworzenie tabeli testowej z 32k punktow
--DROP TABLE "POIAfricaPointsTest32"

CREATE TABLE "POIAfricaPointsTest32" AS
  SELECT gid, the_geom FROM "POIAfricaPointsTest" LIMIT 32000;

ALTER TABLE "POIAfricaPointsTest32" ADD COLUMN cluster INTEGER;

CREATE INDEX africaPOITest32_gix ON "POIAfricaPointsTest32" USING GIST (the_geom);

--utworzenie tabeli testowej z 16k punktow
--DROP TABLE "POIAfricaPointsTest16"

CREATE TABLE "POIAfricaPointsTest16" AS
  SELECT gid, the_geom FROM "POIAfricaPointsTest" LIMIT 16000;

ALTER TABLE "POIAfricaPointsTest16" ADD COLUMN cluster INTEGER;

CREATE INDEX africaPOITest16_gix ON "POIAfricaPointsTest16" USING GIST (the_geom);

--utworzenie tabeli testowej z 8k punktow
--DROP TABLE "POIAfricaPointsTest8"

CREATE TABLE "POIAfricaPointsTest8" AS
  SELECT gid, the_geom FROM "POIAfricaPointsTest" LIMIT 8000;

ALTER TABLE "POIAfricaPointsTest8" ADD COLUMN cluster INTEGER;

CREATE INDEX africaPOITest8_gix ON "POIAfricaPointsTest8" USING GIST (the_geom);

--skrypty czyszczące - do wykonania pojedynczo
VACUUM ANALYZE "POIAfricaPointsTest";
VACUUM ANALYZE "POIAfricaPointsTest64";
VACUUM ANALYZE "POIAfricaPointsTest32";
VACUUM ANALYZE "POIAfricaPointsTest16";
VACUUM ANALYZE "POIAfricaPointsTest8";
