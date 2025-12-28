package com.example.productservice.repository.impl;

import com.example.productservice.dto.SearchCriteria;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jdk.jfr.Percentage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> search(SearchCriteria searchCriteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        //if given keyword
        if(searchCriteria.getKeyword() != null){
            String[] keywords = searchCriteria.getKeyword().toLowerCase().trim().split("\\s+");
            for(String keyword : keywords){
                predicates.add(cb.like(
                        cb.lower(root.get("productName").as(String.class)), "%" + keyword + "%")
                );
            }
        }

        //if only want instock
        if(searchCriteria.getInStock() != null){
            if(searchCriteria.getInStock()){
                predicates.add(cb.greaterThanOrEqualTo(root.get("quantity"), 1));
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        // sorting
        if(searchCriteria.getSort()!=null){
            boolean descending = searchCriteria.isDescending();
            if(descending){
                query.orderBy(cb.desc(root.get(searchCriteria.getSort().getField())));
            }else{
                query.orderBy(cb.asc(root.get(searchCriteria.getSort().getField())));
            }
        }


        //query.orderBy(cb.desc(root.get("creationTime")));

        TypedQuery<Product> typedQuery = em.createQuery(query);

        // pagination
        int pageSize = searchCriteria.getSize();
        int pageNumber = searchCriteria.getPage();
        int offset = (pageNumber - 1) * pageSize;

        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    @Override
    public int updateProductQuantity(UUID id, int delta) {
        Product product = em.find(Product.class, id);
        product.setQuantity(product.getQuantity() + delta);
        return em.merge(product).getQuantity();
    }
}
