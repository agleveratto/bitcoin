# Bitcoin Api

***
### Levantar aplicación

Use el siguiente comando para correr la aplicación correctamente.

```
mvnw spring-boot:run
```
El objetivo de este pequeño ejercicio es demostrar la utilización del api de Streams (java.util.stream)
Construir un Micro Servicio que haciendo uso del siguiente servicio REST (https://cex.io/api/last_price/BTC/USD) realice una llamada recurrente cada 10 segundos, almacene los datos y que exponga a través de un API REST las siguientes funcionalidades:
1. Obtener el precio del Bitcoin en cierto Timestamp.
2. Conocer el promedio de valor entre dos Timestamps así como la diferencia porcentual entre ese valor promedio y el valor máximo almacenado para toda la serie temporal disponible.
3. Incorporar un archivo READ.ME que contenga una descripción de la solución propuesta así como instrucciones de ejecución en entorno local.

####Indicaciones
   - La aplicación deberá estar desarrollada usando Springboot y subida a un repositorio en github con permisos públicos de acceso y clonado
   - La aplicación deberá ser ejecutada en entorno local sin necesidad de dockerización ni de otro software más que java 1.8
   - El uso de frameworks accesorios queda a la elección del candidato
   - La persistencia de información se realizará en una estructura de datos en memoria lo más optimizada posible.
   
####Puntos extras si
  - Se incorpora un conjunto de test unitarios que demuestran la corrección de la solución
  - Se usa WebClient



####Solución
  NOTA: dateFormat --> yyyy-mm-ddThh:mm:ss (2021-09-16T13:23:05)

###GET bitcoin price given a date

  - URL: localhost:8080/v1/api/bitcoin/priceByDate?createdAt=dateFormat
  - HTTP Method: GET
  - respuesta esperada OK 

`48578.6`

  - respuesta esperada con error 

`{ "errors": 
  [
    {
      "code": 404,
      "type": "Resource not found",
      "description": "Price bitcoin not found for date: 2021-09-16T13:23:04"
    }
  ]
}`

###GET average price and perceptual differential given two dates

- URL: localhost:8080/v1/api/bitcoin/priceBetweenDates?sinceDate=dateFormat&untilDate=dateFormat
- HTTP Method: GET
- respuesta esperada OK 

`{
"averageBitcoinPrice": 553.2162790697674,
"diffBitcoinPrice": "98.84%"
}`

- respuesta esperada con error

`{ "errors":
[
{
"code": 404,
"type": "Resource not found",
"description": "Price bitcoin not found for date: Price bitcoin not found between: 2021-09-18T10:42:34 and 2021-09-19T17:42:34"
}
]
}`

####Documentación

- Se agrega documentación tanto diagramas de componentes como casos de uso.

  -[Components Diagram](docs/components/components-diagram.md)

  -[Use Cases Diagrams](docs/usecases/use-cases-diagrams.md)
