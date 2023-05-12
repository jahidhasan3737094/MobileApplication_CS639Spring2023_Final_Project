package com.example.expenseplanner;
public class BankDetails {
    private String id;
    private String bankName;
    private double balance;

    public BankDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(BankDetails.class)
    }

    public BankDetails(String id, String bankName, double balance) {
        this.id = id;
        this.bankName = bankName;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
