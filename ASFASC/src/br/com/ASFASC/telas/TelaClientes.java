/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.ASFASC.telas;

import java.sql.*;
import br.com.ASFASC.dal.ModuloConexao;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class TelaClientes extends javax.swing.JInternalFrame {
    Connection conexao=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    /**
     * Creates new form TelaClientes
     */
    public TelaClientes() {
        initComponents();
        conexao=ModuloConexao.conector();
    }

    
    private void adicionar(){
    String sql="insert into tbuclientes(idcli,nomecli,telefonecli,emailcli,endereçocli,perfilcli) values(?,?,?,?,?,?)";
        try{
            pst=conexao.prepareStatement(sql);
            pst.setString(1,txtCliNome.getText());
            pst.setString(1,txtCliId.getText());
            pst.setString(2,txtCliEndereço.getText());
            pst.setString(3,txtCliTelefone.getText());
            pst.setString(4,txtCliEmail.getText());
            pst.setString(5,txtCliPlano.getText());

            int adicionado= pst.executeUpdate();
            if (adicionado>0) {
                JOptionPane.showMessageDialog(null,"cliente adicionado");
            }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        
private void pesquisarCliente() {
        String sql = "select idcli as id, nomecli as nome, endcli as endereço, Telefonecli as fone, emailcli as email from tbclientes where nomecli like ?";
        try {
            conexao = ModuloConexao.conector();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     * método usado para setar os campos de texto com o conteúdo da tabela
     */
    private void setarCampos() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEndereço.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliTelefone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliPlano.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        if (tblClientes.getModel().getValueAt(setar, 4) == null){
            txtCliEmail.setText(null);
        } else {
            txtCliEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        }        
        btnAdicionar.setEnabled(false);
        btnAlterar.setEnabled(true);
        btnRemover.setEnabled(true);
    }

    /**
     * Método responsável pela edição dos dados do cliente
     */
    private void editarCliente() { 
        int confirma = JOptionPane.showConfirmDialog(null, "Confima as alterações nos dados deste cliente?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "update tbclientes set nomecli=?,endcli=?,fonecli=?,emailcli=? where idcli=?";
            try {
                conexao = ModuloConexao.conector();
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliNome.getText());
                pst.setString(2, txtCliEndereço.getText());
                pst.setString(3, txtCliTelefone.getText());
                if (txtCliEmail.getText().equals("")) {
                pst.setString(4, null);
            } else {
                pst.setString(4, txtCliEmail.getText());
            }
                pst.setString(5, txtCliId.getText());
                if ((txtCliNome.getText().isEmpty()) || (txtCliTelefone.getText().isEmpty())|| (txtCliPlano.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                } else {
                    int adicionado = pst.executeUpdate();
                    if (adicionado > 0) {
                        JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso");
                        limpar();
                    }
                }
            } catch (SQLIntegrityConstraintViolationException e1) {
                JOptionPane.showMessageDialog(null, "Email não preenchido ou já existente.\nEscolha outro email.");
                txtCliEmail.setText(null);
                txtCliEmail.requestFocus();
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            } finally {
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
    }

    /**
     * Método responsável por excluir um cliente
     */
    private void excluirCliente() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confima a exclusão deste cliente?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbclientes where idcli=?";
            try {
                conexao = ModuloConexao.conector();
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    limpar();
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                }
            } catch (SQLIntegrityConstraintViolationException e1) {
                JOptionPane.showMessageDialog(null, "Exclusão não realizada.\nCliente possui OS pendente.");
            } catch (HeadlessException | SQLException e2) {
                JOptionPane.showMessageDialog(null, e2);

            } finally {
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
    }

    /**
     * Método responsável por limpar os campos e gerenciar os componentes
     */
    private void limpar() {
        txtCliPesquisar.setText(null);
        txtCliId.setText(null);
        txtCliNome.setText(null);
        txtCliEndereço.setText(null);
        txtCliTelefone.setText(null);
        txtCliEmail.setText(null);
        txtCliPlano.setText(null);
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
        btnAdicionar.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnRemover.setEnabled(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        txtCliEndereço = new javax.swing.JTextField();
        txtCliTelefone = new javax.swing.JTextField();
        txtCliEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        txtCliPlano = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setTitle("Cadastro de Clientes");
        setPreferredSize(new java.awt.Dimension(1050, 745));

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colInedex){
                return false;
            }
        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nome", "Id", "Telefone", "E-Mail", "Endereço", "Plano"
            }
        ));
        tblClientes.getTableHeader().setResizingAllowed(false);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblClientesAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel1.setPreferredSize(new java.awt.Dimension(150, 22));

        txtCliPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCliPesquisar.setPreferredSize(new java.awt.Dimension(70, 20));
        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon("C:\\Users\\user3\\Documents\\NetBeansProjects\\asfac2\\src\\br\\com\\ASFASC\\icones\\create.jpg")); // NOI18N
        btnAdicionar.setPreferredSize(new java.awt.Dimension(100, 100));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon("C:\\Users\\user3\\Documents\\NetBeansProjects\\asfac2\\src\\br\\com\\ASFASC\\icones\\update.jpg")); // NOI18N
        btnAlterar.setPreferredSize(new java.awt.Dimension(100, 100));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon("C:\\Users\\user3\\Documents\\NetBeansProjects\\asfac2\\src\\br\\com\\ASFASC\\icones\\delete.jpg")); // NOI18N
        btnRemover.setPreferredSize(new java.awt.Dimension(100, 100));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nome");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Endereço");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Telefone");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("E-Mail");

        txtCliNome.setPreferredSize(new java.awt.Dimension(64, 25));
        txtCliNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliNomeActionPerformed(evt);
            }
        });

        txtCliEndereço.setMinimumSize(new java.awt.Dimension(64, 25));

        txtCliTelefone.setPreferredSize(new java.awt.Dimension(64, 25));

        txtCliEmail.setPreferredSize(new java.awt.Dimension(64, 25));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("N° de ID");

        txtCliId.setPreferredSize(new java.awt.Dimension(64, 25));

        txtCliPlano.setMinimumSize(new java.awt.Dimension(64, 25));
        txtCliPlano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPlanoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Plano");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 964, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(txtCliNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCliEndereço, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCliTelefone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCliEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6)
                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 776, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCliPlano, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliEndereço, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliPlano, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        editarCliente();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // TODO add your handling code here:
        excluirCliente();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
        // TODO add your handling code here:
        pesquisarCliente();
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void tblClientesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblClientesAncestorAdded
        // TODO add your handling code here:
        setarCampos();
    }//GEN-LAST:event_tblClientesAncestorAdded

    private void txtCliNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliNomeActionPerformed

    private void txtCliPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPlanoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliPlanoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEndereço;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliPlano;
    private javax.swing.JTextField txtCliTelefone;
    // End of variables declaration//GEN-END:variables
}
