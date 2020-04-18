
package com.xassure.dbTables;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.xassure.dbTables.paymentsId;

@Entity
@Table(name = "payments")
public class payments {

    private String amount;
    private Date paymentDate;
    private paymentsId _paymentsId;
    private customers _customers;

    public payments() {
    }

    @Column(name = "amount", nullable = false, length  = 10)
    public String getamount() {
        return this.amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    @Column(name = "paymentDate", nullable = false, length  = 10)
    public Date getpaymentDate() {
        return this.paymentDate;
    }

    public void setpaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @EmbeddedId
    public paymentsId getpaymentsId() {
        return this._paymentsId;
    }

    public void setpaymentsId(paymentsId _paymentsId) {
        this._paymentsId = _paymentsId;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customerNumber", insertable = false, updatable = false)
    public customers getcustomers() {
        return this._customers;
    }

    public void setcustomers(customers _customers) {
        this._customers = _customers;
    }

}
