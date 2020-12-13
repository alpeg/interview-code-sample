## Описание проекта

Данный проект является реализацией функций математических выражений с помощью ООП. Поддерживаются математические и тригонометрические операции, константы и неизвестные.

Выражения (интерфейс `Expression`) можно упрощать. Поддерживаются упрощения каждого типа выражения, например `1*x`, `0+x` и т.п. Выражения могут быть преобразованы в строки.

Выражение без переменных может быть вычислено, выражение с переменными может быть вычислено при передаче этих переменных как `Map<String, Double>`.

Переменной в выражении может быть назначено любое другое выражение с помощью метода `assign`

Метод `differentiate` находит частичную производную (производную одной переменной).

## Использование

В файле `src/ExpressionsTestExtended.java` показаны примеры использования. Например, в методе `ExpressionsTestExtended#test2()` проверяется, что производная от `(sin(cx^2))` равна `2cx*сos(cx^2)`

```java
// (sin(cx^2))'=2cx*сos(cx^2)
e = new Sin(new Mult(new Pow(new Var("x"), new Num(2)), new Var("c")));
de = e.differentiate("x");
e2 = new Mult(
        new Mult(new Num(2), new Mult(new Var("c"), new Var("x"))),
        new Cos(new Mult(new Pow(new Var("x"), new Num(2)), new Var("c")))
);
this.randomVarEqual(de, e2);
this.testSimplifyEquality(e, de, e2);
```

## Запуск

1. Убедиться что у Вас установлена  Java Runtime 8 (JRE) (`java -version` выдает версию не ниже 8).
2. Скачать `InterviewCodeSample.jar` с вкладки "Releases" проекта Github
3. Запустить `java -jar InterviewCodeSample.jar`

## Разработка

Для разработки использовался NetBeans с JDK 14.

### Сборка с помощью IDE NetBeans

Открыть проект в среде разработки.

### Сборка с помощью ant

Для сборки понадобится JDK.

Если у Вас установлен ant в `%PATH%` / `$PATH` (любая ОС):

```bash
ant run
```

Если вы не знаете, что такое ant, но у Вас установлен NetBeans с поддержкой java (на windows):

```
set PATH=%PATH%;C:\Program Files\NetBeans-12.0\netbeans\extide\ant\bin
ant run
```

