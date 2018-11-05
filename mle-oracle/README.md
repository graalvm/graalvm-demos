# GraalVM demos: Multi Language Engine JavaScript demo

This repository contains the instructions how to run a demo of [GraalVM](graalvm.org) running a JavaScript module functions as stored procedures in the database.

## Prerequisites
* [Docker](https://docker.com/)

## Preparation

Download the docker container of the Oracle database with the experimental support for the Multi Language Engine:
https://oracle.github.io/oracle-db-mle/releases/0.2.7/docker/

Load the docker image:
```
docker load --input mle-docker-0.2.7.tar.gz
```

Run the container (note you can also configure non-default credentials, but this tutorial doesn't do it):
```
docker run mle-docker-0.2.7
```

Shell into the docker container:
```
docker exec -ti <container_id> bash -li
```

You got to wait a bit for the database to start. It's quite a long wait, at least on my MacBook, so be patient please, on the next runs it is faster.
To verify that the database has started run the `sqlplus`:
```
sqlplus scott/tiger@localhost:1521/ORCLCDB
```

Note: `scott/tiger` are the default login/password. `ORCLCDB` is a site identifier (SID), if you changed them, change the command respectively.

If sqlplus works - the database is ready. Exit sqlplus.

Create a directory, initialize an empty node package, install the validator module from NPM, install the TypeScript types for the validator module.

```
mkdir crazyawesome
cd crazyawesome
echo "{}" > package.json
npm install validator
npm install @types/validator
```

Deploy the validator module to the database, in this command `validator` is the module name:
```
dbjs deploy -u scott -p tiger -c localhost:1521/ORCLCDB validator
```

Start sqlplus again:
```
sqlplus scott/tiger@localhost:1521/ORCLCDB
```

Use the validator module functions as normal stored procedures:
```
select validator.isEmail(‘oleg.selaev@oracle.com') from dual;
select validator.isEmail(‘oleg.selaev') from dual;
```
