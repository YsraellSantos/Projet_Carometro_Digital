package interfaceGrafica;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.DaoConeccao;
import validacao.Validador;

public class InterfaceMain extends JFrame {

	// Instanciando objetos JDBC
	DaoConeccao conectar = new DaoConeccao();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	// Instanciando objetos para fluxo de bytes
	private FileInputStream fis;

	// Variaveis globais para armazenar o tamanho da imagem(bytes)
	private int tamanho;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatusBanco;
	private JLabel lblDataSistema;
	private JLabel lblRA;
	private JTextField texRA;
	private JTextField textCampoNome;
	private JLabel lblFoto;
	private JButton btnCarregarFoto;
	private JButton btnBuscar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfaceMain frame = new InterfaceMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfaceMain() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// Essa windowActivated assim que a tela é iniciada
				// A conecção com o banco ja é solicitada
				status(); // Concção
				data(); // Data do sistema
			}
		});
		addComponentListener(new ComponentAdapter() {

		});
		setTitle("Carometro");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(InterfaceMain.class.getResource("/img/instagram.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.activeCaption);
		panel.setForeground(new Color(255, 255, 255));
		panel.setBounds(0, 326, 687, 57);
		contentPane.add(panel);
		panel.setLayout(null);

		lblStatusBanco = new JLabel("");
		lblStatusBanco.setIcon(new ImageIcon(InterfaceMain.class.getResource("/img/dboff.png")));
		lblStatusBanco.setBounds(558, 10, 32, 32);
		panel.add(lblStatusBanco);

		lblDataSistema = new JLabel("");
		lblDataSistema.setBackground(SystemColor.desktop);
		lblDataSistema.setForeground(new Color(0, 0, 0));
		lblDataSistema.setFont(new Font("Arial", Font.BOLD, 20));
		lblDataSistema.setBounds(10, 10, 382, 39);
		panel.add(lblDataSistema);

		btnCarregarFoto = new JButton("Carregar Foto");
		btnCarregarFoto.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		btnCarregarFoto.setBounds(494, 285, 158, 31);
		contentPane.add(btnCarregarFoto);
		btnCarregarFoto.setBackground(new Color(255, 255, 255));
		btnCarregarFoto.addMouseListener(new MouseAdapter() {

		});

		btnCarregarFoto.setFont(new Font("Arial", Font.ITALIC, 18));

		lblRA = new JLabel("RA:");
		lblRA.setFont(new Font("Arial", Font.BOLD, 12));
		lblRA.setBounds(10, 41, 30, 25);
		contentPane.add(lblRA);

		texRA = new JTextField();
		texRA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {

				String caracteres = "0123456789";
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		texRA.setBounds(38, 42, 128, 23);
		contentPane.add(texRA);
		texRA.setColumns(10);
		texRA.setDocument(new Validador(6));

		JLabel lblNome = new JLabel("Nome Aluno:");
		lblNome.setFont(new Font("Arial", Font.BOLD, 12));
		lblNome.setBounds(10, 116, 76, 25);
		contentPane.add(lblNome);

		textCampoNome = new JTextField();
		textCampoNome.setBounds(96, 116, 257, 22);
		contentPane.add(textCampoNome);
		textCampoNome.setColumns(10);
		textCampoNome.setDocument(new Validador(30)); // Numero maximo definido no banco de dados

		lblFoto = new JLabel("");
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblFoto.setIcon(new ImageIcon(InterfaceMain.class.getResource("/img/camera.png")));
		lblFoto.setBounds(409, 10, 256, 256);
		contentPane.add(lblFoto);

		JButton btnCarregarFotoo = new JButton("");
		btnCarregarFotoo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdicionarFoto();
			}
		});
		btnCarregarFotoo.setToolTipText("Adicionar Foto");
		btnCarregarFotoo.setIcon(new ImageIcon(InterfaceMain.class.getResource("/img/create.png")));
		btnCarregarFotoo.setBounds(335, 239, 64, 64);
		contentPane.add(btnCarregarFotoo);

		btnBuscar = new JButton("Bucar");
		btnBuscar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				BuscarRa();
			}
		});
		btnBuscar.setFont(new Font("Arial", Font.ITALIC, 18));
		btnBuscar.setBackground(Color.WHITE);
		btnBuscar.setBounds(201, 41, 158, 31);
		contentPane.add(btnBuscar);

		// fin do construtor
	}

	private void status() {
		try {
			con = conectar.conectarr();
			if (con == null) {
				// System.out.println("Erro de conecção");
				lblStatusBanco.setIcon(new ImageIcon(InterfaceMain.class.getResource("/img/dboff.png")));
				// Neste campo quando a coneção não é feita com suscesso ele troca vai ezibir
				// uma imagen do banco de dados com um x

			} else {
				// System.out.println("Banco de dados conectado");
				lblStatusBanco.setIcon(new ImageIcon(InterfaceMain.class.getResource("/img/dbon.png")));
				// Neste campo quando a conecção é feita com suscesso, o banco de dados ezibe um
				// imagen
				// de um banco de dados

			}

			con.close();
		} catch (Exception erro) {
			System.out.println(erro);
		}
	}

	// Obtem a data do sistema
	// Date formate -> Pega a data do sistema
	private void data() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblDataSistema.setText(formatador.format(data));

	}

	// Carrega a foto
	private void carregarFoto() {
		JFileChooser fotoO = new JFileChooser();
		fotoO.setDialogTitle("Selecione o arquivo");
		fotoO.setFileFilter(
				new FileNameExtensionFilter("Arquivo de imagens (*.PNG,*.JPG,)*.JPEG", "png", "jpg", "jpeg"));
		int resultado = fotoO.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(fotoO.getSelectedFile());
				tamanho = (int) fotoO.getSelectedFile().length();
				Image fot = ImageIO.read(fotoO.getSelectedFile()).getScaledInstance(lblFoto.getWidth(),
						lblFoto.getHeight(), Image.SCALE_SMOOTH);
				lblFoto.setIcon(new ImageIcon(fot));
				lblFoto.updateUI();
			} catch (Exception erro) {
				System.out.println(erro);
			}
		}
	}

	private void AdicionarFoto() {

		if (textCampoNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o campo Nome");
			textCampoNome.requestFocus();
		} else {

			String inset = "insert into alunos(nome,foto) values(?,?)";
			try {
				con = conectar.conectarr();
				pst = con.prepareStatement(inset);
				pst.setString(1, textCampoNome.getText());
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Aluno cadrastrado com suscesso!");
				} else {
					JOptionPane.showMessageDialog(null, "Erro! Aluno não cadrastrado");
				}
				con.close();
			} catch (Exception erro) {
				System.out.println(erro);
			}
		}
	}

	private void BuscarRa() {
		if (btnBuscar.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Digite o RA");
			btnBuscar.requestFocus();

		} else {
			String readRA = "select * from alunos where  ra = ?";
			try {
				con = conectar.conectarr();
				pst = con.prepareStatement(readRA);
				pst.setString(1, texRA.getText());
				rs = pst.executeQuery();
				if (rs.next()) {
					textCampoNome.setText(rs.getString(2));
					Blob blob = (Blob) rs.getBlob(3);
					byte[] img = blob.getBytes(1, (int) blob.length());
					BufferedImage imagem = null;
					try {
						imagem = ImageIO.read(new ByteArrayInputStream(img));
					} catch (Exception erro) {
						System.out.println(erro);
					}
					ImageIcon icone = new ImageIcon(imagem);
					Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(),
							lblFoto.getHeight(), Image.SCALE_SMOOTH));
					lblFoto.setIcon(foto);
				} else {
					JOptionPane.showMessageDialog(null, "Aluno não Cadrastrado");
				}
				con.close();
			} catch (Exception erro) {
				System.out.println(erro);
			}
		}
	}

}
