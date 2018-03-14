 Quick Start
===============================

Before running web application, You have to install docker.

After install docker, let's prepare environment.

```
git clone https://github.com/lucasko-tw/spring-boot-jwt.git

cd spring-boot-jwt/src/main/resources

docker pull mysql

docker-compose up -d
```


docker-compose.yml

```YML
dev_mysql:
  image: mysql:latest
  container_name: spring-mysql
  environment:
    - MYSQL_ROOT_PASSWORD=123456789
    - MYSQL_DATABASE=mydb
  ports:
    - 3306:3306
  volumes:
    - ./db-dump-dev:/docker-entrypoint-initdb.d
  restart: always
```


docker-compose can launch a mysql containner. database's name is mydb. root password is 123456789. In user table, we got a default user :

username: lucas
password: 1234
enabled: 1
role: ROLE_USER


### Run Web Application

1. Import Maven project into STS.
2. For WebApplication.java to run as Spring Boot App


### Login 
Login to obtain token

```
curl -d 'username=lucas&password=1234' http://localhost:8080/login

```

you will got a json response:

```
{
"jwttoken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWNhcyIsInJvbGVzIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTIxODcyNDc4fQ.TGKxznoTQTEjTY82ilq9hF7-SeSOQZsKU_w505_2s1VuJTHrgZxqgtODKN3ELrjLrEEzqUeIgaJ0eioUm3wPDA"
}
```

### Access Restrict API with Token 
Put your jwt token into request header,and access ROLE_USER resource api 

```
 curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWNhcyIsInJvbGVzIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTIxODcyNDc4fQ.TGKxznoTQTEjTY82ilq9hF7-SeSOQZsKU_w505_2s1VuJTHrgZxqgtODKN3ELrjLrEEzqUeIgaJ0eioUm3wPDA" localhost:8080/user/resources
```

Response is :

```
{"msg":"It is user resources"}
```

If you use ROLE_USER jwt token to acces ROLE_ADMIN, it would get access deny.
```
curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWNhcyIsInJvbGVzIjoiUk9MRV9VU0VSIiwiZXhwIjoxNTIxODcyNDc4fQ.TGKxznoTQTEjTY82ilq9hF7-SeSOQZsKU_w505_2s1VuJTHrgZxqgtODKN3ELrjLrEEzqUeIgaJ0eioUm3wPDA" localhost:8080/admin/resources
```


Response is :

```
{"timestamp":1521008726848,"status":403,"error":"Forbidden","message":"Access is denied","path":"/admin/resources"}
```





Detail
=====================

### WebSecurityConfig

```
@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				
				.antMatchers("/").permitAll()

				.antMatchers(HttpMethod.POST, "/login").permitAll()
				
				.antMatchers("/admin/**").hasRole("ADMIN")

				.antMatchers("/user/**").hasRole("USER")
				

				.anyRequest().authenticated().and()
				.addFilterBefore(new LoginFilter("/login", authenticationManager(), userService),
						UsernamePasswordAuthenticationFilter.class)
				// And filter other requests to check the presence of JWT in header
				.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
	}


```
