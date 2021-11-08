package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

import entities.Product;

public class Loja {
	
	static LinkedList<Product> stock = new LinkedList<>();
	static LinkedList<Product> shoppingCart = new LinkedList<>();

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		Scanner input = new Scanner(System.in);
		
		createStock(stock);
		showMainMenu(input);
	}

	public static void showMainMenu(Scanner input) {
		div("GAMA PAN SUPERMARKET");
		System.out.println("[1] - Show products");
		System.out.println("[2] - Show shopping cart");
		System.out.println("[0] - Exit");
		div("");
		
		System.out.print("Enter an option: ");
		int option = input.nextInt();

		switch (option) {
		case 1:
			showProducts(input);
			break;
		case 2:
			if(shoppingCart.isEmpty()) {
				System.out.println("\nShopping cart is empty. There is nothing to show! Try other option.\n");
				showMainMenu(input);
			} else {
			showShoppingCart(input);
			}
			break;
		case 0:
			System.out.println("\nThank you for your business. Bye!");
			System.exit(0);
			break;
		default:
			System.out.println("\nInvalid option. Try again!");
			showMainMenu(input);
		}
	}
	
	public static void showProducts(Scanner input) {
		boolean equal = false;
		div("List of Products");
		System.out.format("%2s%20s%22s%20s%n", "ID", "PRODUCT","UNIT PRICE","QUANTITY");
	
		for(Product x : stock) {
			System.out.format("%2d%20s%22.2f%20d%n", x.getProductId(), x.getName(), x.getPrice(), x.getQuantity());
		}
		
		System.out.println("\n[ID] - To add the desired product\n[-1] - Return to main menu\n[0]  - Show shopping cart");
		div("");
		
		System.out.print("Enter an option: ");
		int prod = input.nextInt();
		
		if(prod > 0 && prod <= stock.size()) {
			System.out.print("Enter the quantity to be added: ");
			int qtdRemove = input.nextInt();
			if(stock.get(prod-1).getQuantity() >= qtdRemove && qtdRemove > 0) {
				for(Product x : shoppingCart) {
					if(x.getProductId() == prod) {
						equal = true;
						x.increaseQtd(qtdRemove);
						stock.get(prod - 1).decreaseQtd(qtdRemove);
					}
				}
				
				if(!equal) {
					Product product = new Product(stock.get(prod-1).getProductId(), stock.get(prod-1).getName(), 
							stock.get(prod-1).getPrice(), stock.get(prod-1).getQuantity());
					product.setQuantity(qtdRemove);
					shoppingCart.add(product);
					stock.get(prod-1).decreaseQtd(qtdRemove);
				}
				System.out.println("Success! Item added to cart.");
				showProducts(input);
			} else {
				System.out.println("Quantity not available.");
				showProducts(input);
			}
		} else if(prod == 0) {
			if(shoppingCart.isEmpty()) {
				System.out.println("\nShopping cart is still empty. There is nothing to show!\n");
				showProducts(input);
			} else {
				showShoppingCart(input);
			}
		}else if(prod == -1){
			showMainMenu(input);
		} else {
			System.out.println("Incorrect value! Try again.");
			showProducts(input);
		}
	}
	
	public static void showShoppingCart(Scanner input) {
		div("SHOPPING CART");
		System.out.format("%2s%16s%16s%16s%16s%n", "ID", "PRODUCT","UNIT PRICE","QUANTITY", "TOTAL");
		
		int i = 1;
		for(Product x : shoppingCart) {
			System.out.format("%2d%16s%16.2f%16d%16.2f%n", i, x.getName(),
					x.getPrice(), x.getQuantity(), x.getPrice() * x.getQuantity());
			i++;
		}
		System.out.println();
		System.out.println("\n[1] - Payment \n[2] - To cancel a product\n[3] - Return to main menu");
		div("");
		
		System.out.print("Enter an option: ");
		int option = input.nextInt();
		
		if(option == 1) {
			payment(input);			
		} else if (option == 2) {
			removeProduct(input);
		} else if(option == 3) {
			showMainMenu(input);
		} else {
			System.out.println("Incorrect option! Try again.");
		}
	}
	
	
	public static void payment(Scanner input) {
		
		double total = 0;
		
		for(Product x : shoppingCart) {
			total += x.getPrice() * x.getQuantity();
		}
		
		div("PAYMENT MENU");
		System.out.printf("The total value of shopping is R$ %.2f%n", total);
		System.out.println("\n[1] - Banco Pan card\n[2] - Pix or cash");
		div("");
		System.out.print("Enter the option: ");
		int option = input.nextInt();
		
		if(option == 1) {
			System.out.println("\n[1] - In cash (15% of discount)\n"
					+ "[2] - 3 interest-free instalments\n" + "[3] - 6 installments with interest (1.5% p.m.)\n");
			
			System.out.print("Enter a option of payment: ");
			int parcelamento = input.nextInt();
			
			if(parcelamento == 1) {
				total *= 0.85;	
				invoice(total, 1, parcelamento, input);
			} else if(parcelamento == 2) {
				total /= 3;
				invoice(total, 3, parcelamento, input);
			} else if(parcelamento == 3) {
				total += total * 1.09344;
				total /= 6;
				invoice(total, 6, parcelamento, input);
			} else {
				System.out.println("Incorrect option. Enter a valid option");
				payment(input);
			}			
		} else if(option == 2) {
			invoice(total, 1, option, input);
		}
	}
	
	public static void removeProduct(Scanner input) {
		
		System.out.print("Enter ID of product to remove: ");
		int prod = input.nextInt();
		
		if(prod > 0 && prod <= shoppingCart.getLast().getProductId()) {
			System.out.print("Qual quantidade deseja remover? ");
			int removeQtd = input.nextInt();
			
			if(shoppingCart.get(prod-1).getQuantity() >= removeQtd) {
				shoppingCart.get(prod-1).decreaseQtd(removeQtd);
				stock.get(shoppingCart.get(prod-1).getProductId()-1).increaseQtd(removeQtd);
				System.out.println("Produto removido com sucesso!");
				showShoppingCart(input);
			}else {
				System.out.println("Quantidade inválida!");
				showShoppingCart(input);				
			}	
		}else {
			System.out.println("Incorret value! Enter a value from 1 to " + shoppingCart.size());
			showShoppingCart(input);
		}		
	}
	
	public static void invoice(double total, int installments, int parcelamento, Scanner input) {
		div("");
		System.out.println("Gama PAN Supermarket \nAv. dos Programadores nº6\nCNPJ: 11.123.456/0001-12");
		System.out.println("Data da compra:" + getDateTime());
				
		div("Nota Fiscal");
		
		System.out.format("%2s%22s%20s%20s%n", "PRODUCT","UNIT PRICE","QUANTITY","TOTAL");
		
		for (Product x : shoppingCart) {
			System.out.format("%2s%22.2f%20d%20.2f%n", x.getName(), x.getPrice(), x.getQuantity(),
					x.getPrice() * x.getQuantity());		
		}
		
		if(parcelamento == 1) {
		System.out.printf("\nDiscount on shopping: R$ %.2f%n", total/0.85*0.15);
		
		}else if(parcelamento == 2 || parcelamento == 3) {
			System.out.printf("Installment amount to be paid: %dx R$ %.2f%n", installments, total);
		}
		
		System.out.printf("Total amount to be paid: R$ %.2f%n", total*installments);
		System.out.printf("Tax amount: R$ %.2f%n", total*installments*0.09);
		div("");
		
		System.out.println("\n[1] - Return to main menu\n[2] - Exit");
		System.out.println();
		System.out.print("Enter a option: ");
		int option = input.nextInt();
		
		if(option == 1) {
			showMainMenu(input);
		}else {
			System.exit(0);
		}		
	}
		
	public static void createStock(LinkedList<Product>stock) {
		stock.add(new Product("Milk", 4.57, 10));
		stock.add(new Product("Cereal", 3.02, 10));
		stock.add(new Product("Rice", 10.20, 3));
		stock.add(new Product("Beans", 7.36, 7));
		stock.add(new Product("Corn meal", 4.50, 12));
		stock.add(new Product("Sugar", 11.25, 15));
		stock.add(new Product("Butter", 3.85, 5));
		stock.add(new Product("Pasta", 7.56, 8));
	}

	public static void div(String title) {
		System.out.println("\t\t\t\t" + title);
		System.out.println("=======================================================================");
	}
	
	private static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}
