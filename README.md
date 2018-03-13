### Run MySQL by Docker 
Before running web application, we have to install docker.
```
docker pull mysql

docker run --name spring-mysql -p 3306:3306  -e MYSQL_ROOT_PASSWORD=123456789 -e MYSQL_DATABASE=backend -d mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

### Register Account 
Using curl command to send request for registering account.

```
 curl -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"1234" }' http://localhost:8080/user/signup
```

```

```
### Login 
Login to obtain token

```
curl -d 'username=admin&password=1234' http://localhost:8080/user/login

```

Response is :

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjoidXNlciIsImlhdCI6MTUyMDgzNTU5M30.7Z3wG9-riv6FFe5wB-gTr4QufW4NqZrGBhAUZvVwVHE
```

### Access Restrict API with Token 
Access resource api 

```
 curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjoidXNlciIsImlhdCI6MTUyMDgzNTU5M30.7Z3wG9-riv6FFe5wB-gTr4QufW4NqZrGBhAUZvVwVHE" -d "username=admin" localhost:8080/resource/user/username
```

Response is :
```
{"username":"admin","password":"1234","created":1520835586000}
```



