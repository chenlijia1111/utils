package com.github.chenlijia1111.util.excel;

import com.github.chenlijia1111.utils.office.excel.annos.ExcelImportField;

import java.util.Date;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/10/11 0011 下午 7:11
 **/
public class ImportPojo {

    @ExcelImportField(cellIndex = 0)
    private String account;

    @ExcelImportField(cellIndex = 1)
    private String bank;

    @ExcelImportField(cellIndex = 2)
    private String userName;

    @ExcelImportField(cellIndex = 4)
    private Date transactionDate;

    @ExcelImportField(cellIndex = 3)
    private Double money;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "ImportPojo{" +
                "account='" + account + '\'' +
                ", bank='" + bank + '\'' +
                ", userName='" + userName + '\'' +
                ", transactionDate=" + transactionDate +
                ", money=" + money +
                '}';
    }
}
