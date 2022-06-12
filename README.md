# BankingSystem
This is a simple Banking System. It can perform some basic operations including account creation, deposit, withdrawal and transfer.

## Build Environment
* OpenJDK version 18 (Minimun SDK: verison 14)
* IntelliJ IDEA 2022.1.2

## File Structure

```
📦src
 ┣ 📦Exceptions                   ## Storing custom exceptions
 ┣  ┣ 📜 InsufficientFundsException                 
 ┣  ┣ 📜 WithdrawalLimitReachedException         
 ┃                                ## Enums
 ┣ 📜Currency                       
 ┣ 📜TransactionType                       
 ┃                                ## Main Components
 ┣ 📜Main                             # Menu-driven main program
 ┣ 📜User                             # User class. A user object can have many BankAccounts
 ┣ 📜BankAccount                      # BankAccount class. A BankAccount must belongs to one user.
 ┣ 📜Transaction                      # Transaction class.
 ```

## Others
`OSL_FEE`, `Aice` and `Bob` are the pre-defined user accounts. For simplicity, user can login to the system by typing the username (case-sensitive)




