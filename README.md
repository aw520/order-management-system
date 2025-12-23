## Future Improvements

- [ ] Introduce product price history in ProductService
    - Persist historical price records with effective timestamps
    - Support audit and analytics use cases
    - Orders will continue to store price snapshots (no recalculation)

- [ ] Add price quote expiration and validation
- [ ] Use Kafka for messaging between order service and product service
- [ ] Handle validation errors in order service, ensure product service is able to rollback
