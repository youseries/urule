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

import org.apache.jackrabbit.core.util.db.CheckSchemaOperation;

import com.bstek.urule.console.repository.database.BaseDbFileSystem;

/**
 * @author Jacky.gao
 * @since 2017年12月6日
 */
public class MSSqlFileSystem extends BaseDbFileSystem {
	@Override
	public String databaseType() {
		// TODO Auto-generated method stub
		return "mssql";
	}
	

    /** the variable for the MS SQL table space */
    public static final String TABLE_SPACE_VARIABLE = "${tableSpace}";

    /** the MS SQL table space to use */
    protected String tableSpace = "";

    /**
     * Returns the configured MS SQL table space.
     * @return the configured MS SQL table space.
     */
    public String getTableSpace() {
        return tableSpace;
    }

    /**
     * Sets the MS SQL table space.
     * @param tableSpace the MS SQL table space.
     */
    public void setTableSpace(String tableSpace) {
        if (tableSpace != null && tableSpace.length() > 0) {
            this.tableSpace = "on " + tableSpace.trim();
        } else {
            this.tableSpace = "";
        }
    }

    /**
     * Creates a new <code>MSSqlFileSystem</code> instance.
     */
    public MSSqlFileSystem() {
        // preset some attributes to reasonable defaults
        schema = "mssql";
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CheckSchemaOperation createCheckSchemaOperation() {
        return super.createCheckSchemaOperation().addVariableReplacement(
            CheckSchemaOperation.TABLE_SPACE_VARIABLE, tableSpace);
    }

}
