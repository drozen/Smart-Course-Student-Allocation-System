--
-- Grant necessary privileges to our database users.  In this case we're going to just grant everything
--    for now to each user, just to make it simple.
--

grant all on scheduling.* to admin;

grant all on scheduling.* to student;
