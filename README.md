# Веб приложение для введения блога на java

## Обзор
Каждый пользователь, после прохождении регистрации и подтверждении почты 
получает доступ к созданию своих статей и просморту статей других пользователей

## Функциональность

* Регистрация аккаунта 

* Визуальное представление статей пользователей
   * Поиск по названию статьи

* Просмотр статьи
   * Редактирование и удаление статьи
    (только для автора или администратора)
   
* Личный кабинет
   * Возможность изменить пароль или email
   * Доступ к списку пользователей с возможностью их удаления
   (только для администратора)
   
## Техническая составляющая
### Backend

* JDK 11

* Spring framework
  * Boot
  * Security
  * Mail
  * Validation

* Maven
* JDBC
* JPA

* Hibernate
  * HQL (JPQL)

* SQL
  * MySQL
 
* Миграция БД - flyway

### Frontend

* Шаблонизатор - thymeleaf
  * Сайт с шаблонами - bootstrap
  * Сайт с иконками - font awesome



