#### docker maven plugin
https://github.com/spotify/docker-maven-plugin#use-a-dockerfile

#### volumes
https://docs.docker.com/engine/admin/volumes/volumes/
https://docs.docker.com/engine/reference/commandline/export/

#### mongodb, docker and robomongo
https://codehangar.io/mongodb-image-instance-with-docker-toolbox-tutorial/

#### docker install
 - docker for mac
 - virtual box
 - docker-machine create default

#### useful commands
`docker-machine create default` - crear maquina por defecto

`docker-machine start default` - iniciar la maquina por defecto

`docker-machine ls` - consultar las maquinas

`docker-machine ip default` - ip de la maquina por defecto

export DOCKER_HOST=tcp://192.168.99.100:2376 -- la maquina asociada al docker_host es la activa

`docker rmi $(docker images -a -q)`- borrar todas las imagenes

`docker ps -a` - listar todos los containers

`docker-machine env` - entorno de la maquina activa

`eval $(docker-machine env)` - configurar el shell

`docker rmi $(docker images -f "dangling=true" -q)` - eliminar imagenes <none>

`docker network inspect renthell_default` - inspect network

`docker info`- docker INFO

`docker exec -ti <container_name> bash` - docker container bash
