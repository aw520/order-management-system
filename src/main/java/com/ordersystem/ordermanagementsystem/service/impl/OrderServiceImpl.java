package com.ordersystem.ordermanagementsystem.service.impl;

import com.ordersystem.ordermanagementsystem.constant.OrderStatus;
import com.ordersystem.ordermanagementsystem.response.OrderResponse;
import com.ordersystem.ordermanagementsystem.dto.ResponseOrderItem;
import com.ordersystem.ordermanagementsystem.dto.SearchCriteria;
import com.ordersystem.ordermanagementsystem.entity.Order;
import com.ordersystem.ordermanagementsystem.entity.OrderProduct;
import com.ordersystem.ordermanagementsystem.entity.Product;
import com.ordersystem.ordermanagementsystem.entity.User;
import com.ordersystem.ordermanagementsystem.exception.*;
import com.ordersystem.ordermanagementsystem.repository.OrderRepository;
import com.ordersystem.ordermanagementsystem.repository.OrderRepositoryCustom;
import com.ordersystem.ordermanagementsystem.repository.ProductRepository;
import com.ordersystem.ordermanagementsystem.repository.UserRepository;
import com.ordersystem.ordermanagementsystem.dto.RequestOrderItem;
import com.ordersystem.ordermanagementsystem.request.OrderCreateRequest;
import com.ordersystem.ordermanagementsystem.request.OrderSearchRequest;
import com.ordersystem.ordermanagementsystem.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ordersystem.ordermanagementsystem.service.OrderService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    //orderCreation but not confirmed
    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        if(hasRole("ADMIN")&&orderCreateRequest.getUserId()!=null){
            Order order = submitOrderForUser(orderCreateRequest, UUID.fromString(orderCreateRequest.getUserId()));
            return getResponseOrder(order);
        }else if((orderCreateRequest.getUserId()==null
                    ||(SecurityUtil.getCurrentUserId().equals(UUID.fromString(orderCreateRequest.getUserId()))))
                &&hasRole("CLIENT")){
            Order order = submitOrderForUser(orderCreateRequest, SecurityUtil.getCurrentUserId());
            return getResponseOrder(order);
        }else{
            throw new PermissionDeniedException("create order");
        }
    }


    private Order submitOrderForUser(OrderCreateRequest orderCreateRequest, UUID userId){
        BigDecimal price = BigDecimal.ZERO;
        Order order = Order.builder()
                .orderStatus(OrderStatus.NEW)
                .build();
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId.toString()));
        order.setUser(user);
        for(RequestOrderItem item : orderCreateRequest.getOrderItems()){
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(()-> new ProductNotFoundException(item.getProductId().toString()));

            BigDecimal unitPrice = product.getProductPrice();
            price = price.add(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());
            orderProduct.setPrice(unitPrice); // snapshot
            order.addProduct(orderProduct);
        }
        order.setPrice(price);
        order.setCurrency(orderCreateRequest.getCurrency());
        ZonedDateTime now = ZonedDateTime.now();
        order.setCreationTime(now);
        order.setLastUpdateTime(now);
        order = orderRepository.save(order);
        return order;
    }

    //orderComfirmation
    @Override
    @Transactional
    public OrderResponse confirmOrder(UUID orderId){
        if(!hasRole("ADMIN")){
            throw new PermissionDeniedException("confirm order");
        }
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        if(!order.getOrderStatus().equals(OrderStatus.NEW)){
            throw new OrderConfirmationFailedException("cannot confirm a " + order.getOrderStatus().toString() + " order");
        }
        for(OrderProduct op : order.getOrderProducts()){
            Product product = op.getProduct();
            if(op.getQuantity()>product.getQuantity()){
                throw new OrderConfirmationFailedException(product.getProductName() + " is out of stock");
            }
            product.setQuantity(product.getQuantity()-op.getQuantity());
        }
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setLastUpdateTime(ZonedDateTime.now());
        return getResponseOrder(order);
    }


    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        //can only update from confirmed to shipped,
        //only non client can do this

        Order order = orderRepository.findById(orderId).orElse(null);
        if(!hasRole("ADMIN")){
            throw new PermissionDeniedException("update order");
        }
        if(order == null){
            throw new OrderNotFoundException(orderId.toString());
        }
        if(!order.getOrderStatus().equals(OrderStatus.CONFIRMED)
        ||!newStatus.equals(OrderStatus.SHIPPED)){
            throw new OrderStatusUpdateFailedException("cannot update order status from " + order.getOrderStatus().toString() + " to " + newStatus.toString());
        }
        order.setOrderStatus(newStatus);
        order.setLastUpdateTime(ZonedDateTime.now());
        return getResponseOrder(order);
    }



    /*
    @Override
    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID userId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException(orderId.toString());
        }
        if(!order.getUser().getUserId().equals(userId)){
            throw new PermissionDeniedException("update order");
        }
        order.setOrderStatus(newStatus.getDbValue());
        order.setLastUpdateTime(ZonedDateTime.now());
        return order;
    }
    */

    /*
    @Override
    public List<Order> searchOrders(SearchCriteria searchCriteria) {
        return orderRepositoryCustom.searchOrder(searchCriteria);
    }
    */

    @Override
    public List<OrderResponse> searchOrders(OrderSearchRequest orderSearchRequest) {
        //choose what to call based on a user's role
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .orderId(UUID.fromString(orderSearchRequest.getOrderId()))
                .pageNumber(orderSearchRequest.getPageNumber())
                .pageSize(orderSearchRequest.getPageSize())
                .status(OrderStatus.getFromDbValue(orderSearchRequest.getStatus()))
                .build();
        List<Order> orders;
        if(hasRole("ADMIN")){
            orders = orderRepositoryCustom.searchOrder(searchCriteria);
        }else{
            orders =  orderRepositoryCustom.searchOrder(searchCriteria, SecurityUtil.getCurrentUserId());
        }
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order : orders){
            orderResponses.add(getResponseOrder(order));
        }
        return orderResponses;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID orderId) {
        //choose what to do based on a user's role
        //only cancel before shipped
        Order order = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException(orderId.toString()));
        if(!OrderStatus.canCancel(order.getOrderStatus())){
            throw new CancellationFailedException("cannot cancel an order that has already been shipped");
        }
        if(!hasRole("ADMIN")){
            if(!order.getUser().getUserId().equals(SecurityUtil.getCurrentUserId())){
                throw new PermissionDeniedException("cancel order");
            }
        }

        if(order.getOrderStatus().equals(OrderStatus.CONFIRMED)){
            List<OrderProduct> orderProduct = order.getOrderProducts();
            for(OrderProduct op : orderProduct){
                Product product = op.getProduct();
                product.setQuantity(product.getQuantity()+op.getQuantity());
            }
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setLastUpdateTime(ZonedDateTime.now());
        return getResponseOrder(order);
    }


    public OrderResponse getResponseOrder(Order order){
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getOrderStatus())
                .totalPrice(order.getPrice())
                .currency(order.getCurrency())
                .createdAt(order.getCreationTime())
                .updatedAt(order.getLastUpdateTime())
                .build();
        List<ResponseOrderItem> list = new ArrayList<>();
        for(OrderProduct op : order.getOrderProducts()){
            ResponseOrderItem item = ResponseOrderItem.builder()
                    .productName(op.getProduct().getProductName())
                    .quantity(op.getQuantity())
                    .unitPrice(op.getPrice())
                    .build();
            list.add(item);
        }
        orderResponse.setItems(list);
        return orderResponse;
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

}
