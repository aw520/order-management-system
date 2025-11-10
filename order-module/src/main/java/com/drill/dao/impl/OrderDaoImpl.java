package com.drill.dao.impl;

import com.drill.constant.OrderSchema;
import com.drill.constant.OrderStatus;
import com.drill.dao.OrderDao;
import com.drill.domain.SearchCriteria;
import com.drill.entity.Order;
import com.drill.utils.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;


public class OrderDaoImpl implements OrderDao {
    private static final Logger log = LoggerFactory.getLogger(OrderDaoImpl.class);

    @Override
    public String createOrder(Order order) {
        JDBCUtils jdbcUtils = new JDBCUtils();
        String sql = "INSERT INTO `Order` (order_id, cl_order_id, order_status, order_quantity, side, order_type, " +
                "price, price_type, currency, instrument_name, settle_type, settle_date, trade_date, creation_time, interested_party, " +
                "user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String orderId;

        try {
            if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
                boolean exists;
                do {
                    orderId = UUID.randomUUID().toString();
                    try (ResultSet rs = jdbcUtils.executeQuery("SELECT 1 FROM `Order` WHERE order_id = ?", orderId)) {
                        exists = rs.next();
                    }
                } while (exists);
            } else {
                orderId = order.getOrderId();
                try (ResultSet rs = jdbcUtils.executeQuery("SELECT 1 FROM `Order` WHERE order_id = ?", orderId)) {
                    if (rs.next()) {
                        throw new IllegalArgumentException("Order ID already exists.");
                    }
                }
            }
            System.out.println("Attempting to insert with user_id = " + order.getUserId());
            int row = jdbcUtils.executeUpdate(sql,
                    orderId,
                    order.getClOrderId(),
                    order.getOrderStatus(),
                    order.getOrderQuantity(),
                    order.getSide(),
                    order.getOrderType(),
                    order.getPrice(),
                    order.getPriceType(),
                    order.getCurrency(),
                    order.getInstrumentName(),
                    order.getSettleType(),
                    order.getSettleDate(),
                    order.getTradeDate(),
                    order.getCreationTime(),
                    order.getInterestedParty(),
                    order.getUserId() == null ? null : order.getUserId()

            );

            if (row > 0) {
                return orderId;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during order creation", e);
        } catch (Exception e) {
            log.error("Failed to insert order: {}", e.getMessage());
        } finally {
            jdbcUtils.close();
        }

        return null;
    }


    @Override
    public String deleteOrder(String id) {
        JDBCUtils jdbcUtils = new JDBCUtils();

        try {
            try (ResultSet rs = jdbcUtils.executeQuery("SELECT order_status FROM `Order` WHERE order_id = ?", id)) {
                if (!rs.next()) {
                    log.warn("Order {} not found.", id);
                    return null;
                }

                int status = rs.getInt("order_status");
                if (status != OrderStatus.NEW.getDbValue()) {
                    log.warn("Order {} cannot be deleted. Status is not NEW.", id);
                    return null;
                }
            }

            String deleteSql = "DELETE FROM `Order` WHERE order_id = ?";
            int row = jdbcUtils.executeUpdate(deleteSql, id);
            if (row > 0) {
                return id;
            }

        } catch (SQLException e) {
            log.error("Database error while deleting order: {}", e.getMessage());
            throw new RuntimeException("Failed to delete order", e);
        } finally {
            jdbcUtils.close();
        }

        return null;
    }


    @Override
    public List<Order> searchOrder(SearchCriteria searchCriteria) {
        JDBCUtils jdbcUtils = new JDBCUtils();
        StringBuilder sql = new StringBuilder(String.format(
                "SELECT * FROM %s WHERE 1=1", OrderSchema.TABLE_NAME
        ));

        List<Order> orders = new ArrayList<>();
        List<Object> sqlParams = new ArrayList<>();

        if (searchCriteria.getOrderIdLike() != null && !searchCriteria.getOrderIdLike().trim().isEmpty()) {
            String clause = String.format(" AND %s LIKE ?", OrderSchema.ORDER_ID);
            sql.append(clause);
            sqlParams.add("%" + searchCriteria.getOrderIdLike() + "%"); //Performance is bad
        }

        if (searchCriteria.getStatus() != null) {
            String clause = String.format(" AND %s = ?", OrderSchema.ORDER_STATUS);
            sql.append(clause);
            sqlParams.add(searchCriteria.getStatus().getDbValue());
        }

        int offset = (searchCriteria.getPageNumber() - 1) * searchCriteria.getPageSize();
        sql.append(String.format(" ORDER BY %s DESC LIMIT ? OFFSET ?", OrderSchema.CREATION_TIME));
        sqlParams.add(searchCriteria.getPageSize());
        sqlParams.add(offset);

        try (ResultSet rs = jdbcUtils.executeQuery(sql.toString(), sqlParams.toArray())) {
            while (rs.next()) {
                orders.add(convertResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("fail to fetch order by id due to {}", e.getMessage());
        } finally {
            jdbcUtils.close();
        }

        return orders;

    }

    @Override
    public List<Order> searchOrderForUser(SearchCriteria searchCriteria, Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        JDBCUtils jdbcUtils = new JDBCUtils();
        StringBuilder sql = new StringBuilder(String.format(
                "SELECT * FROM %s WHERE 1=1", OrderSchema.TABLE_NAME
        ));

        List<Order> orders = new ArrayList<>();
        List<Object> sqlParams = new ArrayList<>();

        if (userId != null) {
            String clause = String.format(" AND %s = ?", OrderSchema.USER_ID);
            sql.append(clause);
            sqlParams.add(userId);
        }

        if (searchCriteria.getOrderIdLike() != null && !searchCriteria.getOrderIdLike().trim().isEmpty()) {
            String clause = String.format(" AND %s LIKE ?", OrderSchema.ORDER_ID);
            sql.append(clause);
            sqlParams.add("%" + searchCriteria.getOrderIdLike() + "%"); //Performance is bad
        }

        if (searchCriteria.getStatus() != null) {
            String clause = String.format(" AND %s = ?", OrderSchema.ORDER_STATUS);
            sql.append(clause);
            sqlParams.add(searchCriteria.getStatus().getDbValue());
        }

        int offset = (searchCriteria.getPageNumber() - 1) * searchCriteria.getPageSize();
        sql.append(String.format(" ORDER BY %s DESC LIMIT ? OFFSET ?", OrderSchema.CREATION_TIME));
        sqlParams.add(searchCriteria.getPageSize());
        sqlParams.add(offset);

        try (ResultSet rs = jdbcUtils.executeQuery(sql.toString(), sqlParams.toArray())) {
            while (rs.next()) {
                orders.add(convertResultSet(rs));
            }
        } catch (SQLException e) {
            log.error("fail to fetch order by id due to {}", e.getMessage());
        } finally {
            jdbcUtils.close();
        }

        return orders;

    }

    @Override
    public int countOrder(SearchCriteria searchCriteria, Integer userId) {
        List<Order> orders;
        if (userId != null){
            orders = searchOrderForUser(searchCriteria, userId);
        }
        else{
            orders = searchOrder(searchCriteria);}
        return orders.size();
    }



    private Order convertResultSet(ResultSet rs) throws SQLException {
        return Order.builder()
                .orderId(rs.getString(OrderSchema.ORDER_ID))
                .clOrderId(rs.getString(OrderSchema.CL_ORDER_ID))
                .orderStatus(rs.getInt(OrderSchema.ORDER_STATUS))
                .orderQuantity(rs.getString(OrderSchema.ORDER_QTY))
                .side(rs.getInt(OrderSchema.SIDE))
                .orderType(rs.getInt(OrderSchema.ORDER_TYPE))
                .price(rs.getString(OrderSchema.PRICE))
                .priceType(rs.getInt(OrderSchema.PRICE_TYPE))
                .currency(rs.getString(OrderSchema.CURRENCY))
                .instrumentName(rs.getString(OrderSchema.INSTRUMENT_NAME))
                .settleType(rs.getInt(OrderSchema.SETTLE_TYPE))
                .settleDate(rs.getString(OrderSchema.SETTLE_DATE))
                .tradeDate(rs.getString(OrderSchema.TRADE_DATE))
                .creationTime(convertFromTimestamp(rs.getTimestamp(OrderSchema.CREATION_TIME)))
                .interestedParty(rs.getString(OrderSchema.INTERESTED_PARTY))
                .userId(rs.getInt(OrderSchema.USER_ID))
                .build();
    }

    private ZonedDateTime convertFromTimestamp(Timestamp timestamp) {
        return timestamp == null ? null :
                timestamp.toLocalDateTime().atZone(ZoneOffset.UTC);
    }
}
