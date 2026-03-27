Контекст  
Наше приложение имеет уже достаточное количество слоев, и теперь пора задуматься о частичной
автоматизации его работы и взаимодействии с внешним миром.  
В рамках семинара попробуем создать микросервис на Spring Boot, который автоматически запускает
заранее настроенные
миссии для спутниковых группировок по расписанию.  
Сервис будет взаимодействовать с уже существующим
основным сервисом управления спутниками через REST API

Подготовка  
В первую очередь необходимо добавить возможность обращаться к нашему сервису с внешнего мира. Для
этого добавь зависимости:

- implementation ("org.springframework.boot:spring-boot-starter-web") – включит веб-сервер для
  работы с внешним миром
- implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5" или более новую версию,
  если она совместима с твоим с проектом) – включит свагер для работы с бекэндом через
  браузер http://localhost:8080/swagger-ui.html  
  После этого создай конфигурационный класс src/main/resources/application.yaml с указанием порта на
  котором будет запущен наш бекэнд

```bash
server:
port: 8080
````

Теперь можно создать первый контроллер. Для этого создайте класс SpaceOperationController, который
будет работать с фасадом и дублировать все функции в нем

Чтобы магия спринга заработала, добавь аннотации над контроллером:

- @RestController – добавление контроллера в качестве бина внутрь спринга
- @RequestMapping("/api") -уточнение, как к контроллеру обращаться извне   
  Затем создай endpoints, которые у тебя есть в фасаде, например для executeMission это будет
  выглядеть так:

```java

@PostMapping("/missions") // Уточнение, как к методу обращаться извне
public ResponseEntity<Void> executeMission(@RequestBody MissionRequest request) {
  spaceOperationCenterService.executeMission(request);
  return ResponseEntity.ok()
      .build(); //Возврат успешного ответа (можно туда кидать и обьекты результата)
}
```

После этого запусти main и исправь появившиеся ошибки. Если все запустилось успешно, то можно
переходить в браузер и пробовать отправить запросы к эндпоинтом по
адресу http://localhost:8080/swagger-ui.html

❗Советуем также изучить, что такое свагер и схема OpenAPI

Частая ошибка: разработчики не учитывают, что параметры бывают разных типов. Пример десериализации
разных типов:

```java

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"   // поле в JSON, которое определяет тип
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CommunicationSatelliteParam.class, name = "COMMUNICATION"),
    @JsonSubTypes.Type(value = ImagingSatelliteParam.class, name = "IMAGE")
})
public class SatelliteParam {
```

Теперь, когда настроено общение с внешним миром, перейдем к созданию нового микросервиса

Описание задачи  
Основной сервис (SpaceOperationCenter) работает на порту 8080 и предоставляет следующие эндпоинты (
возможна твоя реализация):

- POST /api/add-satellites – добавление спутников в группировку
- POST /api/missions – выполнение миссии (для всей группировки или одного спутника)
- GET /api/overview – получение текстовой сводки о состоянии системы
- DELETE /api/constellations/{constellationName}/satellites/{satelliteName} – вывод спутника из
  эксплуатации  
  Твоя задача – разработать сервис-планировщик

❗Для реализации планировщика рекомендуется создать копию проекта

Сервис-планировщик (Mission Scheduler) должен выполнять следующие задачи:

- Читать список запланированных миссий из конфигурационного файла application.yml
- При старте приложения регистрировать эти миссии в планировщике задач Spring
- В заданное время (по cron-выражению) отправлять HTTP-запрос к основному сервису на выполнение
  миссии
- Логировать все действия и обрабатывать возможные ошибки (например, если основной сервис недоступен
  или возвращает ошибку)  
  Сервис не должен использовать базу данных – все настройки хранятся в YAML-файле, а запланированные
  задачи живут в памяти

Требования к реализации

- Сервис должен запускаться на порту 8081 (настраивается в application.yml)
- Конфигурация миссий должна быть задана в `application.yml` в виде списка объектов с полями:
    - targetType – тип миссии (CONSTELLATION или SINGLE_SATELLITE)
    - constellationName – имя группировки
    - satelliteName – имя спутника (обязательно только для SINGLE_SATELLITE)
    - cron – cron-выражение для расписания

Пример конфигурации:

```bash
yaml
app:
space-center-service:
missions:
- targetType: CONSTELLATION
constellationName: "GeoStationary"
cron: "0 0 */6 * * *"  # каждые 6 часов
- targetType: SINGLE_SATELLITE
constellationName: "LowOrbit"
satelliteName: "Sat-1"
cron: "0 30 8 * * MON"  # каждый понедельник в 8:30
- targetType: CONSTELLATION
constellationName: "TestConstellation"
cron: "0 */1 * * * *"   # каждую минуту
```

Адрес основного сервиса должен вычитываться из конфигурации, например:

```bas
yaml
app:
space-center-service:
url: "http://localhost:8080/api"
```

Для взаимодействия с основным сервисом используй RestClient (новый HTTP-клиент Spring Boot 3.2+).  
При запуске приложения для каждой миссии из конфигурации должен быть запланирован вызов метода,
который отправляет MissionRequest на эндпоинт /missions`    
Логируй начало выполнения миссии и результат (успех/ошибку)  
Обрабатывай исключения при вызове внешнего сервиса (например, логируй ошибку, но не прерывай работу
планировщика)

**Технические детали и подсказки**  
Скопируй предыдущий проект и оставь нужные зависимости (не убирай spring-boot-starter-web, он будет
нужен для RestClient)  
Поменяй адрес сервера на 8081, чтобы одновременно запускать оба приложения  
Для работы с расписанием включите @EnableScheduling на главном классе  
Внедри TaskScheduler (он создаётся автоматически при наличии @EnableScheduling) и используй метод
schedule(Runnable, CronTrigger)  
Для чтения конфигурации создай record с аннотацией @ConfigurationProperties(prefix = "
app.space-center-service")  
Не забудь добавить @EnableConfigurationProperties в конфигурацию или главный класс  
Создай DTO-классы (record) для запросов к основному сервису: MissionRequest (можно скопировать из
основного сервиса)  
Реализуй клиентский сервис SpaceOperationClient с методами executeMission(MissionRequest request),
используя внедрения бина RestClient

Пример для добавления спутников:

```bash
spaceOperationRestClient.post().
uri("/add-satellites").
contentType(MediaType.APPLICATION_JSON).
body(request).
retrieve().
toBodilessEntity();
````

Бин RestClient сконфигурируй отдельно, задав базовый URL из app.space-center-service.url

```bash
@Bean
public RestClient spaceOperationRestClient() {
  return RestClient.builder()
      .baseUrl(properties.url())
      .build();
}
```

Продумай структуру пакетов согласно доменам и назначениям: clients, configuration, domains,
services, properties, missions и пр.

Рекомендуемые этапы выполнения

- Создание проекта: скопируй предыдущий проект и оставь только то, что нужно
- Конфигурация свойств: создай record для настроек основного сервиса и миссий
- Настройка RestClient: создай конфигурационный класс с бином RestClient
- Реализация клиента: класс SpaceOperationClient с методами для вызова API
- Создание DTO: определи MissionRequest и, если понадобится, другие записи
- Планировщик миссий: сервис ConfiguredMissionScheduler, который при @PostConstruct считывает
  конфигурацию и планирует задачи
- Обработка ошибок: добавь try-catch в (или хендлеры) для корректного выполнения миссий
- Запуск и тестирование: убедись, что при старте запланированные миссии логируются. Протестируй с
  работающим основным сервисом (можно запустить его локально)

Дополнительные задания для саморазвития

- Добавь валидацию конфигурации: проверь, что для SINGLE_SATELLITE указан satelliteName, а для
  CONSTELLATION – нет
- Реализуй возможность динамического добавления новых миссий через отдельный эндпоинт (без
  перезапуска
  приложения)
- Напиши интеграционные тесты контроллеров в основном сервисе
- Попробуй сделать взаимодействие не через RestClient, а через Feign