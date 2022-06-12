import Exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankAccount {
    // instance variables for account object
    private String userName;
    private Currency currency;
    private BigDecimal balance;
    private List<Transaction> transactionList;

    public BankAccount(String userName, Currency currency) {
        this.userName = userName;
        this.currency = currency;
        this.balance = BigDecimal.valueOf(0);
        this.transactionList = new ArrayList<>();
    }

    public synchronized void deposit(String amount) {
        BigDecimal deposit = new BigDecimal(amount);
        BigDecimal newBalance = this.getBalance().add(deposit);
        this.setBalance(newBalance);
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.DEPOSIT,
                deposit
        ));
    }

    // withdrawal without fee
    public synchronized void withdrawal(String amount) throws Exception {
        BigDecimal withdrawal = new BigDecimal(amount);
        BigDecimal newBalance = this.getBalance().subtract(withdrawal);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        this.setBalance(newBalance);
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.WITHDRAWAL,
                withdrawal
        ));
    }

    // withdrawal with fee
    public synchronized void withdrawal(String amount, BankAccount feeAccount) throws Exception {
        BigDecimal withdrawal = new BigDecimal(amount);
        BigDecimal fee = withdrawal.movePointLeft(2);
        BigDecimal newBalance = this.getBalance().subtract(withdrawal).subtract(fee);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        this.setBalance(newBalance);
        feeAccount.transferIn(String.valueOf(fee));
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.WITHDRAWAL,
                withdrawal
        ));

        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.WITHDRAWAL_FEE,
                fee
        ));
    }

    public synchronized void transferIn(String amount) {
        BigDecimal transfer = new BigDecimal(amount);
        BigDecimal newBalance = this.getBalance().add(transfer);
        this.setBalance(newBalance);
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.TRANSFER_IN,
                transfer
        ));
    }

    // transferOut without fee
    public synchronized void transferOut(String amount, BankAccount targetAccount) throws Exception{
        BigDecimal transfer = new BigDecimal(amount);
        BigDecimal newBalance = this.getBalance().subtract(transfer);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        this.setBalance(newBalance);
        targetAccount.transferIn(amount);

        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.TRANSFER_OUT,
                transfer
        ));
    }

    // transferOut with fee
    public synchronized void transferOut(String amount, BankAccount targetAccount, BankAccount feeAccount) throws Exception{
        BigDecimal transfer = new BigDecimal(amount);
        BigDecimal fee = transfer.movePointLeft(2);
        BigDecimal newBalance = this.getBalance().subtract(transfer).subtract(fee);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        this.setBalance(newBalance);
        targetAccount.transferIn(amount);
        feeAccount.transferIn(String.valueOf(fee));
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.TRANSFER_OUT,
                transfer
        ));
        this.addTransaction(new Transaction(
                this,
                System.currentTimeMillis(),
                this.getCurrency(),
                TransactionType.TRANSFER_OUT_FEE,
                fee
        ));
    }

    // getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionList.add(transaction);
    }
}

