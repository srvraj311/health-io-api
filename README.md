# Health IO API

This ia a springboot api for my personal project health-io, which is a healthcare automation platform.

Health.io leverages artificial intelligence (AI) to automate core healthcare functionalities, aiming to improve efficiency and streamline processes across the healthcare ecosystem. This includes utilizing AI for digital prescription generation, intelligent medication recommendations, and enhanced patient and provider experiences. Additionally, Health.io incorporates AI-powered hospital account management to optimize administrative tasks and financial processes.

## Documents for Project
#### [Project Paper](https://docs.google.com/document/d/1vwjnvhNIuz2VNJmeZEhNQIgMaYdUmzjHRisOBvx4wUc/edit?usp=sharing)

## Architecture

This project is build using spring boot v3.2.2 for a REST api mongodb for storing and retrieving data.
The build platform is maven.

![](https://res.cloudinary.com/srvraj311/image/upload/v1711698075/image2_pzzwzw.png)

![](https://res.cloudinary.com/srvraj311/image/upload/v1711698076/image9_vyntfc.png)

```
Some structures have changed in recent versions and may not be same what shown in image.
This code is only for portfolio purpose, Actual working code is in a private repository. 
Please email me if you want to see original code
 ```
 
### Local Setup
To run this you need to provide a  ```application.properties``` file in ```src/main/resource/``` directory

```
spring.mongodb.embedded.version=3.5.5
spring.data.mongodb.uri=${MONGO_URI}
debug=true

# Loading Web Pages
spring.web.resources.static-locations=classpath:/static


#Spring Security
#spring.security.user.name=${SPRING_SECURITY_USER}
#spring.security.user.password=${SPRING_SECURITY_PASSWORD}

spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=${YAHOO_EMAIL}
spring.mail.password=${YAHOO_PASSWORD}

server.port=${PORT:8080}

#Debug
logging.level.org.springframework.web=DEBUG
spring.mvc.log-request-details=true
logging.level.org.springframework.data.mongodb=DEBUG

# Other properties
spring.mail.properties.mail.debug=true
spring.mail.properties.mail.transport.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# TLS , port 587
spring.mail.properties.mail.smtp.starttls.enable=true

# SSL, post 465
spring.mail.properties.mail.smtp.socketFactory.port = 465
spring.mail.properties.mail.smtp.socketFactory.class = javax.net.ssl.SSLSocketFactory

encryption_key=${SPRING_SECURITY_ENCRYPTION_KEY}


```


