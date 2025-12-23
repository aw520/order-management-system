package com.example.orderservice.repository.impl;

import com.example.orderservice.dto.SearchCriteria;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepositoryCustom;
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

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Order> searchOrder(SearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        //orderId
        if(criteria.getOrderId() != null){
            predicates.add(cb.equal(root.get("orderId"), criteria.getOrderId()));
        }
        //clientId
        if(criteria.getClientId() != null){
            predicates.add(cb.equal(root.get("clientId"), criteria.getClientId()));
        }
        //status
        if(criteria.getStatus() != null){
            predicates.add(cb.equal(root.get("orderStatus"), criteria.getStatus()));
        }
        //creation time
        if(criteria.getCreatedAfter()!= null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("creationTime"), criteria.getCreatedAfter()));
        }
        if(criteria.getCreatedBefore() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("creationTime"), criteria.getCreatedBefore()));
        }
        //last updated time
        if(criteria.getUpdatedAfter() != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastUpdateTime"), criteria.getUpdatedAfter()));
        }
        if(criteria.getUpdatedBefore() != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("lastUpdateTime"), criteria.getUpdatedBefore()));
        }
        //sort
        if(criteria.getSort()!=null){
            if(criteria.isDescending()){
                query.orderBy(cb.desc(root.get(criteria.getSort().getField())));
            }else{
                query.orderBy(cb.asc(root.get(criteria.getSort().getField())));
            }
        }

        TypedQuery<Order> typedQuery = em.createQuery(query.where(predicates.toArray(new Predicate[0])));

        //pagination
        int page = criteria.getPage();
        int size = criteria.getSize();
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }
}
