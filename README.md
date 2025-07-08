# Agente Basico con Embabel (Basic Embabel Agent)

## Español

Ejemplo de un agente básico que utiliza Embabel para interactuar con un LLM (Large Language Model) y realizar tareas de resumen de sitios web.

Este project usa
- Java 24
- Spring Boot 3.5.3
- Embabel 0.1.0-SNAPSHOT
- JSoup 1.21.1

### Ejecutar el proyecto

Para ejecutar el proyecto, asegúrate de tener Java 24 instalado y luego ejecuta el siguiente comando en la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

### Interactuar con el agente

Para interactuar con el agente, una vez que el proyecto esté en ejecución y el prompt de Spring Shell este esperando comandos hay que ejecutar lo siguiente:

```
x "summarize the content of the following page https://en.wikipedia.org/wiki/Alan_Turing" -r -p
```

o incluir varias urls en la peticion al agente:

```
x "summarize the content of the following pages https://en.wikipedia.org/wiki/Alan_Turing, https://en.wikipedia.org/wiki/Albert_Einstein, https://en.wikipedia.org/wiki/Nikola_Tesla" -r -p
```

Las opciones `-r` y `-p` son opcionales y su proposito es 
- `-r`: Indica que se desea imprimir la respuesta del LLMs 
- `-p`: Indica que se desea imprimir el prompt que se envia al LLM

Puedes ver que otras opciones hay disponibles ejecutando el comando `help x` en el prompt de Spring Shell.


## English

Example of a basic agent that uses Embabel to interact with a Large Language Model (LLM) and perform web page summarization tasks.

This project uses
- Java 24
- Spring Boot 3.5.3
- Embabel 0.1.0-SNAPSHOT
- JSoup 1.21.1

### Running the project

To run the project, make sure you have Java 24 installed and then execute the following command in the root of the project:

```bash
./mvnw spring-boot:run
```

### Interacting with the agent

To interact with the agent, once the project is running and the Spring Shell prompt is waiting for commands, execute the following:

```
x "summarize the content of the following page https://en.wikipedia.org/wiki/Alan_Turing" -r -p
```

or include multiple URLs in the agent request:

```
x "summarize the content of the following pages https://en.wikipedia.org/wiki/Alan_Turing, https://en.wikipedia.org/wiki/Albert_Einstein, https://en.wikipedia.org/wiki/Nikola_Tesla" -r -p
```

The options `-r` and `-p` are optional and their purpose is
- `-r`: Indicates that the response from the LLM should be printed
- `-p`: Indicates that the prompt sent to the LLM should be printed

You can see what other options are available by executing the command `help x` in the Spring Shell prompt.




