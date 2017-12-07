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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.apache.jackrabbit.core.fs.FileSystemResource;
import org.apache.jackrabbit.core.fs.local.LocalFileSystem;
import org.apache.jackrabbit.core.id.NodeId;
import org.apache.jackrabbit.core.id.PropertyId;
import org.apache.jackrabbit.core.persistence.PMContext;
import org.apache.jackrabbit.core.persistence.bundle.AbstractBundlePersistenceManager;
import org.apache.jackrabbit.core.persistence.pool.BundleDbPersistenceManager;
import org.apache.jackrabbit.core.persistence.pool.DbNameIndex;
import org.apache.jackrabbit.core.persistence.util.BLOBStore;
import org.apache.jackrabbit.core.persistence.util.BundleBinding;
import org.apache.jackrabbit.core.persistence.util.ErrorHandling;
import org.apache.jackrabbit.core.persistence.util.FileSystemBLOBStore;
import org.apache.jackrabbit.core.persistence.util.NodeInfo;
import org.apache.jackrabbit.core.persistence.util.NodePropBundle;
import org.apache.jackrabbit.core.persistence.util.Serializer;
import org.apache.jackrabbit.core.state.ChangeLog;
import org.apache.jackrabbit.core.state.ItemStateException;
import org.apache.jackrabbit.core.state.NoSuchItemStateException;
import org.apache.jackrabbit.core.state.NodeReferences;
import org.apache.jackrabbit.core.util.StringIndex;
import org.apache.jackrabbit.core.util.db.CheckSchemaOperation;
import org.apache.jackrabbit.core.util.db.ConnectionFactory;
import org.apache.jackrabbit.core.util.db.ConnectionHelper;
import org.apache.jackrabbit.core.util.db.DatabaseAware;
import org.apache.jackrabbit.core.util.db.DbUtility;
import org.apache.jackrabbit.core.util.db.StreamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bstek.urule.console.repository.RepositoryBuilder;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class DbPersistenceManager extends AbstractBundlePersistenceManager implements DatabaseAware {

    /** the default logger */
    private static Logger log = LoggerFactory.getLogger(BundleDbPersistenceManager.class);

    /** storage model modifier: binary keys */
    public static final int SM_BINARY_KEYS = 1;

    /** storage model modifier: longlong keys */
    public static final int SM_LONGLONG_KEYS = 2;

    /** flag indicating if this manager was initialized */
    protected boolean initialized;

    /** the jdbc driver name */
    protected String driver;

    /** the jdbc url string */
    protected String url;

    /** the jdbc user */
    protected String user;

    /** the jdbc password */
    protected String password;

    /** the database type */
    protected String databaseType;

    /** the logical name of the data source to use */
    protected String dataSourceName;

    /** the {@link ConnectionHelper} set in the {@link #init(PMContext)} method */
    protected ConnectionHelper conHelper;

    /** the prefix for the database objects */
    protected String schemaObjectPrefix;

    /** flag indicating if a consistency check should be issued during startup */
    protected boolean consistencyCheck;

    /** flag indicating if the consistency check should attempt to fix issues */
    protected boolean consistencyFix;

    /** initial size of buffer used to serialize objects */
    protected static final int INITIAL_BUFFER_SIZE = 1024;

    /** indicates if uses (filesystem) blob store */
    protected boolean externalBLOBs;

    /** indicates whether to block if the database connection is lost */
    protected boolean blockOnConnectionLoss;

    // SQL statements for bundle management
    protected String bundleInsertSQL;
    protected String bundleUpdateSQL;
    protected String bundleSelectSQL;
    protected String bundleDeleteSQL;
    protected String bundleSelectAllIdsFromSQL;
    protected String bundleSelectAllIdsSQL;
    protected String bundleSelectAllBundlesFromSQL;
    protected String bundleSelectAllBundlesSQL;

    // SQL statements for NodeReference management
    protected String nodeReferenceInsertSQL;
    protected String nodeReferenceUpdateSQL;
    protected String nodeReferenceSelectSQL;
    protected String nodeReferenceDeleteSQL;

    /** file system where BLOB data is stored */
    protected CloseableBLOBStore blobStore;

    /** the index for local names */
    private StringIndex nameIndex;

    /**
     * the minimum size of a property until it gets written to the blob store
     * @see #setMinBlobSize(String)
     */
    private int minBlobSize = 0x1000;

    /**
     * flag for error handling
     */
    protected ErrorHandling errorHandling = new ErrorHandling();

    /**
     * the bundle binding
     */
    protected BundleBinding binding;

    /**
     * the name of this persistence manager
     */
    private String name = super.toString();

    /**
     * Whether the schema check must be done during initialization.
     */
    private boolean schemaCheckEnabled = true;
/*
    *//**
     * The repositories {@link ConnectionFactory}.
     *//*
    private ConnectionFactory connectionFactory;*/

    /**
     * {@inheritDoc}
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        //this.connectionFactory = connectionFactory;
    }

    /**
     * Returns the configured JDBC connection url.
     * @return the configured JDBC connection url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the JDBC connection URL.
     * The connection can be created using a JNDI Data Source as well.
     * To do that, the driver class name must reference a javax.naming.Context class
     * (for example javax.naming.InitialContext), and the URL must be the JNDI URL
     * (for example java:comp/env/jdbc/Test).
     *
     * @param url the url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the configured user that is used to establish JDBC connections.
     * @return the JDBC user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user name that will be used to establish JDBC connections.
     * @param user the user name.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Returns the configured password that is used to establish JDBC connections.
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password that will be used to establish JDBC connections.
     * @param password the password for the connection
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the class name of the JDBC driver.
     * @return the class name of the JDBC driver.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets the class name of the JDBC driver. The driver class will be loaded
     * during {@link #init(PMContext) init} in order to assure the existence.
     * If no driver is specified, the default driver for the database is used.
     *
     * @param driver the class name of the driver
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Returns the configured schema object prefix.
     * @return the configured schema object prefix.
     */
    public String getSchemaObjectPrefix() {
        return schemaObjectPrefix;
    }

    /**
     * Sets the schema object prefix. This string is used to prefix all schema
     * objects, like tables and indexes. this is useful, if several persistence
     * managers use the same database.
     *
     * @param schemaObjectPrefix the prefix for schema objects.
     */
    public void setSchemaObjectPrefix(String schemaObjectPrefix) {
        // make sure prefix is all uppercase
        this.schemaObjectPrefix = schemaObjectPrefix.toUpperCase();
    }

    /**
     * Returns the configured database type name.
     * @deprecated
     * This method is deprecated; {@link getDatabaseType} should be used instead.
     * 
     * @return the database type name.
     */
    public String getSchema() {
        return databaseType;
    }

    /**
     * Returns the configured database type name.
     * @return the database type name.
     */
    public String getDatabaseType() {
        return databaseType;
    }

    /**
     * Sets the database type. This identifier is used to load and execute
     * the respective .ddl resource in order to create the required schema
     * objects.
     * @deprecated
     * This method is deprecated; {@link setDatabaseType} should be used instead.
     *
     * @param databaseType database type name
     */
    public void setSchema(String databaseType) {
        this.databaseType = databaseType;
    }
    
    /**
     * Sets the database type. This identifier is used to load and execute
     * the respective .ddl resource in order to create the required schema
     * objects.
     *
     * @param databaseType database type name
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * Returns if uses external (filesystem) blob store.
     * @return if uses external (filesystem) blob store.
     */
    public boolean isExternalBLOBs() {
        return externalBLOBs;
    }

    /**
     * Sets the flag for external (filesystem) blob store usage.
     * @param externalBLOBs a value of "true" indicates that an external blob
     *        store is to be used.
     */
    public void setExternalBLOBs(boolean externalBLOBs) {
        this.externalBLOBs = externalBLOBs;
    }

    /**
     * Checks if consistency check is enabled.
     * @return <code>true</code> if consistency check is enabled.
     */
    public String getConsistencyCheck() {
        return Boolean.toString(consistencyCheck);
    }

    /**
     * Defines if a consistency check is to be performed on initialization.
     * @param consistencyCheck the consistency check flag.
     */
    public void setConsistencyCheck(String consistencyCheck) {
        this.consistencyCheck = Boolean.valueOf(consistencyCheck).booleanValue();
    }

    /**
     * Checks if consistency fix is enabled.
     * @return <code>true</code> if consistency fix is enabled.
     */
    public String getConsistencyFix() {
        return Boolean.toString(consistencyFix);
    }

    /**
     * Defines if the consistency check should attempt to fix issues that
     * it finds.
     *
     * @param consistencyFix the consistency fix flag.
     */
    public void setConsistencyFix(String consistencyFix) {
        this.consistencyFix = Boolean.valueOf(consistencyFix).booleanValue();
    }

    /**
     * Returns the minimum blob size in bytes.
     * @return the minimum blob size in bytes.
     */
    public String getMinBlobSize() {
        return String.valueOf(minBlobSize);
    }

    /**
     * Sets the minimum blob size. This size defines the threshold of which
     * size a property is included in the bundle or is stored in the blob store.
     *
     * @param minBlobSize the minimum blob size in bytes.
     */
    public void setMinBlobSize(String minBlobSize) {
        this.minBlobSize = Integer.decode(minBlobSize).intValue();
    }

    /**
     * Sets the error handling behaviour of this manager. See {@link ErrorHandling}
     * for details about the flags.
     *
     * @param errorHandling the error handling flags
     */
    public void setErrorHandling(String errorHandling) {
        this.errorHandling = new ErrorHandling(errorHandling);
    }

    /**
     * Returns the error handling configuration of this manager
     * @return the error handling configuration of this manager
     */
    public String getErrorHandling() {
        return errorHandling.toString();
    }

    public void setBlockOnConnectionLoss(String block) {
        this.blockOnConnectionLoss = Boolean.valueOf(block).booleanValue();
    }

    public String getBlockOnConnectionLoss() {
        return Boolean.toString(blockOnConnectionLoss);
    }

    /**
     * Returns <code>true</code> if the blobs are stored in the DB.
     * @return <code>true</code> if the blobs are stored in the DB.
     */
    public boolean useDbBlobStore() {
        return !externalBLOBs;
    }

    /**
     * Returns <code>true</code> if the blobs are stored in the local fs.
     * @return <code>true</code> if the blobs are stored in the local fs.
     */
    public boolean useLocalFsBlobStore() {
        return externalBLOBs;
    }

    /**
     * @return whether the schema check is enabled
     */
    public final boolean isSchemaCheckEnabled() {
        return schemaCheckEnabled;
    }

    /**
     * @param enabled set whether the schema check is enabled
     */
    public final void setSchemaCheckEnabled(boolean enabled) {
        schemaCheckEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     *
     * Basically wraps a JDBC transaction around super.store().
     * 
     * FIXME: the retry logic is almost a duplicate of {@code ConnectionHelper.RetryManager}.
     */
    public synchronized void store(final ChangeLog changeLog) throws ItemStateException {
        int failures = 0;
        ItemStateException lastException = null;
        boolean sleepInterrupted = false;
        while (!sleepInterrupted && (blockOnConnectionLoss || failures <= 1)) {
            try {
                conHelper.startBatch();
                super.store(changeLog);
                conHelper.endBatch(true);
                return;
            } catch (SQLException e) {
                // Either startBatch or stopBatch threw it: either way the
                // transaction was not persisted and no action needs to be taken.
                lastException = new ItemStateException(e.getMessage(), e);
            } catch (ItemStateException e) {
                // store call threw it: we need to cancel the transaction
                lastException = e;
                try {
                    conHelper.endBatch(false);
                } catch (SQLException e2) {
                    DbUtility.logException("rollback failed", e2);
                }

                // if we got here due to a constraint violation and we
                // are running in test mode, we really want to stop
                assert !isIntegrityConstraintViolation(e.getCause());
            }
            failures++;
            log.error("Failed to persist ChangeLog (stacktrace on DEBUG log level), blockOnConnectionLoss = "
                    + blockOnConnectionLoss + ": " + lastException);
            log.debug("Failed to persist ChangeLog", lastException);
            if (blockOnConnectionLoss || failures <= 1) { // if we're going to try again
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    sleepInterrupted = true;
                    log.error("Interrupted: canceling retry of ChangeLog storage");
                }
            }
        }
        throw lastException;
    }

    private boolean isIntegrityConstraintViolation(Throwable t) {
        if (t instanceof SQLException) {
            String state = ((SQLException) t).getSQLState();
            return state != null && state.startsWith("23");
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void init(PMContext context) throws Exception {
        if (initialized) {
            throw new IllegalStateException("already initialized");
        }
        super.init(context);

        conHelper = createConnectionHelper(getDataSource());
        
        this.name = context.getHomeDir().getName();        

        // make sure schemaObjectPrefix consists of legal name characters only
        schemaObjectPrefix = conHelper.prepareDbIdentifier(schemaObjectPrefix);

        // check if schema objects exist and create them if necessary
        if (isSchemaCheckEnabled()) {
            createCheckSchemaOperation().run();
        }

        // create correct blob store
        blobStore = createBlobStore();

        buildSQLStatements();

        // load namespaces
        binding = new BundleBinding(errorHandling, blobStore, getNsIndex(), getNameIndex(), context.getDataStore());
        binding.setMinBlobSize(minBlobSize);

        initialized = true;

        if (consistencyCheck) {
            // check all bundles
            checkConsistency(null, true, consistencyFix);
        }
                
    }

    private DataSource getDataSource() throws Exception {
    	setDatabaseType(RepositoryBuilder.databaseType);
    	return RepositoryBuilder.datasource;
    }

    /**
     * This method is called from the {@link #init(PMContext)} method of this class and returns a
     * {@link ConnectionHelper} instance which is assigned to the {@code conHelper} field. Subclasses may
     * override it to return a specialized connection helper.
     * 
     * @param dataSrc the {@link DataSource} of this persistence manager
     * @return a {@link ConnectionHelper}
     * @throws Exception on error
     */
    protected ConnectionHelper createConnectionHelper(DataSource dataSrc) throws Exception {
        return new ConnectionHelper(dataSrc, blockOnConnectionLoss);
    }

    /**
     * This method is called from {@link #init(PMContext)} after the
     * {@link #createConnectionHelper(DataSource)} method, and returns a default {@link CheckSchemaOperation}.
     * Subclasses can overrride this implementation to get a customized implementation.
     * 
     * @return a new {@link CheckSchemaOperation} instance
     */
    protected CheckSchemaOperation createCheckSchemaOperation() {
        InputStream in =
            AbstractBundlePersistenceManager.class.getResourceAsStream(
                    databaseType + ".ddl");
        return new CheckSchemaOperation(conHelper, in, schemaObjectPrefix + "BUNDLE").addVariableReplacement(
            CheckSchemaOperation.SCHEMA_OBJECT_PREFIX_VARIABLE, schemaObjectPrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BLOBStore getBlobStore() {
        return blobStore;
    }

    /**
     * Creates a suitable blobstore
     * @return a blobstore
     * @throws Exception if an unspecified error occurs
     */
    protected CloseableBLOBStore createBlobStore() throws Exception {
        if (useLocalFsBlobStore()) {
            return createLocalFSBlobStore(context);
        } else {
            return createDBBlobStore(context);
        }
    }

    /**
     * Returns the local name index
     * @return the local name index
     * @throws IllegalStateException if an error occurs.
     */
    public StringIndex getNameIndex() {
        try {
            if (nameIndex == null) {
                FileSystemResource res = new FileSystemResource(context.getFileSystem(), RES_NAME_INDEX);
                if (res.exists()) {
                    nameIndex = super.getNameIndex();
                } else {
                    // create db nameindex
                    nameIndex = createDbNameIndex();
                }
            }
            return nameIndex;
        } catch (Exception e) {
            IllegalStateException exception =
                new IllegalStateException("Unable to create nsIndex");
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * Returns a new instance of a DbNameIndex.
     * @return a new instance of a DbNameIndex.
     * @throws SQLException if an SQL error occurs.
     */
    protected DbNameIndex createDbNameIndex() throws SQLException {
        return new DbNameIndex(conHelper, schemaObjectPrefix);
    }

    /**
     * returns the storage model
     * @return the storage model
     */
    public int getStorageModel() {
        return SM_BINARY_KEYS;
    }

    /**
     * Creates a blob store that is based on a local fs. This is called by
     * init if {@link #useLocalFsBlobStore()} returns <code>true</code>.
     *
     * @param context the persistence manager context
     * @return a blob store
     * @throws Exception if an error occurs.
     */
    protected CloseableBLOBStore createLocalFSBlobStore(PMContext context)
            throws Exception {
        /**
         * store blob's in local file system in a sub directory
         * of the workspace home directory
         */
        LocalFileSystem blobFS = new LocalFileSystem();
        blobFS.setRoot(new File(context.getHomeDir(), "blobs"));
        blobFS.init();
        return new FSBlobStore(blobFS);
    }

    /**
     * Creates a blob store that uses the database. This is called by
     * init if {@link #useDbBlobStore()} returns <code>true</code>.
     *
     * @param context the persistence manager context
     *
     * @return a blob store
     * @throws Exception if an error occurs.
     */
    protected CloseableBLOBStore createDBBlobStore(PMContext context)
            throws Exception {
        return new DbBlobStore();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void close() throws Exception {
        if (!initialized) {
            throw new IllegalStateException("not initialized");
        }

        try {
            if (nameIndex instanceof DbNameIndex) {
                ((DbNameIndex) nameIndex).close();
            }            
            // close blob store
            blobStore.close();
            blobStore = null;
            super.close();
        } finally {
            initialized = false;
        }
    }

    /**
     * Constructs a parameter list for a PreparedStatement
     * for the given node identifier.
     *
     * @param id the node id
     * @return a list of Objects
     */
    protected Object[] getKey(NodeId id) {
        if (getStorageModel() == SM_BINARY_KEYS) {
            return new Object[] { id.getRawBytes() };
        } else {
            return new Object[] {
                    id.getMostSignificantBits(), id.getLeastSignificantBits() };
        }
    }

    /**
     * Creates a parameter array for an SQL statement that needs
     * (i) a node identifier, and (2) another parameter.
     *
     * @param id the node id
     * @param p the other parameter
     * @param before whether the other parameter should be before the uuid parameter
     * @return an Object array that represents the parameters
     */
    protected Object[] createParams(NodeId id, Object p, boolean before) {

        // Create the key
        List<Object> key = new ArrayList<Object>();
        if (getStorageModel() == SM_BINARY_KEYS) {
            key.add(id.getRawBytes());
        } else {
            key.add(id.getMostSignificantBits());
            key.add(id.getLeastSignificantBits());
        }

        // Create the parameters
        List<Object> params = new ArrayList<Object>();
        if (before) {
            params.add(p);
            params.addAll(key);
        } else {
            params.addAll(key);
            params.add(p);
        }

        return params.toArray();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<NodeId> getAllNodeIds(NodeId bigger, int maxCount)
            throws ItemStateException, RepositoryException {
        ResultSet rs = null;
        try {
            String sql = bundleSelectAllIdsSQL;
            NodeId lowId = null;
            Object[] keys = new Object[0];
            if (bigger != null) {
                sql = bundleSelectAllIdsFromSQL;
                lowId = bigger;
                keys = getKey(bigger);
            }
            if (getStorageModel() == SM_LONGLONG_KEYS  && maxCount > 0) {
                // get some more rows, in case the first row is smaller
                // only required for SM_LONGLONG_KEYS
                // probability is very low to get get the wrong first key, < 1 : 2^64
                // see also bundleSelectAllIdsFrom SQL statement
                maxCount += 10;
            }
            rs = conHelper.exec(sql, keys, false, maxCount);
            ArrayList<NodeId> result = new ArrayList<NodeId>();
            while ((maxCount == 0 || result.size() < maxCount) && rs.next()) {
                NodeId current;
                if (getStorageModel() == SM_BINARY_KEYS) {
                    current = new NodeId(rs.getBytes(1));
                } else {
                    long high = rs.getLong(1);
                    long low = rs.getLong(2);
                    current = new NodeId(high, low);
                    if (lowId != null) {
                        // skip the keys that are smaller or equal (see above, maxCount += 10)
                        // only required for SM_LONGLONG_KEYS
                        if (current.compareTo(lowId) <= 0) {
                            continue;
                        }
                    }
                }
                result.add(current);
            }
            return result;
        } catch (SQLException e) {
            String msg = "getAllNodeIds failed.";
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        } finally {
            DbUtility.close(rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Map<NodeId, NodeInfo> getAllNodeInfos(NodeId bigger, int maxCount) throws ItemStateException {
        ResultSet rs = null;
        try {
            String sql = bundleSelectAllBundlesSQL;
            NodeId lowId = null;
            Object[] keys = new Object[0];
            if (bigger != null) {
                sql = bundleSelectAllBundlesFromSQL;
                lowId = bigger;
                keys = getKey(bigger);
            }
            if (getStorageModel() == SM_LONGLONG_KEYS && maxCount > 0) {
                // get some more rows, in case the first row is smaller
                // only required for SM_LONGLONG_KEYS
                // probability is very low to get get the wrong first key, < 1 : 2^64
                // see also bundleSelectAllIdsFrom SQL statement
                maxCount += 10;
            }
            rs = conHelper.exec(sql, keys, false, maxCount);
            Map<NodeId, NodeInfo> result = new LinkedHashMap<NodeId, NodeInfo>(maxCount);
            while ((maxCount == 0 || result.size() < maxCount) && rs.next()) {
                NodeId current;
                if (getStorageModel() == SM_BINARY_KEYS) {
                    current = new NodeId(rs.getBytes(1));
                } else {
                    long high = rs.getLong(1);
                    long low = rs.getLong(2);
                    current = new NodeId(high, low);
                }
                if (getStorageModel() == SM_LONGLONG_KEYS && lowId != null) {
                    // skip the keys that are smaller or equal (see above, maxCount += 10)
                    if (current.compareTo(lowId) <= 0) {
                        continue;
                    }
                }
                NodePropBundle bundle = readBundle(current, rs, getStorageModel() == SM_LONGLONG_KEYS ? 3 : 2);
                NodeInfo nodeInfo = new NodeInfo(bundle);
                result.put(nodeInfo.getId(), nodeInfo);
            }
            return result;
        } catch (SQLException e) {
            String msg = "getAllNodeIds failed.";
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        } finally {
            DbUtility.close(rs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NodePropBundle loadBundle(NodeId id) throws ItemStateException {
        try {
            ResultSet rs =
                conHelper.exec(bundleSelectSQL, getKey(id), false, 0);
            try {
                if (rs != null && rs.next()) {
                    return readBundle(id, rs, 1);
                } else {
                    return null;
                }
            } finally {
            	if (rs != null) {
            		rs.close();
            	}
            }
        } catch (SQLException e) {
        	String msg = "failed to read bundle (stacktrace on DEBUG log level): " + id + ": " + e; 
            log.error(msg);
            log.debug("failed to read bundle: " + id, e);
            throw new ItemStateException(msg, e);
        }
    }

    /**
     * Reads and parses a bundle from the BLOB in the given column of the
     * current row of the given result set. This is a helper method to
     * circumvent issues JCR-1039 and JCR-1474.
     *
     * @param id bundle identifier
     * @param rs result set
     * @param column BLOB column
     * @return parsed bundle
     * @throws SQLException if the bundle can not be read or parsed
     */
    private NodePropBundle readBundle(NodeId id, ResultSet rs, int column)
            throws SQLException {
        try {
            InputStream in;
            if (rs.getMetaData().getColumnType(column) == Types.BLOB) {
                in = rs.getBlob(column).getBinaryStream();
            } else {
                in = rs.getBinaryStream(column);
            }
            try {
                return binding.readBundle(in, id);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            SQLException exception =
                new SQLException("Failed to parse bundle " + id);
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected synchronized void storeBundle(NodePropBundle bundle) throws ItemStateException {
        try {
            ByteArrayOutputStream out =
                new ByteArrayOutputStream(INITIAL_BUFFER_SIZE);
            binding.writeBundle(out, bundle);

            String sql = bundle.isNew() ? bundleInsertSQL : bundleUpdateSQL;
            Object[] params = createParams(bundle.getId(), out.toByteArray(), true);
            conHelper.update(sql, params);
        } catch (Exception e) {
            String msg;

            if (isIntegrityConstraintViolation(e)) {
                // we should never get an integrity constraint violation here
                // other PMs may not be able to detect this and end up with
                // corrupted data
                msg = "FATAL error while writing the bundle: " + bundle.getId();
            } else {
                msg = "failed to write bundle: " + bundle.getId();
            }

            log.error(msg, e);
            throw new ItemStateException(msg, e);
        }
   }

    /**
     * {@inheritDoc}
     */
    protected synchronized void destroyBundle(NodePropBundle bundle) throws ItemStateException {
        try {
            conHelper.update(bundleDeleteSQL, getKey(bundle.getId()));
        } catch (Exception e) {
            if (e instanceof NoSuchItemStateException) {
                throw (NoSuchItemStateException) e;
            }
            String msg = "failed to delete bundle: " + bundle.getId();
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized NodeReferences loadReferencesTo(NodeId targetId)
            throws NoSuchItemStateException, ItemStateException {
        if (!initialized) {
            throw new IllegalStateException("not initialized");
        }

        ResultSet rs = null;
        InputStream in = null;
        try {
            rs = conHelper.exec(nodeReferenceSelectSQL, getKey(targetId), false, 0);
            if (!rs.next()) {
                throw new NoSuchItemStateException(targetId.toString());
            }

            in = rs.getBinaryStream(1);
            NodeReferences refs = new NodeReferences(targetId);
            Serializer.deserialize(refs, in);

            return refs;
        } catch (Exception e) {
            if (e instanceof NoSuchItemStateException) {
                throw (NoSuchItemStateException) e;
            }
            String msg = "failed to read references: " + targetId;
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        } finally {
            IOUtils.closeQuietly(in);
            DbUtility.close(rs);
        }
    }

    /**
     * {@inheritDoc}
     *
     * This method uses shared <code>PreparedStatements</code>, which must
     * be used strictly sequentially. Because this method synchronizes on the
     * persistence manager instance, there is no need to synchronize on the
     * shared statement. If the method would not be synchronized, the shared
     * statement must be synchronized.
     */
    public synchronized void store(NodeReferences refs) throws ItemStateException {
        if (!initialized) {
            throw new IllegalStateException("not initialized");
        }

        // check if insert or update
        boolean update = existsReferencesTo(refs.getTargetId());
        String sql = (update) ? nodeReferenceUpdateSQL : nodeReferenceInsertSQL;

        try {
            ByteArrayOutputStream out =
                    new ByteArrayOutputStream(INITIAL_BUFFER_SIZE);
            // serialize references
            Serializer.serialize(refs, out);

            Object[] params = createParams(refs.getTargetId(), out.toByteArray(), true);
            conHelper.exec(sql, params);
            
            // there's no need to close a ByteArrayOutputStream
            //out.close();
        } catch (Exception e) {
            String msg = "failed to write " + refs;
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void destroy(NodeReferences refs) throws ItemStateException {
        if (!initialized) {
            throw new IllegalStateException("not initialized");
        }

        try {
            conHelper.exec(nodeReferenceDeleteSQL, getKey(refs.getTargetId()));
        } catch (Exception e) {
            if (e instanceof NoSuchItemStateException) {
                throw (NoSuchItemStateException) e;
            }
            String msg = "failed to delete " + refs;
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean existsReferencesTo(NodeId targetId) throws ItemStateException {
        if (!initialized) {
            throw new IllegalStateException("not initialized");
        }

        ResultSet rs = null;
        try {
            rs = conHelper.exec(nodeReferenceSelectSQL, getKey(targetId), false, 0);

            // a reference exists if the result has at least one entry
            return rs.next();
        } catch (Exception e) {
            String msg = "failed to check existence of node references: "
                + targetId;
            log.error(msg, e);
            throw new ItemStateException(msg, e);
        } finally {
            DbUtility.close(rs);
        }
    }

    public String toString() {
        return name;
    }

    /**
     * Initializes the SQL strings.
     */
    protected void buildSQLStatements() {
        // prepare statements
        if (getStorageModel() == SM_BINARY_KEYS) {
            bundleInsertSQL = "insert into " + schemaObjectPrefix + "BUNDLE (BUNDLE_DATA, NODE_ID) values (?, ?)";
            bundleUpdateSQL = "update " + schemaObjectPrefix + "BUNDLE set BUNDLE_DATA = ? where NODE_ID = ?";
            bundleSelectSQL = "select BUNDLE_DATA from " + schemaObjectPrefix + "BUNDLE where NODE_ID = ?";
            bundleDeleteSQL = "delete from " + schemaObjectPrefix + "BUNDLE where NODE_ID = ?";

            nodeReferenceInsertSQL = "insert into " + schemaObjectPrefix + "REFS (REFS_DATA, NODE_ID) values (?, ?)";
            nodeReferenceUpdateSQL = "update " + schemaObjectPrefix + "REFS set REFS_DATA = ? where NODE_ID = ?";
            nodeReferenceSelectSQL = "select REFS_DATA from " + schemaObjectPrefix + "REFS where NODE_ID = ?";
            nodeReferenceDeleteSQL = "delete from " + schemaObjectPrefix + "REFS where NODE_ID = ?";

            bundleSelectAllIdsSQL = "select NODE_ID from " + schemaObjectPrefix + "BUNDLE ORDER BY NODE_ID";
            bundleSelectAllIdsFromSQL = "select NODE_ID from " + schemaObjectPrefix + "BUNDLE WHERE NODE_ID > ? ORDER BY NODE_ID";
            bundleSelectAllBundlesSQL = "select NODE_ID, BUNDLE_DATA from " + schemaObjectPrefix + "BUNDLE ORDER BY NODE_ID";
            bundleSelectAllBundlesFromSQL = "select NODE_ID, BUNDLE_DATA from " + schemaObjectPrefix + "BUNDLE WHERE NODE_ID > ? ORDER BY NODE_ID";
        } else {
            bundleInsertSQL = "insert into " + schemaObjectPrefix + "BUNDLE (BUNDLE_DATA, NODE_ID_HI, NODE_ID_LO) values (?, ?, ?)";
            bundleUpdateSQL = "update " + schemaObjectPrefix + "BUNDLE set BUNDLE_DATA = ? where NODE_ID_HI = ? and NODE_ID_LO = ?";
            bundleSelectSQL = "select BUNDLE_DATA from " + schemaObjectPrefix + "BUNDLE where NODE_ID_HI = ? and NODE_ID_LO = ?";
            bundleDeleteSQL = "delete from " + schemaObjectPrefix + "BUNDLE where NODE_ID_HI = ? and NODE_ID_LO = ?";

            nodeReferenceInsertSQL =
                "insert into " + schemaObjectPrefix + "REFS"
                + " (REFS_DATA, NODE_ID_HI, NODE_ID_LO) values (?, ?, ?)";
            nodeReferenceUpdateSQL =
                "update " + schemaObjectPrefix + "REFS"
                + " set REFS_DATA = ? where NODE_ID_HI = ? and NODE_ID_LO = ?";
            nodeReferenceSelectSQL = "select REFS_DATA from " + schemaObjectPrefix + "REFS where NODE_ID_HI = ? and NODE_ID_LO = ?";
            nodeReferenceDeleteSQL = "delete from " + schemaObjectPrefix + "REFS where NODE_ID_HI = ? and NODE_ID_LO = ?";

            bundleSelectAllIdsSQL = "select NODE_ID_HI, NODE_ID_LO from " + schemaObjectPrefix 
                + "BUNDLE ORDER BY NODE_ID_HI, NODE_ID_LO";
            // need to use HI and LO parameters
            // this is not the exact statement, but not all databases support WHERE (NODE_ID_HI, NODE_ID_LOW) >= (?, ?)
            bundleSelectAllIdsFromSQL =
                "select NODE_ID_HI, NODE_ID_LO from " + schemaObjectPrefix + "BUNDLE"
                + " WHERE (NODE_ID_HI >= ?) AND (? IS NOT NULL)"
                + " ORDER BY NODE_ID_HI, NODE_ID_LO";

            bundleSelectAllBundlesSQL = "select NODE_ID_HI, NODE_ID_LO, BUNDLE_DATA from " + schemaObjectPrefix
                    + "BUNDLE ORDER BY NODE_ID_HI, NODE_ID_LO";
            // need to use HI and LO parameters
            // this is not the exact statement, but not all databases support WHERE (NODE_ID_HI, NODE_ID_LOW) >= (?, ?)
            bundleSelectAllBundlesFromSQL =
                    "select NODE_ID_HI, NODE_ID_LO, BUNDLE_DATA from " + schemaObjectPrefix + "BUNDLE"
                            + " WHERE (NODE_ID_HI >= ?) AND (? IS NOT NULL)"
                            + " ORDER BY NODE_ID_HI, NODE_ID_LO";

        }

    }

    /**
     * Helper interface for closeable stores
     */
    protected static interface CloseableBLOBStore extends BLOBStore {
        void close();
    }

    /**
     * own implementation of the filesystem blob store that uses a different
     * blob-id scheme.
     */
    protected class FSBlobStore extends FileSystemBLOBStore implements CloseableBLOBStore {

        private FileSystem fs;

        public FSBlobStore(FileSystem fs) {
            super(fs);
            this.fs = fs;
        }

        public String createId(PropertyId id, int index) {
            return buildBlobFilePath(null, id, index).toString();
        }

        public void close() {
            try {
                fs.close();
                fs = null;
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Implementation of a blob store that stores the data inside the database
     */
    protected class DbBlobStore implements CloseableBLOBStore {

        protected String blobInsertSQL;
        protected String blobUpdateSQL;
        protected String blobSelectSQL;
        protected String blobSelectExistSQL;
        protected String blobDeleteSQL;

        public DbBlobStore() throws SQLException {
            blobInsertSQL = "insert into " + schemaObjectPrefix + "BINVAL (BINVAL_DATA, BINVAL_ID) values (?, ?)";
            blobUpdateSQL = "update " + schemaObjectPrefix + "BINVAL set BINVAL_DATA = ? where BINVAL_ID = ?";
            blobSelectSQL = "select BINVAL_DATA from " + schemaObjectPrefix + "BINVAL where BINVAL_ID = ?";
            blobSelectExistSQL = "select 1 from " + schemaObjectPrefix + "BINVAL where BINVAL_ID = ?";
            blobDeleteSQL = "delete from " + schemaObjectPrefix + "BINVAL where BINVAL_ID = ?";
        }

        /**
         * {@inheritDoc}
         */
        public String createId(PropertyId id, int index) {
            StringBuilder buf = new StringBuilder();
            buf.append(id.getParentId().toString());
            buf.append('.');
            buf.append(getNsIndex().stringToIndex(id.getName().getNamespaceURI()));
            buf.append('.');
            buf.append(getNameIndex().stringToIndex(id.getName().getLocalName()));
            buf.append('.');
            buf.append(index);
            return buf.toString();
        }

        /**
         * {@inheritDoc}
         */
        public InputStream get(String blobId) throws Exception {
            ResultSet rs = null;
            boolean close = true;
            try {
                rs = conHelper.exec(blobSelectSQL, new Object[]{blobId}, false, 0);
                if (!rs.next()) {
                    throw new Exception("no such BLOB: " + blobId);
                }

                InputStream in = rs.getBinaryStream(1);
                if (in == null) {
                    // some databases treat zero-length values as NULL;
                    // return empty InputStream in such a case
                    return new ByteArrayInputStream(new byte[0]);
                }

                 // return an InputStream wrapper in order to close the ResultSet when the stream is closed
                close = false;
                final ResultSet rs2 = rs;
                return new FilterInputStream(in) {

                    public void close() throws IOException {
                        try {
                            in.close();
                        } finally {
                            // now it's safe to close ResultSet
                            DbUtility.close(rs2);
                        }
                    }
                };
            } finally {
                if (close) {
                    DbUtility.close(rs);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public synchronized void put(String blobId, InputStream in, long size)
                throws Exception {
            ResultSet rs = null;
            boolean exists;
                try {
                    rs = conHelper.exec(blobSelectExistSQL, new Object[]{blobId}, false, 0);
                    // a BLOB exists if the result has at least one entry
                    exists = rs.next();
                } finally {
                    DbUtility.close(rs);
                }
            String sql = (exists) ? blobUpdateSQL : blobInsertSQL;
            Object[] params = new Object[]{new StreamWrapper(in, size), blobId};
            conHelper.exec(sql, params);
        }

        /**
         * {@inheritDoc}
         */
        public synchronized boolean remove(String blobId) throws Exception {
            return conHelper.update(blobDeleteSQL, new Object[]{blobId}) == 1;
        }

        public void close() {
            // closing the database resources of this blobstore is left to the
            // owning BundleDbPersistenceManager
        }
    }
}
