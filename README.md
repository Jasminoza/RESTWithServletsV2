## **Задача**:
##### Необходимо реализовать REST API, которое взаимодействует с файловым хранилищем и предоставляет возможность получать доступ к файлам и истории загрузок. Сущности:

* ###### User 
* ###### Event (File fileEntity)
* ###### File
* ###### User -> List<Event>

##### Требования:

1. ###### Все CRUD операции для каждой из сущностей
2. ###### Придерживаться подхода MVC
3. ###### Для сборки проекта использовать Maven
4. ###### Для взаимодействия с БД - Hibernate
5. ###### Для конфигурирования Hibernate - аннотации
6. ###### Инициализация БД должна быть реализована с помощью flyway
7. ###### Взаимодействие с пользователем необходимо реализовать с помощью Postman (https://www.getpostman.com/)

##### Для запуска приложения необходимо иметь настроенный MySQL сервер, и создать базу данных.
#### JSON link - для тестов Postman: https://www.getpostman.com/collections/0c4aa6519ff871b69609
### Технологии: Java, MySQL, Hibernate, HTTP, Servlets, Maven, Flyway.