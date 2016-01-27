# sis-interview-test
A demonstration restful web service providing access to a table of football teams

a simple web service.

No large framework used as the service was trivial.

accepts the following commands 

* PUT to store data from a JSON object containing the String fields city, owner, name, competition together with the array of String players, NOTE this will not allow a record to be created for a team with the same name as an existing team.
* GET /team/id to get team identified by id as a JSON object.
* GET /team/ALL to get all known teams as a JSON object.
* POST /team/id to update a the database record identified by id, NOTE that this does not allow a team name to changei, and the record must first exist.





