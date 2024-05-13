call mvn -f lego-parent\ clean
call mvn -f lego-parent\ test -Dui.exec=y
call mvn -f lego-parent\ package
pause