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

import org.apache.jackrabbit.core.persistence.PMContext;

import com.bstek.urule.console.repository.database.DbPersistenceManager;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class H2PersistenceManager extends DbPersistenceManager {

    /** the lock time out. see*/
    private long lockTimeout = 10000;

    /**
     * Returns the lock timeout.
     * @return the lock timeout
     */
    public String getLockTimeout() {
        return String.valueOf(lockTimeout);
    }

    /**
     * Sets the lock timeout in milliseconds.
     * @param lockTimeout the lock timeout.
     */
    public void setLockTimeout(String lockTimeout) {
        this.lockTimeout = Long.parseLong(lockTimeout);
    }

    /**
     * {@inheritDoc}
     */
   public void init(PMContext context) throws Exception {
        // init default values
        if (getDriver() == null) {
            setDriver("org.h2.Driver");
        }
        if (getUrl() == null) {
            setUrl("jdbc:h2:file:" + context.getHomeDir().getPath() + "/db/itemState");
        }
        if (getDatabaseType() == null) {
            setDatabaseType("h2");
        }
        if (getSchemaObjectPrefix() == null) {
            setSchemaObjectPrefix("");
        }

        super.init(context);
        
        conHelper.exec("SET LOCK_TIMEOUT " + lockTimeout);
    }
}
