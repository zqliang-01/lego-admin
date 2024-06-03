cd lego-parent
call mvn clean
call mvn test -Dui.exec=y
call mvn package
pause