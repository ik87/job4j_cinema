# [EN] Choosing are places in the cinema
![Heroku](https://cinema-job4j.herokuapp.com/?app=cinema-job4j&style=flat&svg=1)
pet-project "Choosing are places in the cinema"
* demo:  https://cinema-job4j.herokuapp.com/
 
##### I make completely myself: 
   
Frontend:```html, js, css, bootstrap, 100% phone - pc adaptive interface```   

Backend ```Servlet API, Postgresql, etc```  

##### There were used long poling with async servlet's, pluses from regular short poling are:
- short latency, clients view changes lightning fast
- server is low loads, because threads are not busy, hold all clients occur in the special thread 
- easy implementation

###### Example. How it works in two different windows:
[![Watch the video](https://j.gifs.com/3Qq6rn.gif)](https://youtu.be/6LHIaFafKFA)

###### Also with use JS and SVG I made flexible API for drawing cinema places
[![Watch the video](https://j.gifs.com/gZqpDl.gif)](https://youtu.be/KTFfzQAyN1c)

###### Adaptive interface
[![Watch the video](https://j.gifs.com/wVN0xm.gif)](https://youtu.be/3LP5SCvxmdY)
      
### Used tool:

* [Bootstrap](getbootstrap.com) - for create UI
* [J2EE](https://www.oracle.com/technetwork/java/javaee/) - JPA, Servlet, Listener
* [Apache Tomcat](http://tomcat.apache.org/) - container Servlets 
* [POSTGRESQL](https://www.postgresql.org/) - date base
* [liquibase](https://liquibase.org/) - Track, version, and deploy database changes
* [GSON](https://github.com/google/gson) - Serialization/Deserialization json objects 

### Helper recourses:

Get a list of all HttpSession objects in a web application
- https://stackoverflow.com/a/3771134/5984851

Detect browser or tab closing
- https://stackoverflow.com/questions/3888902/detect-browser-or-tab-closing
- https://stackoverflow.com/a/20853978/5984851

Reloading a page when using browser back button
- https://stackoverflow.com/questions/43043113/how-to-force-reloading-a-page-when-using-browser-back-button

Load Java HttpSession from JSESSIONID
- https://stackoverflow.com/a/3092495/5984851

GSON, json Serialization/Deserialization
- https://habr.com/ru/post/253266/

Bootstrap UI credit card
- https://bootstrapious.com/p/bootstrap-credit-card-form

Long polling
- https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/async-servlet/async-servlets.html
- https://gist.github.com/ngugijames/c3eaf929f3512def986b
- https://github.com/matheusgg/ocejwcd/blob/master/Cap8/AsyncProject/src/asyncListener/

# [RU]  Выбор места в кинотеатре
![Heroku](https://cinema-job4j.herokuapp.com/?app=cinema-job4j&style=flat&svg=1)
пет-проект "Выбор места в кинотеатре"
 * демонстрация:  https://cinema-job4j.herokuapp.com/ 

##### Самостоятельно было сделано
Frontend ```html, js, css, bootstrap, 100% phone - pc adaptive interface```

Backend ```Servlet API, Postgresql, etc```

##### Использовался long poling с асинхронным сервлетом, плюсы от традиционного способа:
- низкая задержка, клиенты сразу же видетя изменения 
- низкая загрузка сервера, т.к потоки свободны, а удерживание происходит в отдельном потоке
- простота реализации

###### Пример работы. В двух раздельных окнах:
[![Watch the video](https://j.gifs.com/3Qq6rn.gif)](https://youtu.be/6LHIaFafKFA)

###### Так же с помощью JS и SVG была реализованна API для гибкой отрисовки мест в кинотеатре (SVG)
[![Watch the video](https://j.gifs.com/gZqpDl.gif)](https://youtu.be/KTFfzQAyN1c)


###### Адаптивный дизайн
[![Watch the video](https://j.gifs.com/wVN0xm.gif)](https://youtu.be/3LP5SCvxmdY)
     
### Использованные средства:

* [Bootstrap](getbootstrap.com) - для реализации UI
* [J2EE](https://www.oracle.com/technetwork/java/javaee/) - JPA, Servlet, Listener
* [Apache Tomcat](http://tomcat.apache.org/) - контейнер сервлетов
* [POSTGRESQL](https://www.postgresql.org/) - база данных
* [liquibase](https://liquibase.org/) - отслеживание и изменение БД
* [GSON](https://github.com/google/gson) - сериализация и десериализация Java объектов в JSON

### Вспомогательные рессурсы:

Получение списка всех объектов HttpSession в веб приложении
- https://stackoverflow.com/a/3771134/5984851

Определение что браузер закрыт
- https://stackoverflow.com/questions/3888902/detect-browser-or-tab-closing
- https://stackoverflow.com/a/20853978/5984851

Перегрузка страницы когда пользователь нажимает кнопку "назад"
- https://stackoverflow.com/questions/43043113/how-to-force-reloading-a-page-when-using-browser-back-button

Загрузка Java HttpSession из JSESSIONID
- https://stackoverflow.com/a/3092495/5984851

GSON, json Serialization/Deserialization
- https://habr.com/ru/post/253266/

Bootstrap дизайн кредитных карт
- https://bootstrapious.com/p/bootstrap-credit-card-form

Инструкции по Long polling
- https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/async-servlet/async-servlets.html
- https://gist.github.com/ngugijames/c3eaf929f3512def986b
- https://github.com/matheusgg/ocejwcd/blob/master/Cap8/AsyncProject/src/asyncListener/