### Simple spring kafka demo

This program is written using the spring boot framework with maven plugin and is designed in a modular project and includes a number of modules. This program is simply run on Docker.with using postman you can send data to the first service then receive it by the second service. In this project, the main focus was on Kafka and the usege of Kafka with spring.
![](https://github.com/alirezaalj/simple-spring-kafka-demo/blob/master/img/Screenshot_20210731_162309.png)
my website: [https://alirezaalijani.ir](https://alirezaalijani.ir "https://alirezaalijani.ir")
##### Goals
- create simple kafka producer and consumer
- send json data to kafka and receive them - using JsonSerializer - JsonDeserializer
- running 3 cluster of kafka on docker
- run 2 consumer service and 1 producer service

# How to use
##### Project dependencies
- maven  : [How to use or Download](https://maven.apache.org/ "How to use or Download")
- docker : *Must have Docker installed* 
 : [https://docker.com/](https://docker.com/ "https://docker.com/")
- open terminal in project folder
- If Docker is installed correctly, with this command, two containers "ir.alirezaalijnai/web.to.kafka"  and "ir.alirezaalijnai/kafka.to.log " will be created automatically on your system after downloading the requirements.

```shell
 mvn clean install
```

#### Run
1. go to "docker-compose" folder 
2. start by [docker-compose](https://docs.docker.com/compose/ "docker-compose")
3. this command start all services
```shell
docker-compose --env-file env up
```

4. If you just want to star Kafka containers and start two services with your editor . follow this command
```shell
docker-compose --env-file env.dev up
```
#### Useing

1. In the first part, you should see the logs of the two instance of continer ir.alirezaalijnai/kafka.to.log in Docker. With the following command, you can see the code of both containers. These codes will be different in your system.
```shell
docker ps
```
![](https://github.com/alirezaalj/simple-spring-kafka-demo/blob/master/img/1.png)
2. go to both container logs
```shell
docker logs <CONTAINER ID > -f
```
![](https://github.com/alirezaalj/simple-spring-kafka-demo/blob/master/img/2.png)
3. open post man to send post request to web.to.kafka service
send to : https://localhost:8080/user/push
with this body:
```json
{
    "id":0,
    "username":"usernaem",
    "fullName":"fullName",
    "email":"user@mail.com",
    "hobbies":[
        {
            "name":"hobbies-name",
            "rate":5
        },
        {
            "name":"hobbies-name",
            "rate":5
        },
        {
            "name":"hobbies-name",
            "rate":5
        }
    ]
}
```
4. after send requests web to kafka service push them to kakfa and kafka to log service receive them and pring them to console

5. stop all
```shell
docker-compose --env-file env.dev down
```
