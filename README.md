# jav-eas-472-guides-reader-mngr
[EDUCATIVO] [UNIVERSIDAD PONTIFICIA JAVERIANA] Manager para lectura automática de guías de envío.

### Proceso de instalación

#### Requisitos.

* Requiere de microservicio ejecutado [aqui](https://github.com/Javeriana-Especializacion-AES-Ing-Soft/jav-eas-472-cognitive-service-mngr). Valide readme y ejecute las acciones.
* Instalación de docker. [En caso de trabajar con imagenes, de lo contrario iniciar JavEas472GuidesReaderMngrApplication]
    * Cree un network para comunicación en local entre microservicios. `docker network create cognitive-network`
 
#### Instalación.

* Descargue el repositorio `git clone https://github.com/Javeriana-Especializacion-AES-Ing-Soft/jav-eas-472-guides-reader-mngr.git`
* Ejecute `mvn clean install` para descargar dependencias y compilar el proyecto [Genera el .jar para poder hacer uso del dockerfile].
* Si usa docker, ejecute:
    * `docker build -t guides-reader-mngr:0.1 .` Para generar la imagen.
    * `docker run --network cognitive-network -ti --name guides-reader-mngr_01 -p 9096:9096 guides-reader-mngr:0.1` Para ejecutar la imagen en el contenedor. Reemplace {access} y {secret} por las credenciales de IAM en requisitos.
* Si no usa docker, simplemente `Run` sobre JavEas472GuidesReaderMngrApplication.class y modifique el application.properties `http://cognitive-mngr-01:9095` por `http://localhost:9095`

### Recurso:

Para poder realizar el consumo de los recursos, tenga en cuenta lo siguiente:

**PATH BASE:** /V1/Enterprise/guides
* V1 -> Version Uno.
* Enterprise -> Api Empresarial
* Dominio -> guides (Envío de paqueteria)

<table>
    <tr>
        <td>PATH</td>
        <td>DESCRIPCIÓN</td>
        <td>TIPO</td>
        <td>VERBO</td>
        <td>HTTP CODE OK</td>
        <td>HTTP CODES FAILED</td>
    </tr>
    <tr>
        <td>/analyzer</td>
        <td>Recibe una imagen serializada en Base64, la envia el servicio cognitivo y identifica campos requeridos.</td>
        <td>SINCRONA</td>
        <td>POST</td>
        <td>200 - OK -</td>
        <td>500 - INTERNAL_SERVER_ERROR - Error interno (Servicios cognitivos)</td>
    </tr>
    <tr>
            <td>/{uuid}</td>
            <td>Descarga la imagen serializada en Base64.</td>
            <td>SINCRONA</td>
            <td>GET</td>
            <td>200 - OK -</td>
            <td>500 - INTERNAL_SERVER_ERROR - Error interno (Servicios cognitivos)</td>
        </tr>
</table>
