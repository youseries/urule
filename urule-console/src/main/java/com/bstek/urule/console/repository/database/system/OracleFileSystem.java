/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.console.repository.database.system;

import javax.sql.DataSource;

import org.apache.jackrabbit.core.util.db.CheckSchemaOperation;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.OracleConnectionHelper;

import com.bstek.urule.console.repository.database.BaseDbFileSystem;

/**
 * @author Jacky.gao
 * @since 2017年12月6日
 */
public class OracleFileSystem extends BaseDbFileSystem{
    @Override
    public String databaseType() {
    	return "oracle";
    }
	
	 /**
     * The default tablespace clause used when {@link #tablespace} or {@link #indexTablespace}
     * are not specified.
     */
    protected static final String DEFAULT_TABLESPACE_CLAUSE = "";
    
    /**
     * Name of the replacement variable in the DDL for {@link #tablespace}.
     */
    protected static final String TABLESPACE_VARIABLE = "${tablespace}";
    
    /**
     * Name of the replacement variable in the DDL for {@link #indexTablespace}.
     */
    protected static final String INDEX_TABLESPACE_VARIABLE = "${indexTablespace}";

    /** The Oracle tablespace to use for tables */
    protected String tablespace;

    /** The Oracle tablespace to use for indexes */
    protected String indexTablespace;
    
    /**
     * Creates a new <code>OracleFileSystem</code> instance.
     */
    public OracleFileSystem() {
        // preset some attributes to reasonable defaults
        schema = "oracle";
        driver = "oracle.jdbc.OracleDriver";
        schemaObjectPrefix = "";
        tablespace = DEFAULT_TABLESPACE_CLAUSE;
        indexTablespace = DEFAULT_TABLESPACE_CLAUSE;
        initialized = false;
    }

    /**
     * Returns the configured Oracle tablespace for tables.
     * @return the configured Oracle tablespace for tables.
     */
    public String getTablespace() {
        return tablespace;
    }

    /**
     * Sets the Oracle tablespace for tables.
     * @param tablespaceName the Oracle tablespace for tables.
     */
    public void setTablespace(String tablespaceName) {
        this.tablespace = this.buildTablespaceClause(tablespaceName);
    }
    
    /**
     * Returns the configured Oracle tablespace for indexes.
     * @return the configured Oracle tablespace for indexes.
     */
    public String getIndexTablespace() {
        return indexTablespace;
    }
    
    /**
     * Sets the Oracle tablespace for indexes.
     * @param tablespaceName the Oracle tablespace for indexes.
     */
    public void setIndexTablespace(String tablespaceName) {
        this.indexTablespace = this.buildTablespaceClause(tablespaceName);
    }
    
    /**
     * Constructs the <code>tablespace &lt;tbs name&gt;</code> clause from
     * the supplied tablespace name. If the name is empty, {@link #DEFAULT_TABLESPACE_CLAUSE}
     * is returned instead.
     * 
     * @param tablespaceName A tablespace name
     * @return A tablespace clause using the supplied name or
     * <code>{@value #DEFAULT_TABLESPACE_CLAUSE}</code> if the name is empty
     */
    private String buildTablespaceClause(String tablespaceName) {
        if (tablespaceName == null || tablespaceName.trim().length() == 0) {
            return DEFAULT_TABLESPACE_CLAUSE;
        } else {
            return "tablespace " + tablespaceName.trim();
        }
    }

    //-----------------------------------------< DatabaseFileSystem overrides >

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionHelper createConnectionHelper(DataSource dataSrc) throws Exception {
        OracleConnectionHelper helper = new OracleConnectionHelper(dataSrc, false);
        helper.init();
        return helper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CheckSchemaOperation createCheckSchemaOperation() {
        if (DEFAULT_TABLESPACE_CLAUSE.equals(indexTablespace) && !DEFAULT_TABLESPACE_CLAUSE.equals(tablespace)) {
            // tablespace was set but not indexTablespace : use the same for both
            indexTablespace = tablespace;
        }
        return super.createCheckSchemaOperation()
            .addVariableReplacement(TABLESPACE_VARIABLE, tablespace)
            .addVariableReplacement(INDEX_TABLESPACE_VARIABLE, indexTablespace);
    }

    //-----------------------------------------< DatabaseFileSystem overrides >
    
    /**
     * Builds the SQL statements
     * <p>
     * Since Oracle treats emtpy strings and BLOBs as null values the SQL
     * statements had to be adapated accordingly. The following changes were
     * necessary:
     * <ul>
     * <li>The distinction between file and folder entries is based on
     * FSENTRY_LENGTH being null/not null rather than FSENTRY_DATA being
     * null/not null because FSENTRY_DATA of a 0-length (i.e. empty) file is
     * null in Oracle.</li>
     * <li>Folder entries: Since the root folder has an empty name (which would
     * be null in Oracle), an empty name is automatically converted and treated
     * as " ".</li>
     * </ul>
     */
    protected void buildSQLStatements() {
        insertFileSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "values (?, ?, ?, ?, ?)";

        insertFolderSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "values (?, nvl(?, ' '), ?, null)";

        updateDataSQL = "update "
                + schemaObjectPrefix + "FSENTRY "
                + "set FSENTRY_DATA = ?, FSENTRY_LASTMOD = ?, FSENTRY_LENGTH = ? "
                + "where FSENTRY_PATH = ? and FSENTRY_NAME = ? "
                + "and FSENTRY_LENGTH is not null";

        updateLastModifiedSQL = "update "
                + schemaObjectPrefix + "FSENTRY set FSENTRY_LASTMOD = ? "
                + "where FSENTRY_PATH = ? and FSENTRY_NAME = ? "
                + "and FSENTRY_LENGTH is not null";

        selectExistSQL = "select 1 from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = nvl(?, ' ')";

        selectFileExistSQL = "select 1 from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_LENGTH is not null";

        selectFolderExistSQL = "select 1 from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = nvl(?, ' ') and FSENTRY_LENGTH is null";

        selectFileNamesSQL = "select FSENTRY_NAME from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_LENGTH is not null";

        selectFolderNamesSQL = "select FSENTRY_NAME from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME != ' ' "
                + "and FSENTRY_LENGTH is null";

        selectFileAndFolderNamesSQL = "select FSENTRY_NAME from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME != ' '";

        selectChildCountSQL = "select count(FSENTRY_NAME) from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ?  "
                + "and FSENTRY_NAME != ' '";

        selectDataSQL = "select nvl(FSENTRY_DATA, empty_blob()) from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_LENGTH is not null";

        selectLastModifiedSQL = "select FSENTRY_LASTMOD from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = nvl(?, ' ')";

        selectLengthSQL = "select nvl(FSENTRY_LENGTH, 0) from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_LENGTH is not null";

        deleteFileSQL = "delete from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_LENGTH is not null";

        deleteFolderSQL = "delete from "
                + schemaObjectPrefix + "FSENTRY where "
                + "(FSENTRY_PATH = ? and FSENTRY_NAME = nvl(?, ' ') and FSENTRY_LENGTH is null) "
                + "or (FSENTRY_PATH = ?) "
                + "or (FSENTRY_PATH like ?) ";

        copyFileSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "select ?, ?, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_LENGTH is not null";

        copyFilesSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "select ?, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_LENGTH is not null";
    }
}
