taskkill /fi "windowtitle eq ${project.build.finalName}"  /f /t

@echo off
    start "${project.build.finalName}" java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs\oom_dump.log -Xms500M -Xmx500M ${project.build.finalName}.jar
    echo ${project.build.finalName} start success!
pause