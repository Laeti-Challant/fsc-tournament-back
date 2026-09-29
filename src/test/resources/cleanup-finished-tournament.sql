DELETE FROM coach_result WHERE id > 1000 AND id < 1010;

DELETE FROM "match" WHERE id > 1000 AND id < 1005;

DELETE FROM round WHERE id > 1000 AND id < 1005;

DELETE FROM coach WHERE id > 1000 AND id < 1005;

DELETE FROM roster_category WHERE id > 1000 AND id < 1005;

DELETE FROM tournament_rules WHERE id = 1001;

DELETE FROM tournament WHERE id = 1001;

DELETE FROM "event" WHERE id = 1001;
