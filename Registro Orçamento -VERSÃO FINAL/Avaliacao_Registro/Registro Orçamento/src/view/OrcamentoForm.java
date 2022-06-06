package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controllers.ProcessaOrcamento;
import model.Orcamento;

public class OrcamentoForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel painel;
	private JLabel id, prod, fornecedor, valor;
	private JTextField tfId, tfProd, tfFornecedor, tfValor;
	private JScrollPane rolagem;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton cadastrar, buscar, atualizar, deletar;
	private int autoId = ProcessaOrcamento.orcamentos.get(ProcessaOrcamento.orcamentos.size()-1).getId() + 1;

	//DEFINIÇÕES DE LOCAL
	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");

	
	//DEFINIÇÕES DA TELA
	public OrcamentoForm() {
		setTitle("Registro de Orçamentos");
		setBounds(150, 170, 800, 600);
		painel = new JPanel();
		painel.setBackground(new Color(119, 25, 62));
		setContentPane(painel);
		setLayout(null);
	
		//ID
			id = new JLabel("ID:");
			id.setBounds(115, 20, 120, 30);
			id.setForeground(new Color(255,255,255));
			painel.add(id);
			
		//PRODUTO
			prod = new JLabel("Produto:");
			prod.setBounds(80, 70, 120, 30);
			painel.add(prod);
			prod.setForeground(new Color(255,255,255));
			
		//FORNECEDOR
			fornecedor = new JLabel("Fornecedor:");
			fornecedor.setBounds(60, 120, 120, 30);
			painel.add(fornecedor);
			fornecedor.setForeground(new Color(255,255,255));
			
		//VALOR
			valor = new JLabel("Valor:");
			valor.setBounds(95, 170, 120, 30);
			painel.add(valor);
			valor.setForeground(new Color(255,255,255));
			
			
		//TF'S
			tfId = new JTextField(String.format("%d", autoId));
			tfId.setEditable(false);
			tfId.setBounds(140, 25, 80, 35);
			painel.add(tfId);
			tfProd = new JTextField();
			tfProd.setBounds(140, 75, 315, 35);
			painel.add(tfProd);
			tfFornecedor = new JTextField();
			tfFornecedor.setBounds(140, 125, 315, 35);
			painel.add(tfFornecedor);
			tfValor = new JTextField();
			tfValor.setBounds(140, 175, 315, 35);
			painel.add(tfValor);
	
			table = new JTable();
			tableModel = new DefaultTableModel();
			tableModel.addColumn("Id");
			tableModel.addColumn("Produto");
			tableModel.addColumn("Fornecedor");
			tableModel.addColumn("Valor");
			if (ProcessaOrcamento.orcamentos.size() != 0) {
				preencherTabela();
		}
			
		
		//DEFINIÇÕES DA TABELA
		table = new JTable(tableModel);
		table.setEnabled(false);
		rolagem = new JScrollPane(table);
		rolagem.setBounds(20, 310, 740, 230);
		painel.add(rolagem);

		
		//BOTÕES
			cadastrar = new JButton("Cadastrar");
			buscar = new JButton("Buscar");
			atualizar = new JButton("Atualizar");
			deletar = new JButton("Excluir");
			
			
		//POSIÇÕES BOTÕES
			cadastrar.setBounds(525, 50, 150, 35);
			buscar.setBounds(525, 100, 150, 35);
			atualizar.setBounds(525, 150, 150, 35);
			deletar.setBounds(525, 200, 150, 35);
			
			
		//SETAGEM BOTÕES
			atualizar.setEnabled(false);
			deletar.setEnabled(false);
			
			painel.add(cadastrar);
			painel.add(buscar);
			painel.add(atualizar);
			painel.add(deletar);
	
		//ADD's	
			tfProd.addActionListener(this);
			cadastrar.addActionListener(this);
			buscar.addActionListener(this);
			atualizar.addActionListener(this);
			deletar.addActionListener(this);

	}
	
	private void limparCampos() {
		tfId.setText(String.format("%d",autoId));
		tfProd.setText(null);
		tfFornecedor.setText(null);
		tfValor.setText(null);
	}

	private void preencherTabela() {
		int totLinhas = tableModel.getRowCount();
		if (tableModel.getRowCount() > 0) {
			for (int i = 0; i < totLinhas; i++) {
				tableModel.removeRow(0);
			}
		}
		for (Orcamento or : ProcessaOrcamento.orcamentos) {
			tableModel.addRow(new String[] { or.getId("s"), or.getProduto(), or.getFornecedor(), or.getPreco("s") });
		}
	}

	private void cadastrar() {
		if (tfFornecedor.getText().length() != 0 && tfProd.getText().length() != 0 && tfFornecedor.getText().length() != 0 && tfValor.getText().length() != 0) {
			df.setCurrency(Currency.getInstance(BRASIL));
			double valor;
			try {
				valor = Double.parseDouble(df.parse(tfValor.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				valor = 0;
			}

			ProcessaOrcamento.orcamentos.add(new Orcamento(autoId, tfProd.getText(), tfFornecedor.getText(), valor));
			autoId++;
			preencherTabela();
			limparCampos();
			ProcessaOrcamento.salvar();
		} else {
			JOptionPane.showMessageDialog(this, "Por gentileza, preencha todos os campos.");
		}
	}

	private void buscar() {
		String entrada = JOptionPane.showInputDialog(this, "Digite o ID do Orçamento:");
		boolean isNumeric = true;
		if (entrada != null && !entrada.equals("")) {
			for (int i = 0; i < entrada.length(); i++) {
				if (!Character.isDigit(entrada.charAt(i))) {
					isNumeric = false;
				}
			}
		} else {
			isNumeric = false;
		}
		if (isNumeric) {
			int id = Integer.parseInt(entrada);
			Orcamento orcamento = new Orcamento(id);
			if (ProcessaOrcamento.orcamentos.contains(orcamento)) {
				int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
				tfId.setText(ProcessaOrcamento.orcamentos.get(indice).getId("s"));
				tfProd.setText(ProcessaOrcamento.orcamentos.get(indice).getProduto());
				tfFornecedor.setText(ProcessaOrcamento.orcamentos.get(indice).getFornecedor());
				tfValor.setText(ProcessaOrcamento.orcamentos.get(indice).getPreco("s"));
				cadastrar.setEnabled(false);
				atualizar.setEnabled(true);
				deletar.setEnabled(true);
				ProcessaOrcamento.salvar();
			} else {
				JOptionPane.showMessageDialog(this, "Orçamento não encontrado!");
			}
		}

	}

	private void alterar() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento orcamento = new Orcamento(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
		if (tfProd.getText().length() != 0 && tfFornecedor.getText().length() != 0 && tfValor.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));
			double preco;
			try {
				preco = Double.parseDouble(df.parse(tfValor.getText()).toString());
			} catch (ParseException e) {
				System.out.println(e);
				preco = 0;
			}

			ProcessaOrcamento.orcamentos.set(indice, new Orcamento(id, tfProd.getText(), tfFornecedor.getText(), preco));
			preencherTabela();
			limparCampos();
		} else {
			JOptionPane.showMessageDialog(this, "Por Favor preencher todos os campos.");
		}
		cadastrar.setEnabled(true);
		atualizar.setEnabled(false);
		deletar.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		ProcessaOrcamento.salvar();
	}

	private void excluir() {
		int id = Integer.parseInt(tfId.getText());
		Orcamento orcamento = new Orcamento(id);
		int indice = ProcessaOrcamento.orcamentos.indexOf(orcamento);
		ProcessaOrcamento.orcamentos.remove(indice);
		preencherTabela();
		limparCampos();
		cadastrar.setEnabled(true);
		atualizar.setEnabled(false);
		deletar.setEnabled(false);
		tfId.setText(String.format("%d", autoId));
		ProcessaOrcamento.salvar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cadastrar) {
			cadastrar();
		}
		if (e.getSource() == buscar) {
			buscar();
		}
		if (e.getSource() == atualizar) {
			alterar();
		}
		if (e.getSource() == deletar) {
			excluir();
		}
	}
	
	public static void main(String[] args) {
		ProcessaOrcamento.abrir();
		OrcamentoForm of = new OrcamentoForm();
		of.setVisible(true);
	}
}
