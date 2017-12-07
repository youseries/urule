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

import com.bstek.urule.console.repository.database.BaseDbFileSystem;

/**
 * @author Jacky.gao
 * @since 2017年12月6日
 */
public class DB2FileSystem extends BaseDbFileSystem {

	@Override
	public String databaseType() {
		return "db2";
	}

    /**
     * Creates a new <code>DB2FileSystem</code> instance.
     */
    public DB2FileSystem() {
        // preset some attributes to reasonable defaults
        schema = "db2";
        driver = "com.ibm.db2.jcc.DB2Driver";
    }

    //-----------------------------------------< DatabaseFileSystem overrides >
    /**
     * {@inheritDoc}
     * <p>
     * Since DB2 requires parameter markers within the select clause to be
     * explicitly typed using <code>cast(? as type_name)</code> some statements
     * had to be changed accordingly.
     */
    protected void buildSQLStatements() {
        super.buildSQLStatements();

        copyFileSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "select cast(? as varchar(745)), cast(? as varchar(255)), FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_NAME = ? and FSENTRY_DATA is not null";

        copyFilesSQL = "insert into "
                + schemaObjectPrefix + "FSENTRY "
                + "(FSENTRY_PATH, FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH) "
                + "select cast(? as varchar(745)), FSENTRY_NAME, FSENTRY_DATA, "
                + "FSENTRY_LASTMOD, FSENTRY_LENGTH from "
                + schemaObjectPrefix + "FSENTRY where FSENTRY_PATH = ? "
                + "and FSENTRY_DATA is not null";
    }
}
