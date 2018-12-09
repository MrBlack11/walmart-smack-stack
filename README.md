# Walmart - SMACK

Aplicação baseada nas ações de um ecommerce de grande porte, utilizando a stack smack.

## Running

O comando abaixo executa o projeto

```
sbt run
```

Então digite o endereço http://localhost:9000 para ver a aplicação web funcionando

## Controllers

There are several demonstration files available in this template.

- `HomeController.java`:

  Shows how to handle simple HTTP requests.

- `AsyncController.java`:

  Shows how to do asynchronous programming when handling a request.

- `CountController.java`:

  Shows how to inject a component into a controller and use the component when
  handling requests.

## Components

- `Module.java`:

  Shows how to use Guice to bind all the components needed by your application.

- `Counter.java`:

  An example of a component that contains state, in this case a simple counter.

- `ApplicationTimer.java`:

  An example of a component that starts when the application starts and stops
  when the application stops.

## Actors