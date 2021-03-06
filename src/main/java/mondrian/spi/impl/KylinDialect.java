/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (C) 2015-2015 Pentaho
// All Rights Reserved.
*/
package mondrian.spi.impl;

import mondrian.rolap.SqlStatement;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of {@link mondrian.spi.Dialect} for Kylin.
 *
 * @author Sebastien Jelsch
 * @since Jun 08, 2015
 */
public class KylinDialect extends JdbcDialectImpl {

    public static final JdbcDialectFactory FACTORY =
            new JdbcDialectFactory(KylinDialect.class, DatabaseProduct.KYLIN) {
                protected boolean acceptsConnection(Connection connection) {
                    return super.acceptsConnection(connection);
                }
            };

    /**
     * Creates a KylinDialect.
     *
     * @param connection Connection
     * @throws SQLException on error
     */
    public KylinDialect(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public boolean allowsCountDistinct() {
        return true;
    }

    @Override
    public boolean allowsJoinOn() {
        return true;
    }

    @Override
    public SqlStatement.Type getType(ResultSetMetaData metaData, int columnIndex) throws SQLException {
        return metaData.getColumnType(columnIndex + 1) == Types.BIGINT
                ? SqlStatement.Type.LONG : super.getType(metaData, columnIndex);
    }

    @Override
    public String generateOrderItem(
            String expr,
            boolean nullable,
            boolean ascending,
            boolean collateNullsLast)
    {
        if (ascending) {
            return expr + " ASC";
        } else {
            return expr + " DESC";
        }
    }

    /*
    * Kylin doesn't support UPPER()
    */
    @Override
    public String toUpper(String expr) {
        return expr;
    }
}
