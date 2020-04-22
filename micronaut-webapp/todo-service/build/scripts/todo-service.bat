@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  todo-service startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and TODO_SERVICE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\todo-service-0.1.jar;%APP_HOME%\lib\micronaut-http-client-1.2.3.jar;%APP_HOME%\lib\api-0.1.jar;%APP_HOME%\lib\micronaut-validation-1.2.3.jar;%APP_HOME%\lib\micronaut-http-server-netty-1.2.3.jar;%APP_HOME%\lib\micronaut-http-server-1.2.3.jar;%APP_HOME%\lib\micronaut-runtime-1.2.3.jar;%APP_HOME%\lib\micronaut-http-netty-1.2.3.jar;%APP_HOME%\lib\micronaut-websocket-1.2.3.jar;%APP_HOME%\lib\micronaut-router-1.2.3.jar;%APP_HOME%\lib\micronaut-http-1.2.3.jar;%APP_HOME%\lib\micronaut-aop-1.2.3.jar;%APP_HOME%\lib\micronaut-inject-1.2.3.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\micronaut-buffer-netty-1.2.3.jar;%APP_HOME%\lib\micronaut-core-1.2.3.jar;%APP_HOME%\lib\slf4j-api-1.7.26.jar;%APP_HOME%\lib\rxjava-2.2.10.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.41.Final.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\snakeyaml-1.24.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.9.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.9.jar;%APP_HOME%\lib\jackson-databind-2.9.9.3.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\netty-codec-http-4.1.41.Final.jar;%APP_HOME%\lib\netty-handler-4.1.41.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.41.Final.jar;%APP_HOME%\lib\netty-codec-4.1.41.Final.jar;%APP_HOME%\lib\netty-transport-4.1.41.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.41.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.41.Final.jar;%APP_HOME%\lib\netty-common-4.1.41.Final.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\jackson-annotations-2.9.9.jar;%APP_HOME%\lib\jackson-core-2.9.9.jar

@rem Execute todo-service
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %TODO_SERVICE_OPTS%  -classpath "%CLASSPATH%" org.graalvm.demos.micronaut.service.todo.Application %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable TODO_SERVICE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%TODO_SERVICE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
