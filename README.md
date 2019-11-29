![tag jdk8](https://img.shields.io/badge/tag-jdk8-orange.svg)
![technology Kotlin](https://img.shields.io/badge/technology-Kotlin-olive.svg)
![technology VertX](https://img.shields.io/badge/technology-VertX-blue.svg)
![technology Gradle](https://img.shields.io/badge/technology-Gradle-green.svg)
# Fraud Rules Engine
Esta API se encarga de administrar y evaluar flujos de trabajo.

## Prerrequisitos
1. Docker
2. Java 1.8

## Tecnologías
* Gradle
* Kotlin
* VertX

## Pasos para correr en Local
1. Crear la variable de entorno `ENV=dev` (E.g. usando `export ENV=dev` o seteandolo en IntelliJ)
2. Ejecutar `docker-compose up` para correr la imagen de Dockerfile con PostgreSQL
3. Ejecutar `./gradlew run`
4. Para probar hacer una http GET request a <localhost:8080/api/fraud-rules-engine/health-check>

## Pasos para ejecutar los tests
1. Ejecutar `docker-compose up` para correr la imagen de Dockerfile con PostgreSQL 
2. Si se desean correr los tests por separado usando jUnit, setear la variable de enterno `ENV=test`. Si se usa el task 
test de gradle, esto no es necesario.

## Documentación de la API
#### Crear flujo de trabajo
`POST api/fraud-rules-engine/workflow`: Ejecuta la creación de un flujo de trabajo, retornando el flujo de trabajo creado.
##### Request
```
curl -X POST \
    http://localhost:8080/api/fraud-rules-engine/workflow \
    -H 'Content-Type: application/json' \
    -H 'cache-control: no-cache' \
    -H 'X-Auth-User: 191450503' \
    -d '{
        "countryCode": "CO",
        "workflow": "workflow 'Sample' ruleset 'Sample' 'sample rule' d = 100 return allow default block end" 
    }' 
```
##### Response
```json
{
    "id": 1,
    "country_code": "co",
    "name": "Sample",
    "version": 1,
    "workflow": "workflow 'Sample' ruleset 'Sample' 'sample rule' d = 100 return allow default block end",
    "userId": "191450503",
    "created_at": "2019-10-31T08:31:58.129"
}
```

#### Obtener flujo de trabajo (por código de país, nombre y versión)
`GET api/fraud-rules-engine/workflow/:countryCode/:name/:version`: Obtener un flujo de trabajo por código de país, nombre y versión 
##### Request
``` 
curl -X GET \
    http://localhost:8080/api/fraud-rules-engine/workflow/co/Sample/1 \
    -H 'Content-Type: application/json' \
    -H 'cache-control: no-cache'
```
##### Response
```json
{
    "id": 1,
    "country_code": "CO",
    "name": "Sample",
    "version": 1,
    "workflow": "workflow 'Sample' ruleset 'Sample' 'sample rule' d = 100 return allow default block end",
    "userId": "191450503",
    "created_at": "2019-10-31T08:31:58.129"
}
```

#### Obtener flujos de trabajo (por código de país y nombre)
`GET api/fraud-rules-engine/workflow/:countryCode/:name`: Obtener flujos de trabajo por código de país y nombre ordenados 
de forma descendente por la fecha de creacion.   
##### Request
``` 
curl -X GET \
    http://localhost:8080/api/fraud-rules-engine/workflow/co/Sample/1 \
    -H 'Content-Type: application/json' \
    -H 'cache-control: no-cache'
```
##### Response
```json
[
    {
        "id": 1,
        "country_code": "CO",
        "name": "Sample",
        "version": 1,
        "workflow": "workflow 'Sample' ruleset 'Sample' 'sample rule' d = 100 return allow default block end",
        "userId": "191450503",
        "created_at": "2019-10-31T08:31:58.129"
    }
]
```

#### Activar flujo de trabajo
`POST api/fraud-rules-engine/workflow/:countryCode/:name/activate`: Ejecuta la activación de un flujo de trabajo, retornando el flujo de trabajo activado.

`Nota: El parametro 'name' debe ser escapado.` 
##### Request
```
curl -X POST \
    http://localhost:8080/api/fraud-rules-engine/workflow/co/Sample \
    -H 'Content-Type: application/json' \
    -H 'cache-control: no-cache' \
    -H 'X-Auth-User: 191450502' 
```
##### Response
```json
{
    "id": 1,
    "countryCode": "co",
    "name": "Sample",
    "version": 1,
    "workflow": "workflow 'Sample' ruleset 'Sample' 'sample rule' d = 100 return allow default block end",
    "userId": "191450503",
    "created_at": "2019-10-31T08:31:58.129"
}
```