package com.bolsadeideas.springboot.form.app.models.domain;

public class Role {
	private Integer id;
	private String nombre;
	private String role;
	// Constructor
	public Role() {}
	public Role(Integer id, String nombre, String role) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.role = role;
	}
	// Getters and Setters
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object obj) {
		Role role = (Role)obj;
		return this.id != null && this.id.equals(role.getId());
	}
	
}
