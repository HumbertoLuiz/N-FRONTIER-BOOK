CREATE TABLE IF NOT EXISTS `picture` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) NOT NULL,
  `content_length` bigint NOT NULL,
  `content_type` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;