# TpSocket-TCP

Trabajo Práctico: Sockets TCP/UDP

Grupos:

Estudiantes con apellidos que inician con letra “A” hasta “G”
Integrantes
- Luis Cañete
- Soledad Ayala
- Edher Coronel
- Lucas Candia
- José González

INSTALACIÓN 

- Se debe contar con java 8 instalado.
- Clonar el repositorio en un directorio del git 
$ git clone https://github.com/SoleAyala/TpSocket-TCP.git
- En cualquier ide (como Eclipse o Intellij idea) seleccionamos la option File -> Import, y buscamos la carpeta que contiene el proyecto clonado anteriormente ( en caso de Eclipse seleccionar antes Existing Maven Project para la busqueda de carpetas, y en caso de Intellij idea al cargar la carpeta seleccionar la pestaña meaven del costado derecho y cargar cada pom existente.
- Nos posicionamos dentro de cada proyecto (TcpClient / TcpServer) y ejecutamos en cada uno el siguiente comando:
$ meaven clean package

EJECUCIÓN
 
- Dentro de cada Proyecto (TcpClient / TcpServer) se ha generado un archivo .jar, esos archivos fueron generados mediante el comando "meaven clean package" ejecutado anteriormente.
- Ejecutar el siguiente comando para levantar el Server:
$ java -jar server.jar
- Ejecutar el siguiente comando para levantar el Client: 
$ java -jar client.jar

SERVICIOS PROVEÍDOS POR EL SERVIDOR

1. Enviar orden de reconexión
    * Sin parámetros de entrada 
2. Registrar consumo
    * Parámetro: Consumo (Nro que especifica la cantidad de consumo a registrar)
3. Informar Conectividad
    * Sin parámetros de entrada
4. Enviar orden de desconexión
    * Sin parámetros de entrada
5. Listar NIS activos
    * Sin parámetros de entrada
6. Listar NIS inactivos
    * Sin parámetros de entrada
7. Salir
