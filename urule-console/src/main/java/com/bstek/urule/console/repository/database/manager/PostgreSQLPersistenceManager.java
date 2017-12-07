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
package com.bstek.urule.console.repository.database.manager;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.pool.DbNameIndex;
import org.apache.jackrabbit.core.persistence.pool.PostgreSQLNameIndex;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.PostgreSQLConnectionHelper;

import com.bstek.urule.console.repository.database.DbPersistenceManager;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class PostgreSQLPersistenceManager extends DbPersistenceManager {

    /**
     * {@inheritDoc}
     */
    public void init(PMContext context) throws Exception {
        // init default values
        if (getDriver() == null) {
            setDriver("org.postgresql.Driver");
        }
        if (getDatabaseType() == null) {
            setDatabaseType("postgresql");
        }
        super.init(context);
    }

    /**
     * Returns a new instance of a DbNameIndex.
     * @return a new instance of a DbNameIndex.
     * @throws java.sql.SQLException if an SQL error occurs.
     */
    protected DbNameIndex createDbNameIndex() throws SQLException {
        return new PostgreSQLNameIndex(conHelper, schemaObjectPrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionHelper createConnectionHelper(DataSource dataSrc) throws Exception {
    	return new PostgreSQLConnectionHelper(dataSrc, blockOnConnectionLoss);
    }

    /**
     * returns the storage model
     * @return the storage model
     */
    public int getStorageModel() {
        return SM_LONGLONG_KEYS;
    }

}
