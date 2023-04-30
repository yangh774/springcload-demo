package com.yangh.model;

import java.util.Objects;

public class User {
    private Integer atm_id;
    private String atm_name;
    private String atm_money;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(atm_id, user.atm_id) && Objects.equals(atm_name, user.atm_name) && Objects.equals(atm_money, user.atm_money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atm_id, atm_name, atm_money);
    }

    public Integer getAtm_id() {
        return atm_id;
    }

    public void setAtm_id(Integer atm_id) {
        this.atm_id = atm_id;
    }

    public String getAtm_name() {
        return atm_name;
    }

    public void setAtm_name(String atm_name) {
        this.atm_name = atm_name;
    }

    public String getAtm_money() {
        return atm_money;
    }

    public void setAtm_money(String atm_money) {
        this.atm_money = atm_money;
    }

    public User(Integer atm_id, String atm_name, String atm_money) {
        this.atm_id = atm_id;
        this.atm_name = atm_name;
        this.atm_money = atm_money;
    }
}