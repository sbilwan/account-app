The demo bank/account app implements the task. This is done using Spring boot, Hibernate over Spring Data JPA, Flyway, Mapstruct, Postgres, Docker.
1. Spring boot is the framework to provide REST, JPA, Tomcat
2. Mapstruct to map DTOs to Entities and vice-versa.
3. Flyway to provision tables (schemas) at the start of the application.
4. Postgres as the Db.
5. Docker as the container to provision postgres.


There are two main domains in the app
1. Client 

2. Account

#Assumptions

1. Client is the first requirement for the app.
2. An account will be associated with a client.
3. A client can have a number of accounts.
4. The balance stored in the account is in units to avoid any money exchange issues. It can be assumed in USD for simplicity sake's.
5. When an account is opened, it is always with a positive balance and in CR balance status. This is however, is an assumption and no business check is in place to verify this.
6. Amount withdrawn from the account is said to be Debit operation and hence the source account and debited account is the same.
7. Amount deposited to an account is said to be Credit operation and hence the destination account and credited account is the same.
8. Each account has its own overdrawn balance. The overdrawn balance will act as a credit loan from the bank. Client can use this extra amount for the amount transfer.
9. If an account is drawing from its overdrawn balance then it will be DR balance status.
10. If amount is deposited into a DR balance account then part amount is used to pay off the overdrawn balance and rest is then added towards the CR balance.

#Database
docker is being used to provision postgres db. It is the recommended way to run the application. 

Note:- If docker isn't available by any chance
then first create the `bank_app` db with user `admin` and password `admin` and then use the scripts inside `resources/db/migration` to create tables.  
 
 Assuming the docker is installed on the system. There is a `docker-compose` file in the directory `accountapp`.
 
 1. Start the postgres container by running command `docker-compose up -d`.
 
 2. After the successful completion message. Check whether the container is up using command `docker ps -a`
. You should see `postgres-container` with status `up`

3. To access the container use command ` docker exec -it postgres-container psql -d bank_app -U admin`

4. You could alternatively connect to the DB using pgadmin with dbname=bank_app, username=admin, password=admin, host=localhost, port=25432.

5. Application uses flyway to create tables in the container. But make sure the container is up and running before starting the application failing which the application won't find the DB.

6. Tables used are 
> client
 
> account

> transaction

#Run the Application 
The source code of the application can be directly pulled into an IDE (IntelliJ, Eclipse) and run from inside them or a jar can be created.
> Jar 

1. After downloading the source code, navigate to accountapp directory and start the postgres docker container using the command `docker-compose up -d`.
( If there is no docker success then use the classic db provisioning using the steps mentioned in Database section)

2. Create the jar file by running the command `mvn clean package`.

3. Navigate to target directory and run the command `java -jar accountapp-0.0.1-SNAPSHOT.jar`

#Endpoints 
 `Create a client`
 
> POST http://localhost:8080/demoaccountapp/v1/clients

 `Get all the clients`

> GET http://localhost:8080/demoaccountapp/v1/clients
 
 `Get a client by id`

> GET http://localhost:8080/demoaccountapp/v1/clients/{clientid}

`Create an account of  a client`

> POST http://localhost:8080/demoaccountapp/v1/accounts/clients/{clientid}

`Get all the account details of a client`

> GET http://localhost:8080/demoaccountapp/v1/accounts/clients/{clientid}

`Transfer the amount from one of the client account to another account`

> POST http://localhost:8080/demoaccountapp/v1/accounts/clients/{clientid}/transfer

`List all transactions associated with an account`

> GET http://localhost:8080/demoaccountapp/v1/accounts/{accountid}/transactions

POST payload and other details are available in the provided POSTMAN exported collection 
Bank_App.postman_collection.json

#Testing
Bank_App.postman_collection.json is the exported collection containing REST requests in an order that helps to understand the implementation and functionality of the application.

1. First 12 requests can be run straightway. These will help in populating the DB and some sanity testing.
2. 13th request onwards, there will be transfers and it is recommended to run them one after another and also advised to read the description.
3. There is a Junit test as well `TestAccountHandlerService` written to write some test scenarios for the transfer functionality.
