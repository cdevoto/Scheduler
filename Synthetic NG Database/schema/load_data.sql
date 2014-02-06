INSERT INTO scheduler (scheduler_id, worker_num)
   VALUES (1, 0);
INSERT INTO scheduler (scheduler_id, worker_num)
   VALUES (2, 1);

INSERT INTO location (location_id, name)
   VALUES (1, 'New York City');
INSERT INTO location (location_id, name)
   VALUES (2, 'Los Angeles');
INSERT INTO location (location_id, name)
   VALUES (3, 'Chicago');
INSERT INTO location (location_id, name)
   VALUES (4, 'Boston');
INSERT INTO location (location_id, name)
   VALUES (5, 'Detroit');

INSERT INTO carrier (carrier_id, name)
   VALUES (1, 'Sprint');
INSERT INTO carrier (carrier_id, name)
   VALUES (2, 'Verizon');
INSERT INTO carrier (carrier_id, name)
   VALUES (3, 'AT&T');
INSERT INTO carrier (carrier_id, name)
   VALUES (4, 'Level 3');

INSERT INTO player (player_id, name)
   VALUES (1, 'Chrome');
INSERT INTO player (player_id, name)
   VALUES (2, 'Firefox');
INSERT INTO player (player_id, name)
   VALUES (3, 'Internet Explorer');
INSERT INTO player (player_id, name)
   VALUES (4, 'Android Native');
INSERT INTO player (player_id, name)
   VALUES (5, 'iOS Native');
   
INSERT INTO script_type (script_type_id, name)
   VALUES (1, 'All Browsers');
INSERT INTO script_type (script_type_id, name)
   VALUES (2, 'Android Native');
INSERT INTO script_type (script_type_id, name)
   VALUES (3, 'iOS Native');
INSERT INTO script_type (script_type_id, name)
   VALUES (4, 'Chrome');
INSERT INTO script_type (script_type_id, name)
   VALUES (5, 'Firefox');
INSERT INTO script_type (script_type_id, name)
   VALUES (6, 'Internet Explorer');
   
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (1, 1, 1);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (2, 1, 2);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (3, 1, 3);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (4, 2, 4);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (5, 3, 5);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (6, 4, 1);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (7, 5, 2);
INSERT INTO script_type_player (script_type_player_id, script_type_id, player_id)
   VALUES (8, 6, 3);

INSERT INTO ability_flag (ability_flag_id, ability_flag_level_id, mask, description)
   VALUES (1, 2, 1, 'IPv4');
INSERT INTO ability_flag (ability_flag_id, ability_flag_level_id, mask, description)
   VALUES (2, 2, 2, 'IPv6');
INSERT INTO ability_flag (ability_flag_id, ability_flag_level_id, mask, description)
   VALUES (3, 1, 4, 'SMS');

INSERT INTO script_type_ability_flag (script_type_ability_flag_id, ability_flag_id, script_type_id)
   VALUES (1, 3, 2);
INSERT INTO script_type_ability_flag (script_type_ability_flag_id, ability_flag_id, script_type_id)
   VALUES (2, 3, 3);

INSERT INTO timezone (timezone_id, name)
    VALUES (1, 'UTC');
INSERT INTO timezone (timezone_id, name)
    VALUES (2, 'US/Alaska');
INSERT INTO timezone (timezone_id, name)
    VALUES (3, 'US/Aleutian');
INSERT INTO timezone (timezone_id, name)
    VALUES (4, 'US/Arizona');
INSERT INTO timezone (timezone_id, name)
    VALUES (5, 'US/Central');
INSERT INTO timezone (timezone_id, name)
    VALUES (6, 'US/East-Indiana');
INSERT INTO timezone (timezone_id, name)
    VALUES (7, 'US/Eastern');
INSERT INTO timezone (timezone_id, name)
    VALUES (8, 'US/Hawaii');
INSERT INTO timezone (timezone_id, name)
    VALUES (9, 'US/Indiana-Starke');
INSERT INTO timezone (timezone_id, name)
    VALUES (10, 'US/Michigan');
INSERT INTO timezone (timezone_id, name)
    VALUES (11, 'US/Mountain');
INSERT INTO timezone (timezone_id, name)
    VALUES (12, 'US/Pacific');
INSERT INTO timezone (timezone_id, name)
    VALUES (13, 'US/Pacific-New');
INSERT INTO timezone (timezone_id, name)
    VALUES (14, 'US/Samoa');
   

INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (1, 1, 1, 'Browser Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (2, 4, 1, 'Chrome Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (3, 5, 1, 'Firefox Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (4, 6, 1, 'Internet Explorer Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (5, 2, 1, 'Android Native Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (6, 2, 1, 'Android Native Script (Requires SMS)', 4);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (7, 3, 1, 'iOS Native Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (8, 3, 1, 'iOS Native Script (Requires SMS)', 4);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (9, 1, 2, 'Browser Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (10, 4, 2, 'Chrome Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (11, 5, 2, 'Firefox Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (12, 6, 2, 'Internet Explorer Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (13, 2, 2, 'Android Native Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (14, 2, 2, 'Android Native Script (Requires SMS)', 4);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (15, 3, 2, 'iOS Native Script', 0);
INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)
    VALUES (16, 3, 2, 'iOS Native Script (Requires SMS)', 4);

# New York City:Sprint:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (1, 1, 1, 1);
# New York City:Sprint:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (2, 1, 1, 2);
# New York City:Sprint:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (3, 1, 1, 3);
# New York City:Sprint:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (4, 1, 1, 5);
# New York City:Sprint:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (5, 1, 1, 4);
# New York City:Verizon:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (6, 1, 2, 1);
# New York City:Verizon:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (7, 1, 2, 2);
# New York City:Verizon:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (8, 1, 2, 3);
# New York City:Verizon:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (9, 1, 2, 5);
# New York City:Verizon:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (10, 1, 2, 4);
# New York City:AT&T:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (11, 1, 3, 1);
# New York City:AT&T:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (12, 1, 3, 2);
# New York City:AT&T:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (13, 1, 3, 3);
# New York City:AT&T:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (14, 1, 3, 5);
# New York City:AT&T:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (15, 1, 3, 4);
# New York City:Level 3:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (16, 1, 4, 1);
# New York City:Level 3:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (17, 1, 4, 2);
# New York City:Level 3:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (18, 1, 4, 3);
# New York City:Level 3:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (19, 1, 4, 5);
# New York City:Level 3:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (20, 1, 4, 4);
# Los Angeles:Sprint:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (21, 2, 1, 1);
# Los Angeles:Sprint:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (22, 2, 1, 2);
# Los Angeles:Sprint:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (23, 2, 1, 3);
# Los Angeles:Sprint:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (24, 2, 1, 5);
# Los Angeles:Sprint:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (25, 2, 1, 4);
# Los Angeles:Verizon:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (26, 2, 2, 1);
# Los Angeles:Verizon:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (27, 2, 2, 2);
# Los Angeles:Verizon:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (28, 2, 2, 3);
# Los Angeles:Verizon:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (29, 2, 2, 5);
# Los Angeles:Verizon:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (30, 2, 2, 4);
# Los Angeles:AT&T:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (31, 2, 3, 1);
# Los Angeles:AT&T:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (32, 2, 3, 2);
# Los Angeles:AT&T:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (33, 2, 3, 3);
# Los Angeles:AT&T:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (34, 2, 3, 5);
# Los Angeles:AT&T:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (35, 2, 3, 4);
# Los Angeles:Level 3:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (36, 2, 4, 1);
# Los Angeles:Level 3:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (37, 2, 4, 2);
# Los Angeles:Level 3:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (38, 2, 4, 3);
# Los Angeles:Level 3:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (39, 2, 4, 5);
# Los Angeles:Level 3:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (40, 2, 4, 4);
# Chicago:Sprint:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (41, 3, 1, 1);
# Chicago:Sprint:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (42, 3, 1, 2);
# Chicago:Sprint:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (43, 3, 1, 3);
# Chicago:Sprint:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (44, 3, 1, 5);
# Chicago:Sprint:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (45, 3, 1, 4);
# Chicago:Verizon:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (46, 3, 2, 1);
# Chicago:Verizon:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (47, 3, 2, 2);
# Chicago:Verizon:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (48, 3, 2, 3);
# Chicago:Verizon:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (49, 3, 2, 5);
# Chicago:Verizon:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (50, 3, 2, 4);
# Chicago:AT&T:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (51, 3, 3, 1);
# Chicago:AT&T:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (52, 3, 3, 2);
# Chicago:AT&T:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (53, 3, 3, 3);
# Chicago:AT&T:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (54, 3, 3, 5);
# Chicago:AT&T:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (55, 3, 3, 4);
# Chicago:Level 3:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (56, 3, 4, 1);
# Chicago:Level 3:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (57, 3, 4, 2);
# Chicago:Level 3:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (58, 3, 4, 3);
# Chicago:Level 3:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (59, 3, 4, 5);
# Chicago:Level 3:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (60, 3, 4, 4);
# Boston:Sprint:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (61, 4, 1, 5);
# Boston:Sprint:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (62, 4, 1, 4);
# Boston:Verizon:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (63, 4, 2, 5);
# Boston:Verizon:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (64, 4, 2, 4);
# Boston:AT&T:iOS Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (65, 4, 3, 5);
# Boston:AT&T:Android Native
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (66, 4, 3, 4);
# Detroit:Level 3:Chrome
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (67, 5, 4, 1);
# Detroit:Level 3:Firefox
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (68, 5, 4, 2);
# Detroit:Level 3:Internet Explorer
INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)
    VALUES (69, 5, 4, 3);

# VUC for Chicago:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (1, 3, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (1, 1, 41);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (2, 1, 42);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (3, 1, 43);
# VUC for Chicago:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (2, 3, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (4, 2, 41);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (5, 2, 42);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (6, 2, 43);
# VUC for Chicago:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (3, 3, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (7, 3, 41);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (8, 3, 42);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (9, 3, 43);
# VUC for Chicago:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (4, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (10, 4, 41);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (11, 4, 42);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (12, 4, 43);
# VUC for Chicago:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (5, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (13, 5, 56);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (14, 5, 57);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (15, 5, 58);
# VUC for Chicago:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (6, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (16, 6, 56);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (17, 6, 57);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (18, 6, 58);
# VUC for Los Angeles:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (7, 6, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (19, 7, 29);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (20, 7, 30);
# VUC for Los Angeles:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (8, 5, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (21, 8, 29);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (22, 8, 30);
# VUC for Los Angeles:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (9, 5, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (23, 9, 29);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (24, 9, 30);
# VUC for Chicago:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (10, 6, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (25, 10, 59);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (26, 10, 60);
# VUC for Los Angeles:Sprint:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (11, 2, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (27, 11, 24);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (28, 11, 25);
# VUC for Chicago:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (12, 5, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (29, 12, 49);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (30, 12, 50);
# VUC for Chicago:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (13, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (31, 13, 49);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (32, 13, 50);
# VUC for Chicago:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (14, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (33, 14, 49);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (34, 14, 50);
# VUC for Chicago:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (15, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (35, 15, 54);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (36, 15, 55);
# VUC for Chicago:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (16, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (37, 16, 54);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (38, 16, 55);
# VUC for Chicago:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (17, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (39, 17, 54);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (40, 17, 55);
# VUC for Chicago:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (18, 3, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (41, 18, 54);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (42, 18, 55);
# VUC for Chicago:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (19, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (43, 19, 54);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (44, 19, 55);
# VUC for New York City:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (20, 6, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (45, 20, 19);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (46, 20, 20);
# VUC for New York City:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (21, 3, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (47, 21, 19);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (48, 21, 20);
# VUC for New York City:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (22, 6, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (49, 22, 9);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (50, 22, 10);
# VUC for New York City:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (23, 3, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (51, 23, 9);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (52, 23, 10);
# VUC for New York City:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (24, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (53, 24, 16);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (54, 24, 17);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (55, 24, 18);
# VUC for New York City:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (25, 3, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (56, 25, 16);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (57, 25, 17);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (58, 25, 18);
# VUC for New York City:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (26, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (59, 26, 16);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (60, 26, 17);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (61, 26, 18);
# VUC for New York City:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (27, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (62, 27, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (63, 27, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (64, 27, 3);
# VUC for New York City:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (28, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (65, 28, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (66, 28, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (67, 28, 3);
# VUC for New York City:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (29, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (68, 29, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (69, 29, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (70, 29, 3);
# VUC for New York City:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (30, 3, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (71, 30, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (72, 30, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (73, 30, 3);
# VUC for Detroit:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (31, 3, 5);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (74, 31, 67);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (75, 31, 68);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (76, 31, 69);
# VUC for New York City:Sprint:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (32, 7, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (77, 32, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (78, 32, 5);
# VUC for Los Angeles:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (33, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (79, 33, 34);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (80, 33, 35);
# VUC for Boston:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (34, 5, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (81, 34, 65);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (82, 34, 66);
# VUC for Los Angeles:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (35, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (83, 35, 21);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (84, 35, 22);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (85, 35, 23);
# VUC for Los Angeles:Sprint:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (36, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (86, 36, 21);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (87, 36, 22);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (88, 36, 23);
# VUC for Los Angeles:Level 3:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (37, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (89, 37, 36);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (90, 37, 37);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (91, 37, 38);
# VUC for Chicago:Sprint:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (38, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (92, 38, 44);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (93, 38, 45);
# VUC for Chicago:Sprint:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (39, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (94, 39, 44);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (95, 39, 45);
# VUC for New York City:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (40, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (96, 40, 11);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (97, 40, 12);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (98, 40, 13);
# VUC for New York City:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (41, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (99, 41, 11);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (100, 41, 12);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (101, 41, 13);
# VUC for New York City:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (42, 3, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (102, 42, 11);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (103, 42, 12);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (104, 42, 13);
# VUC for Los Angeles:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (43, 2, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (105, 43, 26);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (106, 43, 27);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (107, 43, 28);
# VUC for Los Angeles:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (44, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (108, 44, 26);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (109, 44, 27);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (110, 44, 28);
# VUC for Los Angeles:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (45, 2, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (111, 45, 26);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (112, 45, 27);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (113, 45, 28);
# VUC for Chicago:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (46, 3, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (114, 46, 51);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (115, 46, 52);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (116, 46, 53);
# VUC for Chicago:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (47, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (117, 47, 51);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (118, 47, 52);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (119, 47, 53);
# VUC for Chicago:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (48, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (120, 48, 51);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (121, 48, 52);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (122, 48, 53);
# VUC for New York City:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (49, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (123, 49, 6);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (124, 49, 7);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (125, 49, 8);
# VUC for New York City:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (50, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (126, 50, 6);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (127, 50, 7);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (128, 50, 8);
# VUC for New York City:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (51, 2, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (129, 51, 6);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (130, 51, 7);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (131, 51, 8);
# VUC for New York City:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (52, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (132, 52, 14);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (133, 52, 15);
# VUC for New York City:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (53, 1, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (134, 53, 14);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (135, 53, 15);
# VUC for New York City:AT&T:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (54, 6, 1);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (136, 54, 14);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (137, 54, 15);
# VUC for Los Angeles:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (55, 5, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (138, 55, 39);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (139, 55, 40);
# VUC for Los Angeles:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (56, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (140, 56, 39);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (141, 56, 40);
# VUC for Los Angeles:Level 3:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (57, 7, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (142, 57, 39);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (143, 57, 40);
# VUC for Los Angeles:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (58, 3, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (144, 58, 31);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (145, 58, 32);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (146, 58, 33);
# VUC for Los Angeles:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (59, 1, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (147, 59, 31);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (148, 59, 32);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (149, 59, 33);
# VUC for Los Angeles:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (60, 2, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (150, 60, 31);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (151, 60, 32);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (152, 60, 33);
# VUC for Los Angeles:AT&T:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (61, 1, 2);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (153, 61, 31);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (154, 61, 32);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (155, 61, 33);
# VUC for Boston:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (62, 1, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (156, 62, 63);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (157, 62, 64);
# VUC for Boston:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (63, 5, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (158, 63, 63);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (159, 63, 64);
# VUC for Boston:Verizon:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (64, 5, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (160, 64, 63);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (161, 64, 64);
# VUC for Boston:Sprint:Native App
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (65, 2, 4);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (162, 65, 61);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (163, 65, 62);
# VUC for Chicago:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (66, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (164, 66, 46);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (165, 66, 47);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (166, 66, 48);
# VUC for Chicago:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (67, 1, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (167, 67, 46);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (168, 67, 47);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (169, 67, 48);
# VUC for Chicago:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (68, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (170, 68, 46);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (171, 68, 47);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (172, 68, 48);
# VUC for Chicago:Verizon:Browser
INSERT INTO vu_controller (vuc_id, supports_f, location_id)
    VALUES (69, 2, 3);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (173, 69, 46);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (174, 69, 47);
INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)
    VALUES (175, 69, 48);