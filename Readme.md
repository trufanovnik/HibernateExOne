# Приложение для управления покупками (Hibernate + PostgreSQL)

Это консольное приложение, разработанное с использованием Hibernate и PostgreSQL, позволяет управлять информацией о покупателях, товарах и покупках. Приложение поддерживает основные команды для работы с данными.

---

## Требования

- Java 8 или выше
- PostgreSQL
- Hibernate
- Maven (для сборки проекта)

---

## Установка и запуск

1. **Установите PostgreSQL:**
    - Создайте базу данных с именем `your_database_name`.
    - Обновите настройки подключения в файле `hibernate.cfg.xml`:
      ```xml
      <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/your_database_name</property>
      <property name="hibernate.connection.username">your_username</property>
      <property name="hibernate.connection.password">your_password</property>
      ```

2. **Создайте таблицы:**
    - Выполните SQL-скрипт для создания таблиц (см. раздел **SQL-скрипт** ниже).

3. **Соберите проект:**
    - Используйте Maven для сборки проекта:
      ```bash
      mvn clean install
      ```

4. **Запустите приложение:**
    - Запустите приложение с помощью команды:
      ```bash
      java -jar target/your-app-name.jar
      ```

---

## SQL-скрипт для создания таблиц

```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE purchases (
    id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price_at_purchase DECIMAL(10, 2) NOT NULL
);