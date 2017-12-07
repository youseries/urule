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
import org.apache.jackrabbit.core.persistence.pool.BundleDbPersistenceManager;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.DerbyConnectionHelper;

import com.bstek.urule.console.repository.database.DbPersistenceManager;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class DerbyPersistenceManager extends DbPersistenceManager {

    /** name of the embedded driver */
    public static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /** @see #setDerbyStorageInitialPages(String) */
    private int derbyStorageInitialPages = 16;

    /** @see #setDerbyStorageMinimumRecordSize(String) */
    private int derbyStorageMinimumRecordSize = 512;

    /** @see #setDerbyStoragePageCacheSize(String) */
    private int derbyStoragePageCacheSize = 1024;

    /** @see #setDerbyStoragePageReservedSpace(String) */
    private int derbyStoragePageReservedSpace = 20;

    /** @see #setDerbyStoragePageSize(String) */
    private int derbyStoragePageSize = 16384;

    /**
     * @see #setDerbyStorageInitialPages
     * @return the initial pages property
     */
    public String getDerbyStorageInitialPages() {
        return String.valueOf(derbyStorageInitialPages);
    }

    /**
     * The on-disk size of a Derby table grows by one page at a time until eight
     * pages of user data (or nine pages of total disk use, one is used for
     * overhead) have been allocated. Then it will grow by eight pages at a time
     * if possible.
     * <p>
     * A Derby table or index can be created with a number of pages already
     * pre-allocated. To do so, specify the property prior to the CREATE TABLE
     * or CREATE INDEX statement.
     * <p>
     * Define the number of user pages the table or index is to be created with.
     * The purpose of this property is to preallocate a table or index of
     * reasonable size if the user expects that a large amount of data will be
     * inserted into the table or index. A table or index that has the
     * pre-allocated pages will enjoy a small performance improvement over a
     * table or index that has no pre-allocated pages when the data are loaded.
     * <p>
     * The total desired size of the table or index should be
     * <p>
     * <strong>(1+derby.storage.initialPages) * derby.storage.pageSize bytes.</strong>
     * <p>
     * When you create a table or an index after setting this property, Derby
     * attempts to preallocate the requested number of user pages. However, the
     * operations do not fail even if they are unable to preallocate the
     * requested number of pages, as long as they allocate at least one page.
     * <p>
     * Default is <code>16</code>
     *
     * @param derbyStorageInitialPages the number of initial pages
     */
    public void setDerbyStorageInitialPages(String derbyStorageInitialPages) {
        this.derbyStorageInitialPages =
                Integer.decode(derbyStorageInitialPages).intValue();
    }

    /**
     * @see #setDerbyStorageMinimumRecordSize
     * @return the minimum record size
     */
    public String getDerbyStorageMinimumRecordSize() {
        return String.valueOf(derbyStorageMinimumRecordSize);
    }

    /**
     * Indicates the minimum user row size in bytes for on-disk database pages
     * for tables when you are creating a table. This property ensures that
     * there is enough room for a row to grow on a page when updated without
     * having to overflow. This is generally most useful for VARCHAR and
     * VARCHAR FOR BIT DATA data types and for tables that are updated a lot,
     * in which the rows start small and grow due to updates. Reserving the
     * space at the time of insertion minimizes row overflow due to updates,
     * but it can result in wasted space. Set the property prior to issuing the
     * CREATE TABLE statement.
     * <p>
     * Default is <code>256</code>
     *
     * @param derbyStorageMinimumRecordSize the minimum record size
     */
    public void setDerbyStorageMinimumRecordSize(String derbyStorageMinimumRecordSize) {
        this.derbyStorageMinimumRecordSize =
                Integer.decode(derbyStorageMinimumRecordSize).intValue();
    }

    /**
     * @see #setDerbyStoragePageCacheSize
     * @return the page cache size
     */
    public String getDerbyStoragePageCacheSize() {
        return String.valueOf(derbyStoragePageCacheSize);
    }

    /**
     * Defines the size, in number of pages, of the database's data page cache
     * (data pages kept in memory). The actual amount of memory the page cache
     * will use depends on the following:
     * <ul>
     * <li> the size of the cache (configured with {@link #setDerbyStoragePageCacheSize})
     * <li> the size of the pages (configured with {@link #setDerbyStoragePageSize})
     * <li> overhead (varies with JVMs)
     * </ul>
     * When increasing the size of the page cache, you typically have to allow
     * more memory for the Java heap when starting the embedding application
     * (taking into consideration, of course, the memory needs of the embedding
     * application as well). For example, using the default page size of 4K, a
     * page cache size of 2000 pages will require at least 8 MB of memory (and
     * probably more, given the overhead).
     * <p>
     * The minimum value is 40 pages. If you specify a lower value, Derby uses
     * the default value.
     * <p>
     * Default is <code>1024</code> (which gives about 16mb memory usage given
     * the default of 16384 as page size).
     *
     * @param derbyStoragePageCacheSize the page cache size
     */
    public void setDerbyStoragePageCacheSize(String derbyStoragePageCacheSize) {
        this.derbyStoragePageCacheSize =
                Integer.decode(derbyStoragePageCacheSize).intValue();
    }


    /**
     * @see #setDerbyStoragePageReservedSpace
     * @return the page reserved space
     */
    public String getDerbyStoragePageReservedSpace() {
        return String.valueOf(derbyStoragePageReservedSpace);
    }

    /**
     * Defines the percentage of space reserved for updates on an on-disk
     * database page for tables only (not indexes); indicates the percentage of
     * space to keep free on a page when inserting. Leaving reserved space on a
     * page can minimize row overflow (and the associated performance hit)
     * during updates. Once a page has been filled up to the reserved-space
     * threshold, no new rows are allowed on the page. This reserved space is
     * used only for rows that increase in size when updated, not for new
     * inserts. Set this property prior to issuing the CREATE TABLE statement.
     * <p>
     * Regardless of the value of derby.storage.pageReservedSpace, an empty page
     * always accepts at least one row.
     * <p>
     * Default is <code>20%</code>
     *
     * @param derbyStoragePageReservedSpace the page reserved space
     */
    public void setDerbyStoragePageReservedSpace(String derbyStoragePageReservedSpace) {
        this.derbyStoragePageReservedSpace =
                Integer.decode(derbyStoragePageReservedSpace).intValue();
    }

    /**
     * @see #setDerbyStoragePageSize
     * @return the page size
     */
    public String getDerbyStoragePageSize() {
        return String.valueOf(derbyStoragePageSize);
    }

    /**
     * Defines the page size, in bytes, for on-disk database pages for tables or
     * indexes used during table or index creation. Page size can only be one
     * the following values: 4096, 8192, 16384, or 32768. Set this property
     * prior to issuing the CREATE TABLE or CREATE INDEX statement. This value
     * will be used for the lifetime of the newly created conglomerates.
     * <p>
     * Default is <code>16384</code>
     *
     * @param derbyStoragePageSize the storage page size
     */
    public void setDerbyStoragePageSize(String derbyStoragePageSize) {
        this.derbyStoragePageSize = Integer.decode(derbyStoragePageSize).intValue();
    }

    /**
     * {@inheritDoc}
     */
    public void init(PMContext context) throws Exception {
        // init default values
        if (getDriver() == null) {
            setDriver(DERBY_EMBEDDED_DRIVER);
        }
        if (getDatabaseType() == null) {
            setDatabaseType("derby");
        }
        if (getUrl() == null) {
            setUrl("jdbc:derby:" + context.getHomeDir().getPath() + "/db/itemState;create=true");
        }
        if (getSchemaObjectPrefix() == null) {
            setSchemaObjectPrefix("");
        }
        super.init(context);
        // set properties       
        if (DERBY_EMBEDDED_DRIVER.equals(getDriver())) {
            conHelper.exec("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY "
                    + "('derby.storage.initialPages', '" + derbyStorageInitialPages + "')");
            conHelper.exec("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY "
                    + "('derby.storage.minimumRecordSize', '" + derbyStorageMinimumRecordSize + "')");
            conHelper.exec("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY "
                    + "('derby.storage.pageCacheSize', '" + derbyStoragePageCacheSize + "')");
            conHelper.exec("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY "
                    + "('derby.storage.pageReservedSpace', '" + derbyStoragePageReservedSpace + "')");
            conHelper.exec("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY " + "('derby.storage.pageSize', '"
                    + derbyStoragePageSize + "')");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConnectionHelper createConnectionHelper(DataSource dataSrc) {
        return new DerbyConnectionHelper(dataSrc, blockOnConnectionLoss);
    }

    /**
     * {@inheritDoc}
     *
     * Since Derby cannot handle binary indexes, we use long-long keys.
     *
     * @return {@link BundleDbPersistenceManager#SM_LONGLONG_KEYS}
     */
    public int getStorageModel() {
        return BundleDbPersistenceManager.SM_LONGLONG_KEYS;
    }

    /**
     * Closes the given connection by shutting down the embedded Derby
     * database.
     *
     * @throws SQLException if an error occurs
     */
    public void close() throws Exception {
        super.close();
        ((DerbyConnectionHelper) conHelper).shutDown(getDriver());
    }
}
