To populate the database in your own virtual environment, do the following:

	To create all objects:

	prompt:> runme_create_all.sh




	To drop all objects:

	prompt:> runme_drop_all.sh



If you want to execute individual scripts inside this directory each '.sql' file will also load into the database on its own.  
Just follow the example in one of the 'runme' scripts to see how to log into MySQL and execute a script.  You can also log in 
as follows:

	prompt:> mysql -u root -proot

	mysql> source <filename.sql>



Good luck!  Let me know if you have any questions.


James
