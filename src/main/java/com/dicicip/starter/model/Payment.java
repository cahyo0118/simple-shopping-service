package com.dicicip.starter.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "payments")
public class Payment {

    public enum PaymentMethod {
        transfer, virtual_account, cod
    }

    public enum PaymentStatus {
        pending, lunas, batal
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String invoice_number;
    public String payment_method;
    public String status;
    public Timestamp paid_at;
    public String receiver_name;
    public String receiver_address;
    public Integer product_id;
    public Double product_price;
    public Integer qty;
    public Double total;
    public String note;

}
