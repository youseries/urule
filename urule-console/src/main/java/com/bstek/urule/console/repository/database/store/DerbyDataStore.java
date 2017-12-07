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
package com.bstek.urule.console.repository.database.store;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.jackrabbit.core.data.DataStoreException;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.DerbyConnectionHelper;

import com.bstek.urule.console.repository.database.DatabaseDataStore;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class DerbyDataStore extends DatabaseDataStore {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionHelper createConnectionHelper(DataSource dataSrc) throws Exception {
        return new DerbyConnectionHelper(dataSrc, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void close() throws DataStoreException {
        super.close();
        try {
            ((DerbyConnectionHelper) conHelper).shutDown(getDriver());
        } catch (SQLException e) {
            throw new DataStoreException(e);
        }
    }
}
