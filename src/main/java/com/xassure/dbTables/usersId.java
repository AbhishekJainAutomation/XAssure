
package com.xassure.dbTables;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class usersId
    implements Serializable
{

    private int id;
    private String username;

    public usersId() {
    }

    public usersId(int id, String username) {
        this.id = id;
        this.username = username;
    }

    @Column(name = "id", nullable = false, length  = 11)
    public int getid() {
        return this.id;
    }

    public void setid(int id) {
        this.id = id;
    }

    @Column(name = "username", nullable = false, length  = 45)
    public String getusername() {
        return this.username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        usersId obj2 = ((usersId) obj);
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        if ((this.id == obj2 .getid())&&this.username.equals(obj2 .getusername())) {
            return true;
        }
        return false;
    }

    public int hashcode() {
        int tmp = 0;
        tmp = (id+username).hashCode();
        return tmp;
    }

}
