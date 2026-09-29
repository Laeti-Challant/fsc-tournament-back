INSERT INTO event (id, name, event_date, registration_deadline, status, max_participants, nb_rounds, dtype) 
	VALUES (1001, 'test integration ranking', '2026-09-28', '2026-09-01', 'FINISHED', 8, 2, 'Tournament');
	
INSERT INTO tournament (id, featured) VALUES (1001, false);

INSERT INTO tournament_rules (id, budget_po, psp_pool, max_skills_per_player, resurrection_mode, mogette_psp_value, mogette_po_value, tournament_id)
	VALUES (1001, 1000000, 30, 2, true, 5, 20000, 1001);
	
INSERT INTO roster_category (id, race_name, is_minus, category_value, rules_id)
	VALUES 	(1001, 'Orcs', false, 1, 1001),
			(1002, 'Morts-vivants', false, 2, 1001),
			(1003, 'Nains', true, 4, 1001),
			(1004, 'Élus du Chaos', false, 1, 1001);
			
INSERT INTO coach (id, coach_pseudo, team_name, race, eating, vegetarian, roster_status, status, substitute, event_id) 
	VALUES 	(1001, 'coachA', 'teamA', 'Élus du Chaos', false, false, 'SUBMITTED', 'VALIDATED', false, 1001),
			(1002, 'coachB', 'teamB', 'Orcs', false, false, 'SUBMITTED', 'VALIDATED', false, 1001),
			(1003, 'coachC', 'teamMinus', 'Nains', false, false, 'SUBMITTED', 'VALIDATED', false, 1001),
			(1004, 'coachD', 'teamD', 'Morts-vivants', false, false, 'SUBMITTED', 'VALIDATED', false, 1001);
			
INSERT INTO round (id, round_number, pairing_type, status, event_id)
	VALUES 	(1001, 1, 'RANDOM', 'FINISHED', 1001),
			(1002, 2, 'SWISS', 'FINISHED', 1001);
			
INSERT INTO "match" (id, status, round_id, coach1_id, coach2_id)
	VALUES 	(1001, 'FINISHED', 1001, 1001, 1004),
			(1002, 'FINISHED', 1001, 1002, 1003),
			(1003, 'FINISHED', 1002, 1002, 1004),
			(1004, 'FINISHED', 1002, 1001, 1003);

INSERT INTO coach_result (id, result, touchdowns, casualties, objectives, passes, foul_actions, bonus_objective, match_id, coach_id)
	VALUES 	(1001, 'LOSS', 3, 5, 4, 2, 4, false, 1001, 1001),
			(1002, 'WIN', 3, 4, 2, 0, 3, true, 1002, 1002),
			(1003, 'LOSS', 1 , 1, 6, 1, 2, true, 1002, 1003),
			(1004, 'WIN', 4, 4, 3, 1, 1, false, 1001, 1004),
			(1005, 'WIN', 3, 5, 2, 2, 3, false, 1003, 1002), 
			(1006, 'LOSS', 2, 5, 3, 1, 1, false, 1003, 1004),
			(1007, 'WIN', 3, 4, 1, 3, 4, true, 1004, 1001),
			(1008, 'LOSS', 0, 1, 6, 0, 2, true, 1004, 1003);