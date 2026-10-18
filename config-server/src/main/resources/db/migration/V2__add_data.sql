-- Common properties for default profile
insert into properties(application, profile, label, "key", "value")
 values ('application', 'default','master', 'app.rabbitmq.username', '{cipher}fedafc134a1013f6c5b47d69d0c90376ce4275b99c24cd96e267466d071d4094');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.rabbitmq.password', '{cipher}515e13bacf9903a06f290b1d0d3c9d2c14eb70abe9603e65e9724c68b6bfd36e');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.rabbitmq.host', 'localhost');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.rabbitmq.port', '5672');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.rabbitmq.virtual-host', '/');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.eureka.url', '{cipher}c967bae1d4e2e22305eb8f00514bfcaa9883431a3ebd529db86c267525cf7860d3c391b41a2d3efb98e3b758161661e8cf292155fe353df78141269c7ca99839');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.postgres.host', 'localhost');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.postgres.port', '5432');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.postgres.database', 'demo');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.postgres.username', '{cipher}fedafc134a1013f6c5b47d69d0c90376ce4275b99c24cd96e267466d071d4094');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.postgres.password', '{cipher}515e13bacf9903a06f290b1d0d3c9d2c14eb70abe9603e65e9724c68b6bfd36e');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.host', 'localhost');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master','app.mongo.port', '27017');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master','app.mongo.database', 'demo');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.username', '{cipher}fedafc134a1013f6c5b47d69d0c90376ce4275b99c24cd96e267466d071d4094');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.password', '{cipher}515e13bacf9903a06f290b1d0d3c9d2c14eb70abe9603e65e9724c68b6bfd36e');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.authentication-database', 'admin');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.transaction-enabled', 'false');
insert into properties(application, profile, label, "key", "value")
values ('application', 'default','master', 'app.mongo.uri-suffix', '?retryWrites=false&authSource=admin');

-- Common properties for docker profile
insert into properties(application, profile, label, "key", "value")
values ('application', 'docker', 'master', 'app.rabbitmq.host', 'rabbitmq');
insert into properties(application, profile, label, "key", "value")
values ('application', 'docker','master', 'app.postgres.host', 'postgres');
insert into properties(application, profile, label, "key", "value")
values ('application', 'docker','master', 'app.mongo.host', 'mongo');
insert into properties(application, profile, label, "key", "value")
values ('application', 'docker','master', 'app.eureka.url', '{cipher}07b9a1060cf4b826ec8cf1776eeae9043d7034aeb2ccf44f1f90a9929e949737f40830f08a3bf0ae530037e9aea130fc45b88f43c0143a4654588b45a203c64f');

-- Movies api properties for default profile
insert into properties(application, profile, label, "key", "value")
values ('movies-api', 'default','master', 'app.port', '8084');
insert into properties(application, profile, label, "key", "value")
values ('movies-api', 'default','master', 'app.swagger.enabled', 'true');
insert into properties(application, profile, label, "key", "value")
values ('movies-api', 'default','master', 'app.postgres.schema', 'movies_api');

-- Movies api properties for docker profile
insert into properties(application, profile, label, "key", "value")
values ('movies-api', 'docker','master', 'app.swagger.enabled', 'false');

-- Actors api properties for default profile
insert into properties(application, profile, label, "key", "value")
values ('actors-api', 'default','master', 'app.port', '8085');
insert into properties(application, profile, label, "key", "value")
values ('actors-api', 'default','master', 'app.swagger.enabled', 'true');


-- Actors api properties for docker profile
insert into properties(application, profile, label, "key", "value")
values ('actors-api', 'docker','master', 'app.swagger.enabled', 'false');

