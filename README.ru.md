# Выбор места в кинотеатре
![Heroku](https://heroku-badge.herokuapp.com/?app=cinema-job4j)

пет-проект "Выбор места в кинотеатре"
 * демонстрация:  https://cinema-job4j.herokuapp.com/ 

*Прочитать это на других языках: [English](README.md), [Русском](README.ru.md)*
##### Самостоятельно было сделано
Frontend ```html, js, css, bootstrap, 100% phone - pc adaptive interface```

Backend ```Servlet API, Postgresql, etc```

##### Использовался long poling с асинхронным сервлетом, плюсы от традиционного способа:
- низкая задержка, клиенты сразу же видят изменения 
- низкая загрузка сервера, т.к потоки свободны, а удерживание происходит в отдельном потоке
- простота реализации

###### Пример работы. В двух раздельных окнах:
[![Watch the video](https://j.gifs.com/3Qq6rn.gif)](https://youtu.be/6LHIaFafKFA)

##### Создан JS\SVG API для гибкой отрисовки мест в кинотеатре (SVG)
###### Пример работы
[![Watch the video](https://j.gifs.com/gZqpDl.gif)](https://youtu.be/KTFfzQAyN1c)


##### Адаптивный дизайн
###### PC:
[![Watch the video](https://j.gifs.com/YWJlNp.gif)](https://youtu.be/3LP5SCvxmdY)
###### Mobile:
[![Watch the video](https://j.gifs.com/VAD0rW.gif)](https://youtu.be/3LP5SCvxmdY)
     
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