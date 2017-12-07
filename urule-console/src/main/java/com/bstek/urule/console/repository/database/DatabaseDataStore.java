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
package com.bstek.urule.console.repository.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.core.data.db.DbDataStore;

import com.bstek.urule.console.repository.RepositoryBuilder;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class DatabaseDataStore extends DbDataStore {
	@Override
	public synchronized void init(String homeDir) throws DataStoreException {
        try {
            initDatabaseType();
            conHelper = createConnectionHelper(RepositoryBuilder.datasource);
            if (isSchemaCheckEnabled()) {
                createCheckSchemaOperation().run();
            }
        } catch (Exception e) {
            throw convert("Can not init data store, driver=" + driver + " url=" + url + " user=" + user +
                    " schemaObjectPrefix=" + schemaObjectPrefix + " tableSQL=" + tableSQL + " createTableSQL=" + createTableSQL, e);
        }
	}
	
	@Override
	protected void initDatabaseType() throws DataStoreException {
		databaseType=RepositoryBuilder.databaseType;
        InputStream in =
            DbDataStore.class.getResourceAsStream(databaseType + ".properties");
        if (in == null) {
        	String msg =
        			"Configuration error: The resource '" + databaseType
        			+ ".properties' could not be found;"
        			+ " Please verify the databaseType property";
        	throw new DataStoreException(msg);
        }
        Properties prop = new Properties();
        try {
            try {
                prop.load(in);
            } finally {
            in.close();
            }
        } catch (IOException e) {
            String msg = "Configuration error: Could not read properties '" + databaseType + ".properties'";
            throw new DataStoreException(msg, e);
        }
        if (driver == null) {
            driver = getProperty(prop, "driver", driver);
        }
        tableSQL = getProperty(prop, "table", tableSQL);
        createTableSQL = getProperty(prop, "createTable", createTableSQL);
        insertTempSQL = getProperty(prop, "insertTemp", insertTempSQL);
        updateDataSQL = getProperty(prop, "updateData", updateDataSQL);
        updateLastModifiedSQL = getProperty(prop, "updateLastModified", updateLastModifiedSQL);
        updateSQL = getProperty(prop, "update", updateSQL);
        deleteSQL = getProperty(prop, "delete", deleteSQL);
        deleteOlderSQL = getProperty(prop, "deleteOlder", deleteOlderSQL);
        selectMetaSQL = getProperty(prop, "selectMeta", selectMetaSQL);
        selectAllSQL = getProperty(prop, "selectAll", selectAllSQL);
        selectDataSQL = getProperty(prop, "selectData", selectDataSQL);
        storeStream = getProperty(prop, "storeStream", storeStream);
        if (!STORE_SIZE_MINUS_ONE.equals(storeStream)
                && !STORE_TEMP_FILE.equals(storeStream)
                && !STORE_SIZE_MAX.equals(storeStream)) {
            String msg = "Unsupported Stream store mechanism: " + storeStream
                    + " supported are: " + STORE_SIZE_MINUS_ONE + ", "
                    + STORE_TEMP_FILE + ", " + STORE_SIZE_MAX;
            throw new DataStoreException(msg);
        }
	}
}
