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

import org.apache.jackrabbit.core.fs.FileSystemException;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;

import com.bstek.urule.console.repository.RepositoryBuilder;

/**
 * @author Jacky.gao
 * @since 2017年12月6日
 */
public abstract class BaseDbFileSystem extends DbFileSystem {
	@Override
	public void init() throws FileSystemException {
        if (initialized) {
            throw new IllegalStateException("already initialized");
        }
        try {
        	setSchema(databaseType());
            conHelper = createConnectionHelper(RepositoryBuilder.datasource);

            // make sure schemaObjectPrefix consists of legal name characters only
            schemaObjectPrefix = conHelper.prepareDbIdentifier(schemaObjectPrefix);

            // check if schema objects exist and create them if necessary
            if (isSchemaCheckEnabled()) {
                createCheckSchemaOperation().run();
            }

            // build sql statements
            buildSQLStatements();

            // finally verify that there's a file system root entry
            verifyRootExists();

            initialized = true;
        } catch (Exception e) {
            String msg = "failed to initialize file system";
            throw new FileSystemException(msg, e);
        }
	}
	public abstract String databaseType();
}
