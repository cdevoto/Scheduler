SET GLOBAL time_zone = '+0:00';
# Enable query logging through console
# SET GLOBAL general_log = 1;
# SET GLOBAL log_output = 'table';

# View query log
# SELECT * FROM mysql.general_log

# Disable query logging
# SET GLOBAL general_log = 0;

# Delete data from query log
# TRUNCATE TABLE mysql.general_log


DROP TABLE IF EXISTS test_queue;
DROP TABLE IF EXISTS test_definition_lcp;
DROP TABLE IF EXISTS vu_controller_lcp;
DROP TABLE IF EXISTS script_type_player;
DROP TABLE IF EXISTS test_definition_schedule;
DROP TABLE IF EXISTS script_type_ability_flag;
DROP TABLE IF EXISTS test_plan;
DROP TABLE IF EXISTS test_definition;
DROP TABLE IF EXISTS vu_controller;
DROP TABLE IF EXISTS lcp;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS carrier;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS script;
DROP TABLE IF EXISTS script_type;
DROP TABLE IF EXISTS ability_flag;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS timezone;
DROP TABLE IF EXISTS scheduler;

DROP VIEW IF EXISTS test_plan_vw;
DROP VIEW IF EXISTS script_player_vw;
DROP VIEW IF EXISTS test_definition_schedule_vw;
DROP VIEW IF EXISTS schedule_test_definition_vw;
DROP VIEW IF EXISTS test_definition_lcp_vw;
DROP VIEW IF EXISTS test_queue_vw;
DROP VIEW IF EXISTS vu_controller_lcp_vw;
DROP VIEW IF EXISTS test_definition_vuc_vw;

DROP PROCEDURE IF EXISTS filter_lcps;
DROP PROCEDURE IF EXISTS delete_script;
DROP PROCEDURE IF EXISTS delete_test_definition;
DROP PROCEDURE IF EXISTS delete_schedule;
DROP PROCEDURE IF EXISTS get_test_plans;
DROP PROCEDURE IF EXISTS get_maint_schedules;
DROP PROCEDURE IF EXISTS poll_test_queue;
DROP PROCEDURE IF EXISTS get_scheduler;

CREATE TABLE location (
	location_id  BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
   CONSTRAINT pk_location PRIMARY KEY (location_id),
   UNIQUE INDEX idx_location_1 (name)
) ENGINE=InnoDB;

CREATE TABLE carrier (
	carrier_id  BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
   CONSTRAINT pk_carrier PRIMARY KEY (carrier_id),
   UNIQUE INDEX idx_carrier_1 (name)
) ENGINE=InnoDB;

CREATE TABLE player (
	player_id  BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
   CONSTRAINT pk_player PRIMARY KEY (player_id),
   UNIQUE INDEX idx_player_1 (name)
) ENGINE=InnoDB;

CREATE TABLE script_type (
	script_type_id  BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
   CONSTRAINT pk_script_type PRIMARY KEY (script_type_id),
   UNIQUE INDEX idx_script_type_1 (name)
) ENGINE=InnoDB;

CREATE TABLE script_type_player (
	script_type_player_id  BIGINT UNSIGNED AUTO_INCREMENT,
	script_type_id  BIGINT UNSIGNED NOT NULL,
	player_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_script_type_player PRIMARY KEY (script_type_player_id),
   INDEX idx_script_type_player_1 (script_type_id),
   INDEX idx_script_type_player_2 (player_id),
   FOREIGN KEY (script_type_id) 
        REFERENCES script_type(script_type_id)
        ON DELETE CASCADE,
   FOREIGN KEY (player_id) 
        REFERENCES player(player_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE script (
	script_id  BIGINT UNSIGNED AUTO_INCREMENT,
	script_type_id BIGINT UNSIGNED NOT NULL,
	tenant_id BIGINT UNSIGNED NOT NULL,
	name VARCHAR(100) NOT NULL,
    requires_f BIGINT UNSIGNED NOT NULL,
	active BOOLEAN NOT NULL DEFAULT TRUE,
	deleted BOOLEAN NOT NULL DEFAULT FALSE,
	last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_script PRIMARY KEY (script_id),
   INDEX idx_script_1 (script_type_id),
   INDEX idx_script_2 (tenant_id),
   INDEX idx_script_3 (active),
   INDEX idx_script_4 (last_modified),
   FOREIGN KEY (script_type_id) 
        REFERENCES script_type(script_type_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE ability_flag (
	ability_flag_id  BIGINT UNSIGNED AUTO_INCREMENT,
	ability_flag_level_id BIGINT UNSIGNED NOT NULL,
	mask BIGINT UNSIGNED NOT NULL,
	description VARCHAR(100) NOT NULL,
   CONSTRAINT pk_ability_flag PRIMARY KEY (ability_flag_id),
   UNIQUE INDEX idx_ability_flag_1 (description),
   INDEX idx_ability_flag_2 (ability_flag_level_id)
) ENGINE=InnoDB;

CREATE TABLE script_type_ability_flag (
	script_type_ability_flag_id  BIGINT UNSIGNED AUTO_INCREMENT,
	ability_flag_id BIGINT UNSIGNED NOT NULL,
	script_type_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_script_type_ability_flag PRIMARY KEY (script_type_ability_flag_id),
   UNIQUE INDEX idx_script_type_ability_flag_1 (ability_flag_id, script_type_id),
   INDEX idx_script_type_ability_flag_2 (ability_flag_id),
   INDEX idx_script_type_ability_flag_3 (script_type_id),
   FOREIGN KEY (script_type_id) 
        REFERENCES script_type(script_type_id)
        ON DELETE CASCADE,
   FOREIGN KEY (ability_flag_id) 
        REFERENCES ability_flag(ability_flag_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lcp (
	lcp_id  BIGINT UNSIGNED AUTO_INCREMENT,
	location_id BIGINT UNSIGNED NOT NULL,
	carrier_id BIGINT UNSIGNED NOT NULL,
	player_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_lcp PRIMARY KEY (lcp_id),
   UNIQUE INDEX idx_lcp_1 (location_id, carrier_id, player_id),
   INDEX idx_lcp_2 (location_id),
   INDEX idx_lcp_3 (carrier_id),
   INDEX idx_lcp_4 (player_id),
   FOREIGN KEY (location_id) 
        REFERENCES location(location_id)
        ON DELETE CASCADE,
   FOREIGN KEY (carrier_id) 
        REFERENCES carrier(carrier_id)
        ON DELETE CASCADE,
   FOREIGN KEY (player_id) 
        REFERENCES player(player_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE vu_controller (
	vuc_id  BIGINT UNSIGNED AUTO_INCREMENT,
	supports_f BIGINT UNSIGNED NOT NULL,
	location_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_vu_controller PRIMARY KEY (vuc_id),
   INDEX idx_vu_controller_1 (supports_f),
   INDEX idx_vu_controller_2 (location_id),
   FOREIGN KEY (location_id) 
        REFERENCES location(location_id)
        ON DELETE RESTRICT
 ) ENGINE=InnoDB;

CREATE TABLE vu_controller_lcp (
	vuc_lcp_id  BIGINT UNSIGNED AUTO_INCREMENT,
	vuc_id BIGINT UNSIGNED NOT NULL,
	lcp_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_vu_controller_lcp PRIMARY KEY (vuc_lcp_id),
   INDEX idx_vu_controller_lcp_1 (vuc_id),
   INDEX idx_vu_controller_lcp_2 (lcp_id),
   FOREIGN KEY (lcp_id) 
        REFERENCES lcp(lcp_id)
        ON DELETE CASCADE,
   FOREIGN KEY (vuc_id) 
        REFERENCES vu_controller(vuc_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE test_definition (
	test_definition_id  BIGINT UNSIGNED AUTO_INCREMENT,
	script_id  BIGINT UNSIGNED NOT NULL,
	tenant_id  BIGINT UNSIGNED NOT NULL,
	name VARCHAR(100) NOT NULL,
	requires_f BIGINT UNSIGNED NOT NULL, # should this be at the test plan level?
	suspended BOOLEAN NOT NULL DEFAULT FALSE,
	maint_suspended BOOLEAN NOT NULL DEFAULT FALSE,
	active BOOLEAN NOT NULL DEFAULT TRUE,
	deleted BOOLEAN NOT NULL DEFAULT FALSE,
	last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_test_definition PRIMARY KEY (test_definition_id),
   INDEX idx_test_definition_1 (script_id),
   INDEX idx_test_definition_2 (tenant_id),
   INDEX idx_test_definition_3 (requires_f),
   INDEX idx_test_definition_4 (suspended),
   INDEX idx_test_definition_5 (maint_suspended),
   INDEX idx_test_definition_6 (active),
   INDEX idx_test_definition_7 (deleted),
   INDEX idx_test_definition_8 (last_modified),
   FOREIGN KEY (script_id) 
        REFERENCES script(script_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE test_definition_lcp (
	test_definition_lcp_id  BIGINT UNSIGNED AUTO_INCREMENT,
	test_definition_id BIGINT UNSIGNED NOT NULL,
	lcp_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_test_definition_lcp PRIMARY KEY (test_definition_lcp_id),
   INDEX idx_test_definition_lcp_1 (test_definition_id),
   INDEX idx_test_definition_lcp_2 (lcp_id),
   FOREIGN KEY (test_definition_id) 
        REFERENCES test_definition(test_definition_id)
        ON DELETE CASCADE,
   FOREIGN KEY (lcp_id) 
        REFERENCES lcp(lcp_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE test_plan (
	test_plan_id  BIGINT UNSIGNED AUTO_INCREMENT,
	test_definition_id  BIGINT UNSIGNED NOT NULL,
	lcp_id  BIGINT UNSIGNED NOT NULL,
	requires_f BIGINT UNSIGNED NOT NULL,
	rrule VARCHAR(150) NOT NULL,
   CONSTRAINT pk_test_plan PRIMARY KEY (test_plan_id),
   UNIQUE INDEX idx_test_plan_1 (test_definition_id, lcp_id),
   INDEX idx_test_plan_2 (test_definition_id),
   INDEX idx_test_plan_3 (lcp_id),
   FOREIGN KEY (test_definition_id) 
        REFERENCES test_definition(test_definition_id)
        ON DELETE CASCADE,
   FOREIGN KEY (lcp_id) 
        REFERENCES lcp(lcp_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE timezone (
	timezone_id  BIGINT UNSIGNED AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
   CONSTRAINT pk_timezone PRIMARY KEY (timezone_id),
   UNIQUE INDEX idx_timezone_1 (name)
) ENGINE=InnoDB;

CREATE TABLE schedule (
	schedule_id  BIGINT UNSIGNED AUTO_INCREMENT,
	tenant_id BIGINT UNSIGNED NOT NULL,
	timezone_id BIGINT UNSIGNED NOT NULL DEFAULT 1,
	name VARCHAR(100) NOT NULL,
	start_date TIMESTAMP NULL,
	end_date TIMESTAMP NULL,
	rrule VARCHAR(150) NOT NULL,
	duration MEDIUMINT UNSIGNED,
	is_maintenance BOOLEAN NOT NULL DEFAULT 0,
	deleted BOOLEAN NOT NULL DEFAULT FALSE,
	active BOOLEAN NOT NULL DEFAULT TRUE,
	last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT pk_schedule PRIMARY KEY (schedule_id),
   INDEX idx_schedule_1 (tenant_id),
   INDEX idx_schedule_2 (name),
   INDEX idx_schedule_3 (is_maintenance),
   INDEX idx_schedule_4 (deleted),
   INDEX idx_schedule_5 (last_modified),
   FOREIGN KEY (timezone_id) 
        REFERENCES timezone(timezone_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE test_definition_schedule (
	test_definition_schedule_id  BIGINT UNSIGNED AUTO_INCREMENT,
	test_definition_id BIGINT UNSIGNED NOT NULL,
	schedule_id BIGINT UNSIGNED NOT NULL,
   CONSTRAINT pk_test_definition_schedule PRIMARY KEY (test_definition_schedule_id),
   UNIQUE INDEX idx_test_group_schedule_1 (test_definition_id, schedule_id),
   INDEX idx_test_group_schedule_2 (test_definition_id),
   INDEX idx_test_group_schedule_3 (schedule_id),
   FOREIGN KEY (test_definition_id) 
        REFERENCES test_definition(test_definition_id)
        ON DELETE CASCADE,
   FOREIGN KEY (schedule_id) 
        REFERENCES schedule(schedule_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE test_queue (
	test_queue_id  BIGINT UNSIGNED AUTO_INCREMENT,
	test_queue_ref_id BIGINT UNSIGNED, # reference to a different test; used for test retry logic
	test_definition_id BIGINT UNSIGNED NOT NULL,
	script_id BIGINT UNSIGNED NOT NULL,
	tenant_id BIGINT UNSIGNED NOT NULL,
	lcp_id BIGINT UNSIGNED NOT NULL,
	requires_f BIGINT UNSIGNED NOT NULL,
	priority MEDIUMINT UNSIGNED NOT NULL DEFAULT 5,
	status MEDIUMINT UNSIGNED NOT NULL,
	enqueued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	dispatched_at TIMESTAMP NULL,
	dispatched_vuc_id BIGINT UNSIGNED,
	cancelled_at TIMESTAMP NULL,
	cancelled_status MEDIUMINT UNSIGNED,
	completed_at TIMESTAMP NULL,
	completed_status MEDIUMINT UNSIGNED,
   CONSTRAINT pk_test_queue PRIMARY KEY (test_queue_id),
   INDEX idx_test_queue_1 (test_definition_id),
   INDEX idx_test_queue_2 (script_id),
   INDEX idx_test_queue_3 (tenant_id),
   INDEX idx_test_queue_4 (lcp_id),
   INDEX idx_test_queue_5 (requires_f),
   INDEX idx_test_queue_6 (priority),
   INDEX idx_test_queue_7 (enqueued_at),
   INDEX idx_test_queue_8 (dispatched_at),
   INDEX idx_test_queue_9 (completed_at),
   INDEX idx_test_queue_10 (status),
   INDEX idx_test_queue_11 (completed_status),
   INDEX idx_test_queue_12 (test_queue_ref_id),
   FOREIGN KEY (test_definition_id) 
        REFERENCES test_definition(test_definition_id)
        ON DELETE CASCADE,
   FOREIGN KEY (script_id) 
        REFERENCES script(script_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE scheduler (
	scheduler_id  BIGINT UNSIGNED AUTO_INCREMENT,
	worker_num MEDIUMINT UNSIGNED NOT NULL,
   CONSTRAINT pk_scheduler PRIMARY KEY (scheduler_id),
   UNIQUE INDEX worker_num (worker_num)
) ENGINE=InnoDB;


CREATE VIEW vu_controller_lcp_vw AS
    SELECT
        vu_controller.vuc_id,
        lcp.lcp_id,
        CONCAT_WS(':', location.name, carrier.name, player.name) AS lcp_name 
    FROM lcp, location, carrier, player, vu_controller, vu_controller_lcp
    WHERE lcp.location_id = location.location_id AND
       lcp.carrier_id = carrier.carrier_id AND
       lcp.player_id = player.player_id AND
       lcp.lcp_id = vu_controller_lcp.lcp_id AND
       vu_controller_lcp.vuc_id = vu_controller.vuc_id;

CREATE VIEW test_queue_vw AS
    SELECT 
        test_queue_id, 
        test_queue.test_definition_id,
        test_queue.script_id,
        test_queue.tenant_id,
        test_queue.lcp_id,
        test_queue.requires_f,
        test_queue.priority,
        test_queue.status,
        test_queue.enqueued_at,
        test_queue.dispatched_at,
        test_queue.dispatched_vuc_id,
        test_queue.cancelled_at,
        test_queue.cancelled_status,
        test_queue.completed_at,
        test_queue.completed_status,
        test_definition.suspended,
        test_definition.maint_suspended,
        test_definition.active,
        test_definition.deleted AS test_def_deleted,
        script.deleted AS script_deleted,
        test_definition.last_modified AS test_def_last_modified,
        script.last_modified AS script_last_modified
    FROM test_queue, test_definition, script
    WHERE test_queue.test_definition_id = test_definition.test_definition_id AND
          test_queue.script_id = script.script_id; 


CREATE VIEW test_plan_vw AS
    SELECT 
        test_plan_id, 
        test_plan.test_definition_id, 
        test_plan.lcp_id,
        test_definition.script_id,
        test_definition.tenant_id,
        test_plan.requires_f,
        test_plan.rrule,
        test_definition.active,
        test_definition.deleted,
        test_definition.last_modified
    FROM test_plan, test_definition
    WHERE test_plan.test_definition_id = test_definition.test_definition_id; 

CREATE VIEW script_player_vw AS
    SELECT
        script.script_id,
        player.player_id, 
        player.name 
    FROM script, script_type, script_type_player, player
    WHERE script.script_type_id = script_type.script_type_id AND
       script_type.script_type_id = script_type_player.script_type_id AND
       script_type_player.player_id = player.player_id;
 
CREATE VIEW test_definition_lcp_vw AS
    SELECT
        test_definition.test_definition_id,
        lcp.lcp_id,
        CONCAT_WS(':', location.name, carrier.name, player.name) AS lcp_name 
    FROM lcp, location, carrier, player, test_definition, test_definition_lcp
    WHERE lcp.location_id = location.location_id AND
       lcp.carrier_id = carrier.carrier_id AND
       lcp.player_id = player.player_id AND
       lcp.lcp_id = test_definition_lcp.lcp_id AND
       test_definition_lcp.test_definition_id = test_definition.test_definition_id;
 

CREATE VIEW test_definition_schedule_vw AS
    SELECT
        test_definition.test_definition_id,
        schedule.schedule_id,
        schedule.tenant_id,
        schedule.timezone_id,
        schedule.name, 
        schedule.rrule, 
        schedule.duration,
        schedule.is_maintenance,
        schedule.last_modified,
        schedule.deleted OR test_definition.deleted AS deleted,
        schedule.active
    FROM test_definition, test_definition_schedule, schedule, timezone
    WHERE test_definition.test_definition_id = test_definition_schedule.test_definition_id AND
       test_definition_schedule.schedule_id = schedule.schedule_id AND
       schedule.timezone_id = timezone.timezone_id;

CREATE VIEW schedule_test_definition_vw AS
    SELECT
        schedule.schedule_id,
        test_definition.test_definition_id,
        test_definition.name,
        test_definition.suspended,
        test_definition.last_modified,
        schedule.deleted OR test_definition.deleted AS deleted,
        test_definition.active
    FROM test_definition, test_definition_schedule, schedule
    WHERE test_definition.test_definition_id = test_definition_schedule.test_definition_id AND
       test_definition_schedule.schedule_id = schedule.schedule_id;

CREATE VIEW test_definition_vuc_vw AS
    SELECT 
        test_definition.test_definition_id, 
        vu_controller.vuc_id 
    FROM test_definition, test_definition_lcp, lcp, vu_controller_lcp, vu_controller, script 
    WHERE test_definition.test_definition_id = test_definition_lcp.test_definition_id AND 
        test_definition_lcp.lcp_id = lcp.lcp_id AND 
        lcp.lcp_id = vu_controller_lcp.lcp_id AND
        test_definition.script_id = script.script_id AND 
        vu_controller_lcp.vuc_id = vu_controller.vuc_id AND 
        vu_controller.supports_f & (test_definition.requires_f | script.requires_f) = test_definition.requires_f | script.requires_f; 
  
DELIMITER // 
CREATE PROCEDURE filter_lcps (IN script_id BIGINT UNSIGNED, IN requires_f BIGINT UNSIGNED) 
BEGIN 
   SELECT DISTINCT
       lcp.lcp_id,
       CONCAT_WS(':', location.name, carrier.name, player.name) AS lcp_name
   FROM 
       lcp, player, location, carrier, script_type_player, script_type, script, vu_controller_lcp, vu_controller
   WHERE
       script.script_id = script_id AND
       script.script_type_id = script_type.script_type_id AND
       script_type.script_type_id = script_type_player.script_type_id AND
       script_type_player.player_id = player.player_id AND
       player.player_id = lcp.player_id AND
       vu_controller_lcp.lcp_id = lcp.lcp_id AND
       vu_controller_lcp.vuc_id = vu_controller.vuc_id AND
       vu_controller.supports_f & (requires_f | script.requires_f) = (requires_f | script.requires_f) AND
       lcp.location_id = location.location_id AND
       lcp.carrier_id = carrier.carrier_id
    ORDER BY lcp_name ASC, lcp.lcp_id ASC; 
END // 

CREATE PROCEDURE delete_script (IN script_id BIGINT UNSIGNED) 
BEGIN 
    DECLARE the_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
	    ROLLBACK;
	    RESIGNAL;
	END;    

    START TRANSACTION;
    
        UPDATE script 
            SET script.deleted = true, script.last_modified = the_time 
            WHERE script.script_id = script_id;   
    
        UPDATE test_definition 
            SET test_definition.deleted = true, test_definition.last_modified = the_time 
            WHERE test_definition.script_id = script_id; 

        UPDATE schedule
            SET schedule.deleted = true, schedule.last_modified = the_time 
            WHERE schedule.schedule_id IN (
                    SELECT DISTINCT test_definition_schedule.schedule_id 
                    FROM test_definition_schedule, test_definition
                    WHERE test_definition.script_id = script_id AND
                        test_definition.test_definition_id = test_definition_schedule.test_definition_id
            ) AND schedule.is_maintenance = false;        
    COMMIT;
END // 

CREATE PROCEDURE delete_test_definition (IN test_definition_id BIGINT UNSIGNED) 
BEGIN 
    DECLARE the_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
	    ROLLBACK;
	    RESIGNAL;
	END;    

    START TRANSACTION;
    
    
        UPDATE test_definition 
            SET test_definition.deleted = true, test_definition.last_modified = the_time 
            WHERE test_definition.test_definition_id = test_definition_id; 

        UPDATE schedule
            SET schedule.deleted = true, schedule.last_modified = the_time 
            WHERE schedule.schedule_id IN (
                    SELECT DISTINCT test_definition_schedule.schedule_id 
                    FROM test_definition_schedule, test_definition
                    WHERE test_definition.test_definition_id = test_definition_id AND
                        test_definition.test_definition_id = test_definition_schedule.test_definition_id
            ) AND schedule.is_maintenance = false;        
    COMMIT;
END // 

CREATE PROCEDURE delete_schedule (IN schedule_id BIGINT UNSIGNED) 
BEGIN 
    DECLARE the_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

    UPDATE schedule
        SET schedule.deleted = true, schedule.last_modified = the_time 
        WHERE schedule.schedule_id = schedule_id;        
END // 

CREATE PROCEDURE get_test_plans (IN total_workers SMALLINT UNSIGNED, IN worker_num SMALLINT UNSIGNED, IN max_rows SMALLINT UNSIGNED, IN max_last_modified TIMESTAMP, IN min_test_def_id BIGINT UNSIGNED, IN min_test_plan_id BIGINT UNSIGNED, IN min_last_modified TIMESTAMP) 
BEGIN
	IF min_test_def_id IS NOT NULL AND min_test_plan_id IS NOT NULL THEN
	    IF min_last_modified IS NOT NULL THEN
		    SELECT * 
		        FROM test_plan_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              (test_definition_id > min_test_def_id OR 
		              test_definition_id > min_test_def_id AND test_plan_id > min_test_plan_id) AND
		              last_modified >= min_last_modified AND
		              last_modified < max_last_modified
		        ORDER BY test_definition_id ASC, test_plan_id ASC
		        LIMIT max_rows;
	    ELSE
		    SELECT * 
		        FROM test_plan_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              (test_definition_id > min_test_def_id OR 
		              test_definition_id > min_test_def_id AND test_plan_id > min_test_plan_id) AND
		              last_modified < max_last_modified AND
		              deleted = false AND
		              active = true
		        ORDER BY test_definition_id ASC, test_plan_id ASC
		        LIMIT max_rows;
	    END IF;	        
	ELSE
	    IF min_last_modified IS NOT NULL THEN
		    SELECT * 
		        FROM test_plan_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              last_modified >= min_last_modified AND
		              last_modified < max_last_modified		              
		        ORDER BY test_definition_id ASC, test_plan_id ASC
		        LIMIT max_rows;
	    ELSE
		    SELECT * 
		        FROM test_plan_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              last_modified < max_last_modified AND
		              deleted = false AND
		              active = true
		        ORDER BY test_definition_id ASC, test_plan_id ASC
		        LIMIT max_rows;
		END IF;        
    END IF;	   
END // 

CREATE PROCEDURE get_maint_schedules (IN total_workers SMALLINT UNSIGNED, IN worker_num SMALLINT UNSIGNED, IN max_rows SMALLINT UNSIGNED, IN max_last_modified TIMESTAMP, IN min_schedule_id BIGINT UNSIGNED, IN min_test_def_id BIGINT UNSIGNED, IN min_last_modified TIMESTAMP) 
BEGIN
	IF min_schedule_id IS NOT NULL AND min_test_def_id IS NOT NULL THEN
	    IF min_last_modified IS NOT NULL THEN
		    SELECT * 
		        FROM test_definition_schedule_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              (schedule_id > min_schedule_id OR
		              schedule_id = min_schedule_id AND test_definition_id > min_test_def_id) AND
		              last_modified >= min_last_modified AND
		              last_modified < max_last_modified AND
		              is_maintenance = true
		        ORDER BY schedule_id ASC, test_definition_id ASC
		        LIMIT max_rows;
	    ELSE
		    SELECT * 
		        FROM test_definition_schedule_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              (schedule_id > min_schedule_id OR
		              schedule_id = min_schedule_id AND test_definition_id > min_test_def_id) AND
		              last_modified < max_last_modified AND
		              deleted = false AND
		              active = true AND
		              is_maintenance = true
		        ORDER BY schedule_id ASC, test_definition_id ASC
		        LIMIT max_rows;
	    END IF;	        
	ELSE
	    IF min_last_modified IS NOT NULL THEN
		    SELECT * 
		        FROM test_definition_schedule_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              last_modified >= min_last_modified AND
		              last_modified < max_last_modified AND
		              is_maintenance = true
		        ORDER BY schedule_id ASC, test_definition_id ASC
		        LIMIT max_rows;
	    ELSE
		    SELECT * 
		        FROM test_definition_schedule_vw 
		        WHERE test_definition_id % total_workers = worker_num AND
		              last_modified < max_last_modified AND
		              deleted = false AND
		              active = true AND
		              is_maintenance = true
		        ORDER BY schedule_id ASC, test_definition_id ASC
		        LIMIT max_rows;
		END IF;        
    END IF;	   
END // 

CREATE PROCEDURE poll_test_queue (IN vucId BIGINT UNSIGNED, IN supportsF BIGINT UNSIGNED, IN maxRows INT UNSIGNED) 
BEGIN 
	SELECT * 
	FROM test_queue_vw
	WHERE status = 1 AND
	      requires_f & supportsF = requires_f AND
	      lcp_id IN (SELECT lcp_id 
	                 FROM vu_controller_lcp 
	                 WHERE vu_controller_lcp.vuc_id = vucId)
	ORDER BY
	   priority ASC, enqueued_at ASC
	LIMIT
	   maxRows;
	      
END // 


CREATE PROCEDURE get_scheduler (IN schedulerId BIGINT UNSIGNED)
BEGIN
	DECLARE total_workers INT(4);
	
	SELECT count(scheduler_id) INTO @total_workers FROM scheduler;
	
	SELECT scheduler_id, worker_num, @total_workers AS total_workers FROM scheduler WHERE scheduler_id = schedulerId;
	
END //	

DELIMITER ; 