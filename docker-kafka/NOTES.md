https://sookocheff.com/post/kafka/kafka-quick-start/

software
 - docker for mac
 - kitematic
 - virtual box
 - docker-machine create default   
 - brew install kafkacat
 
 
#### Crear maquina por defecto
`docker-machine create default` - crear maquina por defecto

`docker-machine ls` - consultar las maquinas

`docker-machine ip default` - ip de la maquina por defecto

export DOCKER_HOST=tcp://192.168.99.100:2376 -- la maquina asociada al docker_host es la activa

`docker rmi $(docker images -a -q)`- borrar todas las imagenes

`docker ps -a` - listar todos los containers

`eval $(docker-machine env)`

docker run -p 2181:2181 -p 9092:9092 
--env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` 
--env ADVERTISED_PORT=9092 spotify/kafka


kafkacat -P -b 192.168.99.100:9092 -t test
kafkacat -C -b 192.168.99.100:9092 -t test


docker run -p 2181:2181 -p 9092:9092 
--env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` --env ADVERTISED_PORT=9092 spotify/kafka
