import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private BankAccount bankAccount;
    private Long timestamp;
    private Currency currency;
    private TransactionType operationType;
    private BigDecimal amount;

    public Transaction(BankAccount bankAccount, Long timestamp, Currency currency, TransactionType operationType, BigDecimal amount) {
        this.bankAccount = bankAccount;
        this.timestamp = timestamp;
        this.currency = currency;
        this.operationType = operationType;
        this.amount = amount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public TransactionType getOperationType() {
        return operationType;
    }

    public void setOperationType(TransactionType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        if(this.operationType.equals(TransactionType.DEPOSIT)||this.operationType.equals(TransactionType.TRANSFER_IN)) {
            return dateFormat.format(new Date(timestamp)) +
                    ", " + currency +
                    ", " + operationType +
                    ", +" + amount;
        } else if(this.operationType.equals(TransactionType.WITHDRAWAL)||this.operationType.equals(TransactionType.WITHDRAWAL_FEE)||
                this.operationType.equals(TransactionType.TRANSFER_OUT)||this.operationType.equals(TransactionType.TRANSFER_OUT_FEE)){
            return dateFormat.format(new Date(timestamp)) +
                    ", " + currency +
                    ", " + operationType +
                    ", -" + amount;
        }

        return "Transaction{" +
                "bankAccount=" + bankAccount +
                ", date=" + timestamp +
                ", currency=" + currency +
                ", operationType=" + operationType +
                ", amount=" + amount +
                '}';
    }
}
