# Category

**Category** — основной сервис для работы с категориями. Он будет управлять всеми операциями добавления, удаления и получения категорий (под получением имеется в виду получение всей категории для определенного пользователя, но не обработка данных). Можно сказать, что это обычные CRUD операции.

Дальше каждый пункт с символом `+` будет обозначать то, что я заметил при проектировании и почему решил добавить новые возможности для сервиса.

## Требования:
1. Для каждого пользователя есть свои данные о категориях.
2. Категория будет представлять собой объект с именем и дочерними элементами.
3. Удаление категории также удаляет все её дочерние категории (даже из базы данных).
4. Имя всех категорий будет уникальным вне зависимости от иерархии (имеется в виду для определенного пользователя).
5. Обработка сценариев, если категория не найдена или нарушена уникальность.
6. Для хранения данных о категориях используется PostgreSQL.

## Дополнительно:
+ Так как категория должна быть связана с определенным пользователем, в объект категории будет добавлено новое поле `Long userId`, которое передается из телеграмма.
+ При удалении категории автоматически удаляются все её дочерние элементы, чтобы избежать "осиротевших" записей в базе данных. Для этого используется каскадное удаление и удаление осиротевших записей (`cascade = ALL`, `orphanRemoval = true`). Это также относится к добавлению дочерних элементов, чтобы родительские категории обновлялись автоматически.

## Основные задачи:
* Добавление категорий (корневых и дочерних).
* Удаление категорий.
* Получение данных о категориях.
* Обработка ошибок (например, если категория не найдена или родительская категория не существует).


# Вариант использования

## 1. Добавление категории:

### 1.1. Добавление корневой категории:
1. В систему передается запрос с нужными данными для создания новой категории.
2. Система проверяет, что категория с таким именем не существует среди всех категорий.
    1. Если категория существует, то возвращается response с подробным описанием и с неудачной попыткой.
3. Система сохраняет категорию в базу данных.
4. Система возвращает response с подтверждением успешного добавления с информацией о категории.

### 1.2. Добавление дочерней категории:
1. В систему передается запрос с нужными данными для создания дочерней категории.
2. Система выполняет проверки:
    1. Убедиться, что категория с именем дочерней не существует среди всех категорий.
        1. В случае существования категории с таким именем дочерней категории возвращается response с подробным описанием.
    2. Убедиться, что родительская категория существует.
        1. В случае несуществования родительской категории возвращается response с подробным описанием.
3. После успешной проверки система создает дочернюю категорию и добавляет её в родительскую. Из-за каскада база данных автоматически обновляет категории.
4. Система возвращает response с подтверждением успешного добавления с информацией о категории.

### 1.3. Удаление категории:
1. В систему передается запрос с нужными данными для удаления категории.
2. Система выполняет проверку, чтобы убедиться, что категория с указанным именем для пользователя существует.
    1. Если в системе нет такой категории, возвращается response с подробным сообщением об ошибке при выполнении операции.
3. Система удаляет категорию и все её дочерние элементы из системы.
4. Система возвращает response с успешной операцией.

### 1.4. Получение категорий:
1. Системе передается объект с ID пользователя.
2. Система ищет в базе данных все категории с указанным ID пользователя.
   1. Если список пусто вызывается исключение (каждое исключение обрабатывается)
3. Система преобразует каждую категорию в `categoryDto`.
4. Система возвращает объект `List<categoryDto>`, где CategoryDto представляет из себя (Long categoryID,Long parentId,String categoryName).

---

## Дополнительно:
+ **CategoryTreeDto** возвращается в виде одного объекта. То есть, когда мы получаем этот объект, то его поле `List<child>` — это и есть родительские категории в базе данных.
+ Для передачи информации об операциях будет создан **CategoryResponse** (`String message`), который подробно информирует клиента.
+ В случае ошибок будет передан объект **ErrorResponse** (`errorMessage: String`).

# Работа системы

В системе реализованы следующие основные методы для работы с категориями:

## 1. Получение дерева категорий
- **Метод:** `GET /category/{userId:\d+}`
- **Описание:**
    - Принимает `userId` из пути запроса.
    - Возвращает дерево категорий для указанного пользователя в виде объекта список `CategoryDto`, (Long categoryID,Long parentId,String categoryName).
- **Пример запроса:**
    ```
    GET /category/123
    ```
### Пример ответа:

```json
[
  {
    "categoryId": 2,
    "parentId": "1",
    "categoryName": "First Category"
  },
  {
    "categoryId": 1,
    "parentId": -1,  // Если нет родителя, то значение -1
    "categoryName": "First Category"
  }
]
```

## 2. Добавление корневой категории
- **Метод:** `POST /category/{userId:\d+}`
- **Описание:**
    - Принимает `userId` из пути запроса и данные для новой категории в теле запроса (`CreateNewCategoryRequest`).
    - Добавляет новую корневую категорию для указанного пользователя.
- **Пример запроса:**
    ```
    POST /api/categories/123
    Content-Type: application/json
    {
        "categoryName": "New Root Category"
    }
    ```
- **Пример ответа:**
    ```json
    {
        "message": "Category 'New Root Category' added successfully."
    }
    ```

## 3. Добавление дочерней категории
- **Метод:** `POST category/{userId:\d+}/{parentCategoryName}`
- **Описание:**
    - Принимает `userId` и имя родительской категории из пути запроса и данные для новой дочерней категории в теле запроса (`CreateNewCategoryRequest`).
    - Добавляет дочернюю категорию в указанную родительскую категорию.
- **Пример запроса:**
    ```
    POST /category/123/Root Category
    Content-Type: application/json
    {
        "categoryName": "New Child Category"
    }
    ```
- **Пример ответа:**
    ```json
    {
        "message": "Category 'New Child Category' added under 'Root Category'."
    }
    ```

## 4. Удаление категории
- **Метод:** `DELETE /category/{userId:\d+}/{categoryName}`
- **Описание:**
    - Принимает `userId` и имя категории из пути запроса.
    - Удаляет категорию с указанным именем и все её дочерние элементы.
- **Пример запроса:**
    ```
    DELETE /category/123/Root Category
    ```
- **Пример ответа:**
    ```json
    {
        "message": "Category 'Root Category' and all its subcategories were deleted successfully."
    }
    ```

---

## Ошибки и обработки
- В случае ошибок, таких как:
    - Категория с таким именем уже существует,
    - Родительская категория не найдена,
    - Категория не существует для указанного пользователя,

  система будет возвращать объект `ErrorResponse` с подробным сообщением об ошибке:

    ```json
    {
        "errorMessage": "Category 'Root Category' already exists."
    }
    ```