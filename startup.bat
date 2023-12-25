taskkill /fi "windowtitle eq LegoAdmin"  /f /t

@echo off
set file_path=lego-parent\lego-admin\target\lego-admin.jar
if exist %file_path% (
    cd lego-parent\lego-admin\target
    start "LegoAdmin" java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs\oom_dump.log -Xms500M -Xmx500M lego-admin.jar
    echo LegoAdmin启动成功！
) else (
    echo 未找到启动包lego-admin.jar，请确认是否打包成功！
)
pause