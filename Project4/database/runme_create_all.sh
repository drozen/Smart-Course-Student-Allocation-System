#!/bin/bash


mysql -u root -proot < create_database.sql
mysql -u root -proot < create_tables.sql
mysql -u root -proot < create_users.sql
mysql -u root -proot < grant_privileges.sql
mysql -u root -proot < load_data.sql
