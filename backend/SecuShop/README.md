# SzgbiztBackend

# How to set up the application

### Parser

The application included the parser by default, but to be able to run you need **GNU C++ Compiler** _(G++)_.

_Comment: If it does not run try to rebuild the parser, this can happen if it was built on a different type of processor
than the one you are currently using._

### Databases

1) Create two databases, one is **security**, the other is **shop**. The **security** is responsible for the
   application's security-related processes while the **shop** is responsible for
   the shopping processes.

2) Modify the attributes required for database connections properties in the **application.properties** file, if it is
   necessary.

3) Run the **create_table.sql** file's scripts for each of the databases to create the required tables to run the
   application.

If everything is done then let's try, it should start successfully. :)

# API

An API descriptor is available for the application, which you can access at **<server_domain>/swagger-ui/index.html** address when the application **is running**.