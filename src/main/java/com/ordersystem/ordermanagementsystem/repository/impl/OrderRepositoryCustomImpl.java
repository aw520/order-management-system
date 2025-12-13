package com.ordersystem.ordermanagementsystem.repository.impl;

import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> searchOrder(SearchCriteria searchCriteria) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        // orderId
        if (searchCriteria.getOrderId() != null) {

            predicates.add(
                    cb.equal(
                            root.get("orderId"),
                            searchCriteria.getOrderId()
                    )
            );
        }

        // status =
        if (searchCriteria.getStatus() != null) {
            predicates.add(
                    cb.equal(
                            root.get("orderStatus"),
                            searchCriteria.getStatus()
                    )
            );
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("creationTime")));

        TypedQuery<Order> typedQuery = em.createQuery(query);

        // pagination
        int pageSize = searchCriteria.getPageSize();
        int pageNumber = searchCriteria.getPageNumber();
        int offset = (pageNumber - 1) * pageSize;

        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }


    @Override
    public List<Order> searchOrder(SearchCriteria searchCriteria, UUID userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        // userId
        predicates.add(cb.equal(root.get("user").get("userId"), userId));

        // orderId
        if (searchCriteria.getOrderId() != null) {

            predicates.add(
                    cb.equal(
                            root.get("orderId"),
                             searchCriteria.getOrderId()
                    )
            );
        }

        // status =
        if (searchCriteria.getStatus() != null) {
            predicates.add(
                    cb.equal(
                            root.get("orderStatus"),
                            searchCriteria.getStatus()
                    )
            );
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("creationTime")));

        TypedQuery<Order> typedQuery = em.createQuery(query);

        // pagination
        int pageSize = searchCriteria.getPageSize();
        int pageNumber = searchCriteria.getPageNumber();
        int offset = (pageNumber - 1) * pageSize;

        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }



    /*
    @Override
    public Order updateOrderStatus(String orderId, OrderStatus status) {
        Order order = em.find(Order.class, orderId);
        order.setOrderStatus(status.getDbValue());
        order.setLastUpdateTime();
        return order;
    }
    */
}

