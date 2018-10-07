package com.chinjiaxiong.hongleongchan;

import java.util.Stack;

public class Account {
    private double balance = 13200.0;
    private double spending = 2000.0;
    private double receiving = 3000.0;
    private String account_type = "saving";
    private Stack<Transaction> stack = new Stack<>();
    private String[] favourite_recipient = new String[]{
            "john",
            "andre",
    };

    // Initial for stack
    Account(){
        Transaction transaction = new Transaction("lee", 200f);
        stack.push(transaction);
    }

    public double getBalance() {
        return balance;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String get_cashflow(){
        return "You received " + receiving + " and spent " + spending + " this month";
    }

    public void send_amount(String name, double amount){
        this.balance -= amount;
        this.spending += amount;
        Transaction transaction = new Transaction(name, amount);
        stack.push(transaction);
    }

    public String last_transaction(){
        return stack.peek().toString();
    }
}

class Transaction {
    private String name;
    private double amount;
    // private String location;

    Transaction(String name, double amount){
        this.name = name;
        this.amount = amount;
        // this.location = location;
    }

    @Override
    public String toString() {
        return "You spend " + this.amount + " on " + this.name ;
    }
}
