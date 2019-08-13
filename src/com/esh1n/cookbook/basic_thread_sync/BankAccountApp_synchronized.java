package com.esh1n.cookbook.basic_thread_sync;


import com.esh1n.cookbook.SysUtil;

public class BankAccountApp_synchronized {
    public static void main(String[] args) {
        Account account = new Account();
        account.setBalance(1000);

        Thread companyThread = new Thread(new Company(account));
        Thread bankThread = new Thread(new Bank(account));

        System.out.printf("Account : Initial Balance: %f\n", account.getBalance());
        companyThread.start();
        bankThread.start();
        try {
            companyThread.join();
            bankThread.join();
            System.out.printf("Account : Final Balance: %f\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Account {
        private double balance;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public synchronized void addAmount(double amount) {
            double tmp = balance;
            SysUtil.sleep(10);
            tmp += amount;
            balance = tmp;
        }

        public synchronized void subtractAmount(double amount) {
            double tmp = balance;
            SysUtil.sleep(10);
            tmp -= amount;
            balance = tmp;
        }

    }

    public static class Bank implements Runnable {

        private Account account;

        public Bank(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                account.subtractAmount(1000);
            }
        }
    }

    public static class Company implements Runnable {

        private Account account;

        public Company(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                account.addAmount(1000);
            }
        }
    }
}
