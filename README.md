
# Práctica 2 Sistemas Distribuidos URJC -> ¡Ahora con Bases de Datos!

Esta práctica es una aplicación web que consta de una interfaz web (formularios), una API REST y, además, utiliza una Base de Datos. En nuestro caso, hemos utilizado H2. El objetivo principal de esta práctica es relacionar las entidades entre si, a través de los tipos de correspondencia 1:N y N:M.

Nuestra práctica consiste en una web de gestión de eventos llamada TicketSphere, la cual tiene 3 entidades independientes, evento, localización y usuario. En la interfaz web hemos implementado mediante formularios todas las operaciones CRUD (Create, Read, Update y Delete), y en la API REST hemos implementado todas las operaciones CRUD + PATCH.

En cuanto a las relaciones entre las entidades, tenemos una relación 1:N entre la entidad evento y la entidad usuario. Es decir, 1 usuario va a 1 único evento y a un evento pueden ir 0 o N usuarios. Por otro lado, tenemos una relación N:M entre las entidades evento y localización. Es decir, 1 evento puede estar en 0 o N localizaciones, y en 1 localización pueden haber 0 o N eventos.

## Authors

- [@s-v-x](https://github.com/s-v-x)
- [loreeue](https://github.com/loreeue)