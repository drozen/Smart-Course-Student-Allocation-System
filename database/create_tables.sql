--
-- First get into the scheduling database;
--

use scheduling;


--
-- Now create the tables needed for the project;
--

--
-- USERS table
--

create table users
(
gt_id varchar(20) not null,
first_name varchar(30),
last_name varchar(30),
password varchar(20),
constraint users_pk primary key (gt_id)
);


--
-- STUDENTS table
--

create table students
(
gt_id varchar(20) not null,
good_standing_flg integer,
total_credit_hours integer,
constraint users_pk primary key (gt_id)
);


--
-- LOGIN_HISTORY table
--

create table login_history
(
gt_id varchar(20) not null,
login_date date,
hostname varchar(50),
constraint login_history_pk primary key (gt_id, login_date)
);


--
-- COURSE_RESRC_LU table
--

create table course_resrc_lu
(
resource_type_cd varchar(5),
resource_desc varchar(200),
constraint course_resrc_lu_pk primary key (resource_type_cd)
);


--
-- COURSE_RESRC table
--

create table course_resrc
(
gt_id varchar(20),
course_ref_nbr varchar(15),
resource_type_cd varchar(5)
);


--
-- COURSE_RESRC_HIST table
--

create table course_resrc_hist
(
gt_id varchar(20),
course_ref_nbr varchar(15),
resource_type_cd varchar(5)
);

--
-- COURSE_RESRC_AVAIL table
--

create table course_resrc_avail
(
gt_id varchar(20),
course_ref_nbr varchar(15),
term_cd varchar(10),
resource_type_cd varchar(5),
availability_flg varchar(1)
);


--
-- COURSE_CATALOG table
--

create table course_catalog
(
course_ref_nbr varchar(15),
course_id varchar(15),
course_cd varchar(5),
course_nbr varchar(10),
course_title_desc varchar(200),
credit_hours integer,
class_size_limit integer,
foundational_flg varchar(1),
constraint course_catalog_pk primary key (course_ref_nbr)
);


--
-- COURSES_OFFERED table
--

create table courses_offered
(
course_ref_nbr varchar(15),
term_cd varchar(10),
gt_id varchar(20),
constraint courses_offered_pk primary key (course_ref_nbr)
);


--
-- COURSE_REQUISITE table
--

create table course_requisite
(
course_ref_nbr varchar(15),
reqste_course_ref_nbr varchar(15),
prerequisite_flg integer,
constraint course_requisite_pk primary key (course_ref_nbr, reqste_course_ref_nbr)
);


--
-- COURSE_ASSIGMENT table
--

create table course_assignment
(
course_ref_nbr varchar(15),
gt_id varchar(20),
term_cd varchar(10),
constraint course_assignment_pk primary key (course_ref_nbr, gt_id)
);


--
-- COURSE_ASSIGMENT_HIST table
--

create table course_assignment_hist
(
course_ref_nbr varchar(15),
gt_id varchar(20),
term_cd varchar(10),
constraint course_assignment_pk primary key (course_ref_nbr, gt_id)
);
--
-- TERM_CODE_LU table
--

create table term_code_lu
(
term_cd varchar(10),
term_cd_desc varchar(200),
constraint term_code_lu_pk primary key (term_cd)
);


--
-- STUDENT_COURSE_HIST table
--

create table student_course_hist
(
gt_id varchar(20),
course_ref_nbr varchar(15),
term_cd varchar(10),
credit_hours integer,
constraint student_course_hist_pk primary key (gt_id, course_ref_nbr, term_cd)
);


--
-- STUDENT_COURSE_REQUEST table
--

create table student_course_request
(
gt_id varchar(20),
course_ref_nbr varchar(15),
term_cd varchar(10),
course_req_rank_cd varchar(5),
constraint student_course_req_pk primary key (gt_id, course_ref_nbr, term_cd)
);



--
-- STUDENT_COURSE_REQUEST_HIST table
--

create table student_course_request_hist
(
gt_id varchar(20),
course_ref_nbr varchar(15),
term_cd varchar(10),
course_req_rank_cd varchar(5),
constraint student_course_req_pk primary key (gt_id, course_ref_nbr, term_cd)
);


