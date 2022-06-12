import Exceptions.WithdrawalLimitReachedException;

import java.math.BigDecimal;
import java.util.*;

public class User {
    private final String userName;
    HashMap<Currency, BankAccount> userAccounts;
    private Date lastWithdrawalTimestamp;
    private int withdrawalCounter;

    public User(String userName) {
        this.userName = userName;
        this.userAccounts = new HashMap<Currency, BankAccount>();
        userAccounts.put(Currency.USD, new BankAccount(userName, Currency.USD));
        userAccounts.put(Currency.HKD, new BankAccount(userName, Currency.HKD));
        userAccounts.put(Currency.SGD, new BankAccount(userName, Currency.SGD));
    }

    public void deposit(Currency currency, String amount) {
        this.getUserAccounts().get(currency).deposit(amount);
    }

    // withdrawal without fee
    public void withdrawal(Currency currency, String amount) throws Exception {
        if (!isWithdrawalLimitReached()) {
            this.getUserAccounts().get(currency).withdrawal(amount);
            this.setLastWithdrawalTimestamp(new Date(System.currentTimeMillis()));
            this.incrementWithdrawalCounter();
        } else throw new WithdrawalLimitReachedException();
    }

    // withdrawal with fee
    public void withdrawal(Currency currency, String amount, User feeUser) throws Exception {
        if (!isWithdrawalLimitReached()) {
            this.getUserAccounts().get(currency).withdrawal(amount, feeUser.getUserAccounts().get(currency));
            this.setLastWithdrawalTimestamp(new Date(System.currentTimeMillis()));
            this.incrementWithdrawalCounter();
        } else throw new WithdrawalLimitReachedException();
    }

    public void transferIn(Currency currency, String amount) {
        this.getUserAccounts().get(currency).transferIn(amount);
    }

    // transferOut without fee
    public void transferOut(Currency currency, String amount, User targetUser) throws Exception {
        this.getUserAccounts().get(currency).transferOut(amount, targetUser.getUserAccounts().get(currency));
    }

    // transferOut with fee
    public void transferOut(Currency currency, String amount, User targetUser, User feeUser) throws Exception {
        this.getUserAccounts().get(currency).transferOut(
                amount,
                targetUser.getUserAccounts().get(currency),
                feeUser.getUserAccounts().get(currency)
        );
    }

    public boolean isWithdrawalLimitReached() {
        if (this.lastWithdrawalTimestamp == null) return false;
        long differenceInTime =
                new Date(System.currentTimeMillis()).getTime() - this.getLastWithdrawalTimestamp().getTime();
        long countdownDuration = 1000L * 60 * 5; //5 minutes
        if (differenceInTime >= countdownDuration) {
            this.setWithdrawalCounter(0);
            return false;
        } else return this.getWithdrawalCounter() >= 5;
    }

    public List<Transaction> getAllTransactionList() {
        List<Transaction> allTransactionList = new ArrayList<>();
        this.getUserAccounts().forEach(((currency, bankAccount) -> allTransactionList.addAll(bankAccount.getTransactionList())));
        Collections.sort(allTransactionList, Comparator.comparing(Transaction::getTimestamp));
        return allTransactionList;
    }

    public BigDecimal getBalanceByAccountCurrency(Currency currency) {
        return this.getUserAccounts().get(currency).getBalance();
    }

    public void incrementWithdrawalCounter() {
        this.withdrawalCounter++;
    }

    public void resetWithdrawalCounter() {
        this.withdrawalCounter = 0;
    }

    // getters and setters
    public HashMap<Currency, BankAccount> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(HashMap<Currency, BankAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public Date getLastWithdrawalTimestamp() {
        return lastWithdrawalTimestamp;
    }

    public void setLastWithdrawalTimestamp(Date lastWithdrawalTimestamp) {
        this.lastWithdrawalTimestamp = lastWithdrawalTimestamp;
    }

    public int getWithdrawalCounter() {
        return withdrawalCounter;
    }

    public void setWithdrawalCounter(int withdrawalCounter) {
        this.withdrawalCounter = withdrawalCounter;
    }

}
