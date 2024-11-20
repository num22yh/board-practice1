CREATE DATABASE IF NOT EXISTS ebrainsoft_study;
USE ebrainsoft_study;

CREATE TABLE `categories` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `category_name` varchar(45) NOT NULL,
                              PRIMARY KEY (`id`)
);

CREATE TABLE `posts` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `category_id` int NOT NULL,
                         `author` varchar(45) NOT NULL,
                         `password` varchar(45) NOT NULL,
                         `title` varchar(100) NOT NULL,
                         `content` text NOT NULL,
                         `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         `view_count` int DEFAULT 0,
                         PRIMARY KEY (`id`),
                         KEY `category_id` (`category_id`),
                         FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ;


CREATE TABLE `attachments` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `post_id` int NOT NULL,
                               `original_name` varchar(45)  NOT NULL,
                               `stored_name` varchar(45) NOT NULL,
                               `logical_path` varchar(255)  NOT NULL,
                               `physical_path` varchar(255) NOT NULL,
                               `size` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `post_id` (`post_id`),
                               FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);


CREATE TABLE `comments` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `post_id` int NOT NULL,
                            `author` varchar(45) NOT NULL,
                            `content` text NOT NULL,
                            `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `post_id` (`post_id`),
                            FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

