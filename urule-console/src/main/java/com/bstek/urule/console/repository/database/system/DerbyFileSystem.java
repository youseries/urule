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

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.jackrabbit.core.fs.FileSystemException;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.DerbyConnectionHelper;

import com.bstek.urule.console.repository.database.BaseDbFileSystem;

/**
 * @author Jacky.gao
 * @since 2017年12月6日
 */
public class DerbyFileSystem extends BaseDbFileSystem {

	@Override
	public String databaseType() {
		return "derby";
	}

	

    /**
     * Flag indicating whether this derby database should be shutdown on close.
     */
    protected boolean shutdownOnClose;

    /**
     * Creates a new <code>DerbyFileSystem</code> instance.
     */
    public DerbyFileSystem() {
        // preset some attributes to reasonable defaults
        schema = "derby";
        driver = "org.apache.derby.jdbc.EmbeddedDriver";
        shutdownOnClose = true;
        initialized = false;
    }

    //----------------------------------------------------< setters & getters >

    public boolean getShutdownOnClose() {
        return shutdownOnClose;
    }

    public void setShutdownOnClose(boolean shutdownOnClose) {
        this.shutdownOnClose = shutdownOnClose;
    }

    //-----------------------------------------------< DbFileSystem overrides >

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
    public void close() throws FileSystemException {
        super.close();
        if (shutdownOnClose) {
            try {
                ((DerbyConnectionHelper) conHelper).shutDown(driver);
            } catch (SQLException e) {
                throw new FileSystemException("failed to shutdown Derby", e);
            }
        }
    }
}
