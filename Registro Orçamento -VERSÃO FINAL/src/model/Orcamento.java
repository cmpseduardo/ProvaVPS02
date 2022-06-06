package model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

public class Orcamento {
	private int id;
	private String fornecedor, produto;
	private double precoProduto;
	private boolean melhorCusto;
	
	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");
	
	
	
	public Orcamento(int id) {
		this.id = id;
	}

	public Orcamento(int id, String fornecedor, String produto, double preco) {
		this.id = id;
		this.fornecedor = fornecedor;
		this.produto = produto;
		this.precoProduto = preco;
	}

	public Orcamento(String linha) {
		df.setCurrency(Currency.getInstance(BRASIL));
		this.id = Integer.parseInt(linha.split(";")[0]);
		this.fornecedor = linha.split(";")[1];
		this.produto = linha.split(";")[2];
		
		try {
			this.precoProduto = Double.parseDouble(df.parse(linha.split(";")[3]).toString());
		}catch (ParseException e) {
			System.out.println(e);
		}
	}

		public int getId() {
			return id;
			}
	
		public String getId(String s) {
			return String.format("%d", id);
			}

		public void setId(int id) {
			this.id = id;
			}

		public String getFornecedor() {
			return fornecedor;
			}

		public void setFornecedor(String fornecedor) {
			this.fornecedor = fornecedor;
			}

		public String getProduto() {
			return produto;
			}

		public void setProduto(String produto) {
			this.produto = produto;
			}

		public double getPreco() {
			return precoProduto;
			}
	
		public String getPreco(String s) {
			return String.format("%.2f", precoProduto);
			}

		public void setPreco(double preco) {
			this.precoProduto = preco;
			}

		public boolean isMaisBarato() {
			return melhorCusto;
			}

		public void setMaisBarato(boolean maisBarato) {
			this.melhorCusto = maisBarato;
			}
	
	
	

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		Orcamento other = (Orcamento) object;
		return id == other.id;
	}

	@Override
	public String toString() {
		return id + "\t" + fornecedor + "\t" + produto + "\t" + String.format("%.2f", precoProduto) + "\t" + melhorCusto + "\n";
	}
	
	public String toCSV() {
		return id + ";" + fornecedor + ";" + produto + ";" + precoProduto + "\r\n";
	}
}
