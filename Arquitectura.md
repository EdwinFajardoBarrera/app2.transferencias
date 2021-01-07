# Aplicación de Pagos
## Indice
- 1.- [Introducción](#1)  
	- 1.1.- [Propósito](#11)
	- 1.2.- [Alcance](#12)
	- 1.3.- [Documentos de referencia](#13)
- 2.- [Arquitectura](#2)  
	- 2.1.- [Descripción de la arquitectura](#21)
	- 2.2.- [Definición técnica de Microservicios.](#22)
	- 2.3.- [Diagrama de arquitectura.](#23)
	- 2.4.- [Diagrama de secuencia para los procesos.](#24)
	- 2.5.- [Diagrama de la base de datos](#25)
	- 2.6.- [Descripción de las entidades](#26)
- 3.- [Documentación de la API](#3)  
- 4.- [Criterios de calidad](#4)

## 1. Introducción
### 1.1 Propósito
El software a construir tiene, como objetivo principal, permitir al usuario el correcto y seguro manejo de transferencias de su dinero de una cuenta origen a una cuenta destino.
Para ello es necesario el uso de dos apps.
El propósito de la primera app es la recolección y guardado de los datos del usuario.
El propósito de la segunda app es la validación de los datos de transacción del usuario.
   
### 1.2 Alcance 
Se desea facilitar al usuario el manejo de sus transferencias de una cuenta origen a una destino, de manera que se le ofrezca una interfaz sencilla y clara donde pueda ingresar sus datos personales y bancarios y realizar las transferencias que considere necesarias.
El producto estará conformado por una app con la que el usuario podrá comunicarse, una base de datos donde los datos del usuario serán almacenados y una api de pagos encargada de recolectar los pagos. De igual manera contará con el apoyo de servicios como kafka para el procesamiento de pagos y DLQ para el almacenamiento de mensajes.
   
### 1.3 Documentos de referencia
https://markdown.es/sintaxis-markdown/  
https://jwt.io/introduction/  
https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-dead-letter-queues.html  
https://en.wikipedia.org/wiki/Dead_letter_queue  
https://kafka.apache.org/documentation/  
   
## 2. Arquitectura
### 2.1 Descripción de la arquitectura.

La arquitectura del sistema a gran escala consta de 2 aplicaciónes distribuídas centradas en microservicios, comunicadas de manera asíncrona por una servicio de queues de mensajes como RabbitMQ o Apache Kafka.

Tanto el microsericio 1 como el 2 constan de una arquitectura específica por capas que siguen la siguente jerarquía:

- main
	- java/mx/uady/aplicacion-pagos
		- exception
		- model
			- request
		- repository
		- rest
		- service
	- resources
- test

La capa exception es una capa vertical accesible por las demás capas, mientras que, para seguir el flujo del sistema, se requiere pasar por cada una de las capas involucradas horizontalmente en el siguiente orden:

- rest
- model/request
- service
- repository
- model


### 2.2 Definición técnica de Microservicios. 

El sistema de pagos consiste en 2 aplicaciones que realizan las tareas del procesamiento de pagos:

   **App 1:**

Esta es la que interactua directamente con el cliente, debido a que ofrece los servicios REST de:

Autentificación, como son el registro de usuarios, e inicio de sesión. La sesión del usuario es mantenida con JSON Web Tokens firmados en el servicio de inicio de sesión y validados con middleware cuando se realiza una nueva petición de una transferencia para validar que hay un usuario autentificado para la transacción.

Creación de ficheros de transacción de pagos en archivos CSV. Después de validar con middleware la autenficación del usuario se genera un archivo CSV con los datos de dicho usuario y los valores ingresados para la transferencia como la cuenta destino y el monto. Dichos datos también serán almacenados en una base de datos.

Visualización y actualización de estado de cuenta. El usuario puede ingresar dinero a su balance y consultar el estado de sus transacciónes.

**App 2:**

Ésta aplicación recibe sus entradas de una queue de Apache Kafka, las cuales son archivos JSON con los datos de la transacción de pagos los cuales son validados para ejecutar efectivamente la transacción o resolver una transacción fallida.

### 2.3 Diagrama de arquitectura.
**Diagrama de Arquitectura General**
![No image](/Diagramas/ArquitecturaGeneral.png)

Link : https://github.com/moninam/sicei-app/blob/master/Diagramas/ArquitecturaGeneral.png

**Diagrama de Arquitectura Especifico**
![No image](/Diagramas/ArquitecturaEspecifico.png)

Link : https://github.com/moninam/sicei-app/blob/master/Diagramas/ArquitecturaEspecifico.png

### 2.4 Diagrama de secuencia para los procesos.
**Proceso: Generar Pago**
![No image](/Diagramas/DiagramaSecuenciaPago.png)

**Proceso: Procesar Pago Fallido**
![No image](/Diagramas/DiagramaSecuenciaPagoFallido.png)

**Proceso: Obtener Balance**
![No image](/Diagramas/DiagramaSecuenciaBalance.png)

### 2.5 Diagrama de la base de datos
**Diagrama E-R de la Base de datos** 
![No image](/Diagramas/DiagramaER.png)

### 2.6 Descripción de las entidades

_Se enlistan las entidades consideradas para el sistema_

* [Usuario]()
&nbsp;

Representación de la cuenta de un usuario en el sistema; esta entidad será encargada de mapear
la información básica de acceso al sistema, como es ID, Clave y su nombre; ésta estará asociada a una 
cuenta bancaria.
Una cuenta de usuario estará unicamente asociada a una cuenta bancaria.

* [Cuenta]()



Representación de la cuenta bancaria de un usuario, la cual esta asociada a una cuenta de usuario.La entidad
mapeara toda la información asociada a la cuenta monetaría de un usuario, cuyos datos son: Número de cuenta y el balance (Cantidad de dinero disponible).
Con dicha cuenta el usuario podrá realizar operaciones para transferir y recibir dinero.
Una cuenta bancaria estará unicamente asociada a una cuenta de usuario
Una cuenta podrá realizar "n" pagos.
* [Pagos]()
&nbsp;

Representación de una transacción de pago de un usuario, esta entidad estará asociada a una cuenta de usuario, de la cual obtendrá el dinero para realizar la operación. La entidad mapeara toda la información asociada a un pago, cuyos datos son: Monto del pago, Cuenta de Origen, Cuenta Destino, Estado de la operación, Fecha de Registro de la operación y Fecha de procesamiento de la operación.
Un pago estará asociado unicamente a una cuenta.

## 3. Documentación de la API
```
GET /users
HTTP/1.1 200 Succes
[
	{
		"id": Integer,
		"name": String,
	},
	...	
]
```

```
GET /user/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"name": String,
}
```

```
POST /user
{
	"id": Integer,
	"name": String,
	"password": String
}
HTTP/1.1 201 Created
```

```
PUT /user/:id
{
	"id": Integer,
	"name": String,
	"password": String
}
HTTP/1.1 200 Succes
```

```
DELETE /user/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"name": String,
}
```

```
GET /accounts
HTTP/1.1 200 Succes
[
	{
		"id": Integer,
		"account_number": String,
		"balance": Double
	},
	...	
]
```

```
GET /account/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"account_number": String,
	"balance": Double
}
```

```
POST /account
{
	"id": Integer,
	"account_number": String,
	"balance": Double
}
HTTP/1.1 201 Created
```

```
PUT /account/:id
{
	"id": Integer,
	"account_number": String,
	"balance": Double
}
HTTP/1.1 200 Succes
```

```
DELETE /account/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"account_number": String,
	"balance": Double
}
```

```
GET /payments
HTTP/1.1 200 Succes
[
	{
		"id": Integer,
		"amount": Double,
		"from_account_id": Integer,
		"to_account_id": Integer,
		"status": String [Enum],
		"register_date": String,
		"processing_date": String
	},
	...	
]
```

```
GET /payment/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"amount": Double,
	"from_account_id": Integer,
	"to_account_id": Integer,
	"status": String [Enum],
	"register_date": String,
	"processing_date": String
}
```

```
POST /payment
{
	"id": Integer,
	"amount": Double,
	"from_account_id": Integer,
	"to_account_id": Integer,
	"status": String [Enum],
	"register_date": String,
	"processing_date": String
}
HTTP/1.1 201 Created
```

```
PUT /payment/:id
{
	"id": Integer,
	"amount": Double,
	"from_account_id": Integer,
	"to_account_id": Integer,
	"status": String [Enum],
	"register_date": String,
	"processing_date": String
}
HTTP/1.1 200 Succes
```

```
DELETE /payment/:id
HTTP/1.1 200 Succes
{
	"id": Integer,
	"amount": Double,
	"from_account_id": Integer,
	"to_account_id": Integer,
	"status": String [Enum],
	"register_date": String,
	"processing_date": String
}
```

## 4. Criterios de calidad

### - Introducción

El estándar ISO/IEC 9126 presenta la calidad del software como un conjunto de seis características globales:  

**Funcionalidad:** Las funciones del software son aquellas que buscan satisfacer las necesidades del usuario.  
**Confiabilidad:** La capacidad del software de mantener su rendimiento bajo ciertas condiciones durante cierto período de tiempo.  
**Usabilidad:** Basada en el esfuerzo necesario para utilizar el software por parte de un grupo de usuarios.  
**Eficiencia:** Basada en la relación entre el nivel de rendimiento del software y el volumen de recursos utilizado, bajo ciertas condiciones.  
**Capacidad de mantenimiento:** Basada en el esfuerzo necesario para realizar modificaciones específicas.  
**Portabilidad:** Basada en la capacidad del software para ser transferido de un entorno a otro.  


### - Criterios de calidad de la aplicación de pagos: Características que nos interesan    

Basándonos en el estándar ISO/IEC 9126, decidimos definir la calidad de nuestro producto en torno en las siguientes características:

**Funcionalidad:** 
Es necesario que se satisfagan todos los requisitos del proyecto descritos debido a que estos tiene como fin último la satisfacción de las necesidades del usuario.  


**Usabilidad** 
El usuario ha de poder registrarse, iniciar sesión y realizar transferencias sin percanses causados por una falta de entendimiento de la interfaz proporcionada.
La aplicación debe resultar sencilla y clara al usuario.


