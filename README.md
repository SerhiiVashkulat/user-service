RESTful API USERS

Technologies:

Spring Boot, Postgres, Flyway, MapStruct, Lombok

How to start:

1.Clone project from repository

2.Create database

2.1 Change credential in the configuration file(url,user,password)

2.1 Or use docker, enter command in terminal:

	- docker compose up.

Api requests:

Post /api/v1/users - creat user

Put /api/v1/users/{id} - update user

Patch /api/v1/users/{id} - update user names

Get /api/v1/users - get users by dates 

 Json request:

 Post
 
   	{
	 "email": "user51@xamplem.com",
	 "firstName": "Serhii",
	 "lastName": "Vashkulat",
	 "birthDate": "2005-05-05",
	 "phoneNumber":"123-567-9873",
	 "address": {
	 "country":"Ukraine",
	 "apartment":"123",
	 "street": "123 Main St",
	 "streetNumber":"1234",
	 "city": "Example City",
	 "zip": "12345"
	}
	}



 
-------------------
Put
	
 	{
	 "email": "user55@xamplem.com",
	 "firstName": "Serega",
	 "lastName": "Vashkulat",
	 "birthDate": "2006-15-15",
	 "phoneNumber":"123-564-9873",
	 "address": {
	 "country":"Ukraine",
	 "apartment":"126",
	 "street": "124 Sof St",
	 "streetNumber":"12",
	 "city": "Kyiv City",
	 "zip": "1239"
	}
	}

 ----------------------
 Path
 
	  {
	   "firstName": "Matthew",
	   "lastName": "Miller",
	 }
	        			
