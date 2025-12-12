package com.ordersystem.ordermanagementsystem.repository.impl;

import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> searchOrder(SearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        // Add dynamic where conditions here using criteria
        // For example:
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSide() != null) {
            predicates.add(cb.equal(root.get("side"), criteria.getSide()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(query).getResultList();
    }

    // Implement the other methods similarly...
}

