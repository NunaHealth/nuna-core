package com.nuna.trustdb.tools.dbloader

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.nuna.trustdb.core.util.Params

@Params(allowInRootPackage = true)
case class DbLoaderConfig(
    // Table (directory) names to load.
    tableNames: Seq[String],
    // TODO: deprecate these and switch to classpath scanning.
    // DEPRECATED: Full name of case classes representing the tables.
    // Note: The classes have to be on classpath.
    caseClassNames: Seq[String],
    // Jdbc url of target database.
    @JsonProperty(access = Access.WRITE_ONLY)
    jdbcUrl: String,
    // Jdbc user name.
    // TODO: rename to jdbcUser
    username: Option[String],
    // Jdbc password.
    // TODO: rename to jdbcPassword
    @JsonProperty(access = Access.WRITE_ONLY)
    password: Option[String],
    // Timestamp. Current time - now() - will be used if missing.
    timestamp: String,
    // Name of the schema to be created. For example: "data_${timestamp}" (the timestamp variable will get replaced).
    schemaName: String,
    // If not null, insert an record w/ $schemaName to update_history table
    updateHistorySchemaName: Option[String],
    // File name of sql script to run before all the tables are imported.
    beforeLoadScript: String,
    // File name of sql script to run after all the tables are imported.
    afterLoadScript: String,
    // Jdbc options.
    jdbcOptions: Map[String, String],
    // Spark write options.
    sparkWriteOptions: Map[String, String],
    // Note: This disable table creation by our reflection based code. Spark may still create the table. Add your own
    //       CREATE TABLE statement into beforeLoadScript if you want full manual control over the table creation.
    disableTableCreation: Boolean,
    // This primary key and unique key constrains creation. JdbcReflection creates primary key on @Id annotated columns.
    // The unique key constrains are generated per column if @Column(unique=true) is present.
    disableConstrainCreation: Boolean
)
