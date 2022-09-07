## **Задача**:
##### Необходимо реализовать REST API, которое взаимодействует с файловым хранилищем и предоставляет возможность получать доступ к файлам и истории загрузок. Сущности:

* ###### User 
* ###### Event (File file)
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
8. ###### Репозиторий должен иметь бейдж сборки travis(https://travis-ci.com/)
9. ###### Рабочее приложение должно быть развернуто на heroku.com

##### Для запуска приложения необходимо иметь настроенный MySQL сервер, и создать базу данных.
### Технологии: Java, MySQL, Hibernate, HTTP, Servlets, Maven, Flyway.