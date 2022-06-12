package Exceptions;

public class WithdrawalLimitReachedException extends Exception {
    public WithdrawalLimitReachedException() {
        super("Withdrawal Limit Reached");
    }
}
