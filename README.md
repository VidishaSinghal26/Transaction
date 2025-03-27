# Transaction Management System

This is a Spring Boot application for managing customer transactions and calculating reward points based on their purchases.

## Features
- Add new transactions  
- Retrieve reward points by customer and month  
- Get total rewards for all customers  
- Fetch transactions by month  

## Technologies Used
- **Java** (Spring Boot, JPA)  
- **MySQL** (Database)  

## API Endpoints
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/rewards/add` | Add a new transaction |
| `GET` | `/rewards/getAllRewards` | Get monthly rewards for all customers |
| `GET` | `/rewards/getRewardsByCustomer/{customerName}` | Get rewards by customer name |
| `GET` | `/rewards/totalRewardsForAll` | Get total rewards for all customers |
| `GET` | `/rewards/transactionsByMonth/{month}` | Get transactions by month |


Use Postman or any API client to test the endpoints.

