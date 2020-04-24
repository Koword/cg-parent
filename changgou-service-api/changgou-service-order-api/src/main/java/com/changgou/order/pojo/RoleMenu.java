package com.changgou.order.pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/****
 * @Author:shenkunlin
 * @Description:RoleMenu构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_role_menu")
public class RoleMenu implements Serializable{

	@Id
    @Column(name = "role_id")
	private Integer roleId;//

    @Column(name = "menu_id")
	private String menuId;//



	//get方法
	public Integer getRoleId() {
		return roleId;
	}

	//set方法
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	//get方法
	public String getMenuId() {
		return menuId;
	}

	//set方法
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}


}
