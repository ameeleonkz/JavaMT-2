# Реализация RxJava

Данный проект представляет собой кастомную реализацию библиотеки RxJava, демонстрирующую основные концепции реактивного программирования. Проект включает в себя базовые компоненты для создания реактивных потоков, обработки асинхронного выполнения, обработки ошибок и операторы преобразования данных.

## Возможности

- **Observable**: Класс, поддерживающий подписку и отправляющий элементы наблюдателям
- **Observer**: Интерфейс для обработки отправленных элементов, ошибок и сигналов завершения
- **Subscription**: Управляет состоянием подписки и позволяет наблюдателям отписаться
- **Операторы**: Включает различные операторы такие как `map`, `filter`, и `flatMap` для преобразования и фильтрации элементов
- **Планировщики (Schedulers)**: Предоставляет абстракции для управления выполнением потоков, включая I/O и вычислительное планирование
- **Субъекты (Subjects)**: Реализует возможности многоадресной рассылки с `Subject`, `PublishSubject`, и `BehaviorSubject`

## Структура проекта

```
rxjava-implementation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── rxjava/
│   │               ├── Observable.java
│   │               ├── ObservableOnSubscribe.java
│   │               ├── Observer.java
│   │               ├── Subscription.java
│   │               ├── schedulers/
│   │               │   ├── Scheduler.java
│   │               │   ├── IOScheduler.java
│   │               │   └── ComputationScheduler.java
│   │               └── subjects/
│   │                   ├── Subject.java
│   │                   ├── PublishSubject.java
│   │                   └── BehaviorSubject.java
│   └── test/
│       └── java/
│           └── com/
│               └── rxjava/
│                   ├── ObservableTest.java
│                   ├── OperatorsTest.java
│                   ├── SubjectsTest.java
│                   └── SchedulersTest.java
├── pom.xml
└── README.md
```

## Архитектура системы

### Основные компоненты

#### 1. Observable
Центральный класс системы, представляющий поток данных. Основные особенности:
- **Абстрактный класс** с методом `subscribe(Observer<T> observer)`
- **Статический метод `create()`** для создания новых экземпляров
- **Встроенные операторы** (`map`, `filter`, `flatMap`) для трансформации данных
- **Ленивое выполнение** - код выполняется только при подписке

#### 2. Observer
Интерфейс для получения данных из Observable:

```java
public interface Observer<T> {
    void onNext(T item);        // Получение нового элемента
    void onError(Throwable t);  // Обработка ошибки
    void onComplete();          // Сигнал о завершении потока
}
```

#### 3. Subscription
Управляет жизненным циклом подписки:
- `unsubscribe()` - отписка от Observable
- `isUnsubscribed()` - проверка состояния подписки

#### 4. Операторы
Реализованы как методы Observable, возвращающие новый Observable:
- `map` - преобразование каждого элемента
- `filter` - фильтрация элементов по условию
- `flatMap` - преобразование элементов в Observable и их объединение

## Принципы работы Schedulers

Планировщики обеспечивают управление потоками выполнения в реактивных потоках.

### Базовый класс Scheduler

```java
public abstract class Scheduler {
    public abstract Executor getExecutor();
    public void schedule(Runnable action) {
        getExecutor().execute(action);
    }
}
```

### IOScheduler
- **Назначение**: I/O операции (сетевые запросы, работа с файлами, базами данных)
- **Реализация**: Использует `Executors.newCachedThreadPool()`
- **Особенности**:
  - Создает новые потоки по требованию
  - Переиспользует существующие потоки
  - Подходит для блокирующих операций

### ComputationScheduler
- **Назначение**: Вычислительные операции (обработка данных, алгоритмы)
- **Реализация**: Использует `Executors.newFixedThreadPool(CPU_COUNT)`
- **Особенности**:
  - Фиксированный пул потоков (по количеству ядер процессора)
  - Оптимизирован для CPU-интенсивных задач
  - Избегает создания избыточных потоков

### Различия и области применения

| Планировщик | Тип операций | Управление потоками | Применение |
|-------------|--------------|---------------------|------------|
| IOScheduler | I/O, блокирующие | Динамический пул | Сеть, файлы, БД |
| ComputationScheduler | Вычислительные | Фиксированный пул | Алгоритмы, обработка |

## Subjects

Subjects объединяют функциональность Observable и Observer:

### PublishSubject
- **Поведение**: Отправляет данные только активным подписчикам
- **Особенности**: Новые подписчики не получают предыдущие данные

### BehaviorSubject
- **Поведение**: Хранит последнее значение и отправляет его новым подписчикам
- **Особенности**:
  - Может быть создан с начальным значением
  - Методы `getValue()` и `hasValue()` для получения текущего состояния

## Процесс тестирования

### Основные сценарии тестирования

#### 1. Базовая функциональность Observable
- Создание Observable через `create()`
- Подписка и получение данных
- Обработка завершения потока
- Обработка ошибок

#### 2. Тестирование операторов
- **Map**: Преобразование типов данных
- **Filter**: Фильтрация по условиям
- **FlatMap**: Работа с вложенными Observable
- **Цепочки операторов**: Комбинирование нескольких операторов

#### 3. Тестирование Subjects
- **PublishSubject**: Поведение с множественными подписчиками
- **BehaviorSubject**: Сохранение и передача последнего значения
- **BehaviorSubject с начальным значением**

#### 4. Тестирование Schedulers
- Асинхронное выполнение задач
- Корректность работы разных типов планировщиков

### Запуск тестов

```bash
# Компиляция и запуск всех тестов
mvn clean test

# Запуск конкретного тестового класса
mvn test -Dtest=ObservableTest

# Запуск с подробным выводом
mvn test -Dtest=ObservableTest -X
```

## Примеры использования

### 1. Базовое создание Observable

```java
import com.rxjava.Observable;
import com.rxjava.Observer;

// Создание простого Observable
Observable<String> observable = Observable.create(observer -> {
    observer.onNext("Привет");
    observer.onNext("Мир");
    observer.onComplete();
});

// Подписка на Observable
observable.subscribe(new Observer<String>() {
    @Override
    public void onNext(String item) {
        System.out.println("Получено: " + item);
    }
    
    @Override
    public void onError(Throwable t) {
        System.err.println("Ошибка: " + t.getMessage());
    }
    
    @Override
    public void onComplete() {
        System.out.println("Поток завершен");
    }
});
```

### 2. Использование операторов

```java
// Цепочка операторов
Observable<Integer> numbers = Observable.create(observer -> {
    for (int i = 1; i <= 10; i++) {
        observer.onNext(i);
    }
    observer.onComplete();
});

numbers
    .filter(x -> x % 2 == 0)           // Только четные числа
    .map(x -> "Число: " + x)           // Преобразование в строку
    .subscribe(new Observer<String>() {
        @Override
        public void onNext(String item) {
            System.out.println(item);
        }
        
        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }
        
        @Override
        public void onComplete() {
            System.out.println("Обработка завершена");
        }
    });
```

### 3. Работа с FlatMap

```java
// FlatMap для обработки вложенных потоков
Observable<String> words = Observable.create(observer -> {
    observer.onNext("привет");
    observer.onNext("мир");
    observer.onComplete();
});

words
    .flatMap(word -> Observable.create(innerObserver -> {
        // Создаем поток символов для каждого слова
        for (char c : word.toCharArray()) {
            innerObserver.onNext(String.valueOf(c));
        }
        innerObserver.onComplete();
    }))
    .subscribe(new Observer<String>() {
        @Override
        public void onNext(String character) {
            System.out.print(character + " ");
        }
        
        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }
        
        @Override
        public void onComplete() {
            System.out.println("\nРазбор слов завершен");
        }
    });
```

### 4. Использование PublishSubject

```java
import com.rxjava.subjects.PublishSubject;

// Создание PublishSubject
PublishSubject<String> subject = PublishSubject.create();

// Первый подписчик
subject.subscribe(new Observer<String>() {
    @Override
    public void onNext(String item) {
        System.out.println("Подписчик 1: " + item);
    }
    
    @Override
    public void onError(Throwable t) { /* обработка ошибок */ }
    
    @Override
    public void onComplete() {
        System.out.println("Подписчик 1: завершено");
    }
});

// Отправка данных
subject.onNext("Сообщение 1");

// Второй подписчик (не получит предыдущие сообщения)
subject.subscribe(new Observer<String>() {
    @Override
    public void onNext(String item) {
        System.out.println("Подписчик 2: " + item);
    }
    
    @Override
    public void onError(Throwable t) { /* обработка ошибок */ }
    
    @Override
    public void onComplete() {
        System.out.println("Подписчик 2: завершено");
    }
});

subject.onNext("Сообщение 2"); // Получат оба подписчика
subject.onComplete();
```

### 5. Использование BehaviorSubject

```java
import com.rxjava.subjects.BehaviorSubject;

// Создание BehaviorSubject с начальным значением
BehaviorSubject<String> behaviorSubject = 
    BehaviorSubject.createDefault("Начальное значение");

// Первый подписчик получит начальное значение
behaviorSubject.subscribe(new Observer<String>() {
    @Override
    public void onNext(String item) {
        System.out.println("Подписчик 1: " + item);
    }
    
    @Override
    public void onError(Throwable t) { /* обработка ошибок */ }
    
    @Override
    public void onComplete() {
        System.out.println("Подписчик 1: завершено");
    }
});

// Отправка нового значения
behaviorSubject.onNext("Обновленное значение");

// Второй подписчик получит последнее значение
behaviorSubject.subscribe(new Observer<String>() {
    @Override
    public void onNext(String item) {
        System.out.println("Подписчик 2: " + item);
    }
    
    @Override
    public void onError(Throwable t) { /* обработка ошибок */ }
    
    @Override
    public void onComplete() {
        System.out.println("Подписчик 2: завершено");
    }
});

// Проверка текущего значения
System.out.println("Текущее значение: " + behaviorSubject.getValue());
```

### 6. Использование Schedulers

```java
import com.rxjava.schedulers.IOScheduler;
import com.rxjava.schedulers.ComputationScheduler;

// I/O операции
IOScheduler ioScheduler = new IOScheduler();
ioScheduler.schedule(() -> {
    // Симуляция I/O операции
    try {
        Thread.sleep(1000);
        System.out.println("I/O операция выполнена в потоке: " 
            + Thread.currentThread().getName());
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});

// Вычислительные операции
ComputationScheduler computationScheduler = new ComputationScheduler();
computationScheduler.schedule(() -> {
    // Симуляция вычислений
    long result = 0;
    for (int i = 0; i < 1000000; i++) {
        result += i;
    }
    System.out.println("Вычисления завершены в потоке: " 
        + Thread.currentThread().getName() + ", результат: " + result);
});
```

## Сборка и запуск

### Требования
- Java 11 или выше
- Maven 3.6 или выше

### Команды сборки

```bash
# Зайти в папку проекта
cd rxjava-implementation

# Компиляция проекта
mvn clean compile

# Запуск тестов
mvn test

# Создание JAR файла
mvn package

# Полная пересборка с тестами
mvn clean install
```
