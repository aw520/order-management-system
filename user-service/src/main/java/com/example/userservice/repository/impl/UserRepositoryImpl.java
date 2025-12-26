package com.example.userservice.repository.impl;

import com.example.userservice.constant.UserRole;
import com.example.userservice.dto.UserSearchCriteria;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepositoryCustom;
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
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> searchUser(UserSearchCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        //email
        if(criteria.getEmail() != null){
            predicates.add(cb.like(
                    cb.lower(root.get("email").as(String.class)), "%" + criteria.getEmail().toLowerCase() + "%")
            );
        }
        //first name
        if(criteria.getFirstName() != null){
            predicates.add(cb.like(
                    cb.lower(root.get("firstName").as(String.class)), "%" + criteria.getFirstName().toLowerCase() + "%")
            );
        }
        //last name
        if(criteria.getLastName() != null){
            predicates.add(cb.like(
                    cb.lower(root.get("lastName").as(String.class)), "%" + criteria.getLastName().toLowerCase() + "%")
            );
        }
        //role
        //filter out the users contains all roles in Set<UserRole>
        if (criteria.getRoles() != null && !criteria.getRoles().isEmpty()) {
            for (UserRole role : criteria.getRoles()) {
                predicates.add(
                        cb.like(
                                root.get("roles").as(String.class),
                                "%" + role.name() + "%"
                        )
                );
            }
        }

        //sort
        if(criteria.getSort()!=null){
            if(criteria.isDescending()){
                query.orderBy(cb.desc(root.get(criteria.getSort().getField())));
            }else{
                query.orderBy(cb.asc(root.get(criteria.getSort().getField())));
            }
        }

        TypedQuery<User> typedQuery = em.createQuery(query.where(predicates.toArray(new Predicate[0])));

        //pagination
        int page = criteria.getPage();
        int size = criteria.getSize();
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }
}
