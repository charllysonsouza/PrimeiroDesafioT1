package entities;

import java.util.Objects;

public class Product {
	
	private static int increment = 0;
	private int productId;
	private String name;
	private double price;
	private int quantity;
	
	public Product(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		productId = ++increment;
	}
	
	public Product(int id, String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.productId = id;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int id) {
		this.productId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", price=" + price + ", quantity=" + quantity
				+ "]";
	}
	
	public void decreaseQtd(int qtd) {
		quantity -= qtd;
	}
	
	public void increaseQtd(int qtd) {
		quantity += qtd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(name, other.name)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
	}

	
	
	
}
