DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `content` TEXT NULL,
  `user_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `comment_count` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `date_index` (`created_date` ASC));

  DROP TABLE IF EXISTS `user`;
  CREATE TABLE `user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL DEFAULT '',
    `password` varchar(128) NOT NULL DEFAULT '',
    `salt` varchar(32) NOT NULL DEFAULT '',
    `head_url` varchar(256) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `fromid` INT(11) NOT NULL,
    `toid` INT(11) NOT NULL,
    `content` TEXT NOT NULL,
    `conversation_id` INT(11) NOT NULL,
    `created_date` DATETIME NOT NULL,
    PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `content` TEXT NOT NULL,
    `user_id` INT(11) NOT NULL,
    `created_date` DATETIME NOT NULL,
    `entity_id` INT(11) NOT NULL,
    `entity_type` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
