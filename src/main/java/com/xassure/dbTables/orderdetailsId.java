
package com.xassure.dbTables;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class orderdetailsId
    implements Serializable
{

    private int orderNumber;
    private String productCode;

    public orderdetailsId() {
    }

    public orderdetailsId(int orderNumber, String productCode) {
        this.orderNumber = orderNumber;
        this.productCode = productCode;
    }

    @Column(name = "orderNumber", nullable = false, length  = 11)
    public int getorderNumber() {
        return this.orderNumber;
    }

    public void setorderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name = "productCode", nullable = false, length  = 15)
    public String getproductCode() {
        return this.productCode;
    }

    public void setproductCode(String productCode) {
        this.productCode = productCode;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        orderdetailsId obj2 = ((orderdetailsId) obj);
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        if ((this.orderNumber == obj2 .getorderNumber())&&this.productCode.equals(obj2 .getproductCode())) {
            return true;
        }
        return false;
    }

    public int hashcode() {
        int tmp = 0;
        tmp = (orderNumber+productCode).hashCode();
        return tmp;
    }

}
